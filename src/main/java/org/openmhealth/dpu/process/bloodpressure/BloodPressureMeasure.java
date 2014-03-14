package org.openmhealth.dpu.process.bloodpressure;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Blood Pressure measure.
 * 
 * @author Alberto Lagna
 *
 */
@Data @NoArgsConstructor
public class BloodPressureMeasure{
	private int systolic;
	private int diastolic;
}

