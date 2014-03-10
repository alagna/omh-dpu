package org.openmhealth.dpu.process;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * Converts the JSON input into the calculate() input and the calculate() output into JSON 
	 */
	public String process(String jsonInput, boolean preserveRawInputData) {
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
	
	@lombok.Data
	public class BloodPressureMeasure{
		private int systolic;
		private int diastolic;
	}
	
	private BloodPressureCategory calculate(BloodPressureMeasure input){
		if (input.getSystolic()<120 && input.getDiastolic()<80)
			return BloodPressureCategory.normal;
		
		return BloodPressureCategory.hypertensive_crisis;
	}

}
