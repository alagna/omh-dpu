package org.openmhealth.dpu.process.bloodpressure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The goal is to return from the BloodPressureDPU an object: The current class is an object wrapper
 * for the BloodPressureCategory enum item.
 * 
 * @author Alberto Lagna
 *
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class BloodPressureCategoryWrapper {
	private BloodPressureCategory category;
}
