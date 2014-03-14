package org.openmhealth.dpu.process.bloodpressure.v1;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Blood Pressure measure.
 * 
 * @author Alberto Lagna
 *
 */
@Data @NoArgsConstructor
public class BloodPressureMeasure_v1{
	private int systolic;
	private int diastolic;
}

