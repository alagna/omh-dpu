package org.openmhealth.dpu.process;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data @NoArgsConstructor
public class BloodPressureMeasure{
	private int systolic;
	private int diastolic;
}

