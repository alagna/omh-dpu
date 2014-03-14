package org.openmhealth.dpu.process.bloodpressure;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openmhealth.dpu.process.DataProcessUnit;
import org.openmhealth.dpu.process.DataProcessUnitBaseImpl;
import org.openmhealth.dpu.process.SchemaIdVersion;
import org.openmhealth.dpu.process.exception.BusinessException;
import org.openmhealth.dpu.process.exception.SystemException;
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
public class BloodPressureDPU extends DataProcessUnitBaseImpl implements DataProcessUnit {

	private static final Logger log = Logger.getLogger(BloodPressureDPU.class);
	
	/**
	 * Converts the JSON input into the calculate() input and the calculate() output into JSON.
	 * The error management is an important part of the method.
	 */
	public String process(String jsonInput, boolean preserveRawInputData) 
		throws BusinessException, SystemException {

		// unmarshalling the input JSON string into the target object
		BloodPressureMeasure measure = (BloodPressureMeasure)unmarshllProcessInput(jsonInput, preserveRawInputData, BloodPressureMeasure.class);
		
		// calculating the result
		BloodPressureCategoryWrapper res = new BloodPressureCategoryWrapper(calculate(measure));
		
		// Marshalling the result into a JSON string
		return marshallProcessOutput(res);
	}

	/**
	 * Returns the schema id and version of the input and output.
	 * No error management is needed because the method is trivial.
	 * @throws SystemException 
	 */
	public String registryRead() throws SystemException {
		// calculating the result
		List<SchemaIdVersion> schemaIdVersions = new ArrayList<>();
		schemaIdVersions.add(new SchemaIdVersion("omh:dpu:bloodpressure", "1"));
		schemaIdVersions.add(new SchemaIdVersion("omh:dpu:bloodpressure", "1"));

		// Marshalling the result into a JSON string
		return marshallProcessOutput(schemaIdVersions);
	}

	
	
	/**
	 * Does the calculation provided by the DPU
	 * 
	 * @param input
	 * @return
	 */
	private BloodPressureCategory calculate(BloodPressureMeasure input) 
		throws BusinessException{
		if (input.getSystolic()<120 && input.getDiastolic()<80)
			return BloodPressureCategory.normal;
		if ((input.getSystolic()>=120 && input.getSystolic()<=139) ||
			(input.getDiastolic()>=80 && input.getDiastolic()<=89))
			return BloodPressureCategory.prehypertension;
		if ((input.getSystolic()>=140 && input.getSystolic()<=159) ||
			(input.getDiastolic()>=90 && input.getDiastolic()<=99))
			return BloodPressureCategory.high_blood_pressure_stage1;
		if ((input.getSystolic()>=160 && input.getSystolic()<=180) ||
				(input.getDiastolic()>=100 && input.getDiastolic()<=110))
			return BloodPressureCategory.high_blood_pressure_stage2;
		if ((input.getSystolic()>180) || (input.getDiastolic()>110))
			return BloodPressureCategory.hypertensive_crisis;

		throw new BusinessException(BusinessException.INVALID_INPUT, null, log, 
			" the systolic " + input.getSystolic() + 
			" and diastolic " + input.getDiastolic() + 
			" are incoherent, please consider to do another measure");
	}

}
