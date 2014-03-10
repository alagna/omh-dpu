package org.openmhealth.dpu.process;

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
	public Data process(Data input, boolean preserveRawInputData) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Forwards the call towards the registry
	 */
	public void registryRead() {
		// TODO Auto-generated method stub
		
	}

	public enum BloodPressureCategory {
		normal, prehypertension, high_blood_pressure_stage1, high_blood_pressure_stage2, hypertensive_crisis
	}
	
	private BloodPressureCategory calculate(int systolic, int diastolic){
		if (systolic<120 && diastolic<80)
			return BloodPressureCategory.normal;
		
		return BloodPressureCategory.hypertensive_crisis;
	}

}
