package org.openmhealth.dpu.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

import org.codehaus.jackson.map.ObjectMapper;
import org.openmhealth.dpu.domain.SchemaIdVersion;
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
public class BloodPressureCalculator implements DataProcessUnit {

	public static final String processName="blood_pressure";

	private ObjectMapper mapper = new ObjectMapper();

	
	/**
	 * Converts the JSON input into the calculate() input and the calculate() output into JSON 
	 */
	public String process(String jsonInput, boolean preserveRawInputData) {
		try {
			BloodPressureMeasure measure = mapper.readValue(jsonInput, BloodPressureMeasure.class);
			return mapper.writeValueAsString(new BloodPressureMeasureResult(calculate(measure)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Forwards the call towards the registry
	 */
	public List<SchemaIdVersion> registryRead() {
		return new ArrayList<>();
	}

	public enum BloodPressureCategory {
		normal, prehypertension, high_blood_pressure_stage1, high_blood_pressure_stage2, hypertensive_crisis
	}
	
	private BloodPressureCategory calculate(BloodPressureMeasure input){
		if (input.getSystolic()<120 && input.getDiastolic()<80)
			return BloodPressureCategory.normal;
		
		return BloodPressureCategory.hypertensive_crisis;
	}

}
