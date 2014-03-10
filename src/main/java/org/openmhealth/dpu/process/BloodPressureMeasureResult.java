package org.openmhealth.dpu.process;

import org.openmhealth.dpu.process.BloodPressureCalculator.BloodPressureCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class BloodPressureMeasureResult {
	private BloodPressureCategory category;
}
