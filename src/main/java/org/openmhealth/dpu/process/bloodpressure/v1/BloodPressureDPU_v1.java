package org.openmhealth.dpu.process.bloodpressure.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmhealth.dpu.process.exception.SystemException;
import org.openmhealth.dpu.domain.SchemaIdVersion;
import org.openmhealth.dpu.process.DataProcessUnit;
import org.openmhealth.dpu.process.exception.BusinessException;
import org.springframework.stereotype.Service;

/**
 * Data Process Unit that calculates the Blood Pressure Category, given the Systolic and
 * Distolic blood pressure.
 * 
 * @see http://www.heart.org/HEARTORG/Conditions/HighBloodPressure/AboutHighBloodPressure/Understanding-Blood-Pressure-Readings_UCM_301764_Article.jsp
 * 
 * @author Alberto Lagna
 *
 */
@Service
public class BloodPressureDPU_v1 implements DataProcessUnit {

	public static final String processName="blood_pressure";

	private ObjectMapper mapper = new ObjectMapper();
	private static final Logger log = Logger.getLogger(BloodPressureDPU_v1.class);

	
	/**
	 * Converts the JSON input into the calculate() input and the calculate() output into JSON.
	 * The error management is an important part of the method.
	 */
	public String process(String jsonInput, boolean preserveRawInputData) 
		throws BusinessException, SystemException {
		
		BloodPressureMeasure_v1 measure;
		
		// unmarshalling the input JSON string into the target object
		try {
			measure = mapper.readValue(jsonInput, BloodPressureMeasure_v1.class);
		} catch (JsonParseException e) {
			throw new BusinessException(BusinessException.JSON_NOT_WELL_FORMED, e, log, jsonInput);		
		} catch (JsonMappingException e) {
			throw new BusinessException(BusinessException.JSON_MAPPING_ERROR, e, log, jsonInput, BloodPressureMeasure_v1.class);		
		} catch (IOException e) {
			// It should never happen, but you never know
			throw new SystemException(SystemException.UNABLE_TO_READ_INPUT, e, log, jsonInput);		
		}
		
		// calculating the result
		BloodPressureCategoryWrapper_v1 res = new BloodPressureCategoryWrapper_v1(calculate(measure));
		
		// Marshalling the result into a JSON string
		try {
			return mapper.writeValueAsString(res);
		} catch (IOException e) {
			throw new SystemException(SystemException.MARSHALL_ERROR, e, log, jsonInput);
		}
	}

	/**
	 * Returns the schema id and version of the input and output.
	 * No error management is needed because the method is trivial.
	 */
	public List<SchemaIdVersion> registryRead() {
		List<SchemaIdVersion> schemaIdVersions = new ArrayList<>();
		schemaIdVersions.add(new SchemaIdVersion("omh:dpu:bloodpressure", "1"));
		schemaIdVersions.add(new SchemaIdVersion("omh:dpu:bloodpressure", "1"));
		
		return schemaIdVersions;
	}

	
	
	/**
	 * Does the calculation provided by the DPU
	 * 
	 * @param input
	 * @return
	 */
	private BloodPressureCategory_v1 calculate(BloodPressureMeasure_v1 input) 
		throws BusinessException{
		if (input.getSystolic()<120 && input.getDiastolic()<80)
			return BloodPressureCategory_v1.normal;
		if ((input.getSystolic()>=120 && input.getSystolic()<=139) ||
			(input.getDiastolic()>=80 && input.getDiastolic()<=89))
			return BloodPressureCategory_v1.prehypertension;
		if ((input.getSystolic()>=140 && input.getSystolic()<=159) ||
			(input.getDiastolic()>=90 && input.getDiastolic()<=99))
			return BloodPressureCategory_v1.high_blood_pressure_stage1;
		if ((input.getSystolic()>=160 && input.getSystolic()<=180) ||
				(input.getDiastolic()>=100 && input.getDiastolic()<=110))
			return BloodPressureCategory_v1.high_blood_pressure_stage2;
		if ((input.getSystolic()>180) || (input.getDiastolic()>110))
			return BloodPressureCategory_v1.hypertensive_crisis;

		throw new BusinessException(BusinessException.INVALID_INPUT, null, log, 
			" the systolic " + input.getSystolic() + 
			" and diastolic " + input.getDiastolic() + 
			" are incoherent, please consider to do another measure");
	}

}
