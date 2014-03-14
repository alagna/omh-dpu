package org.openmhealth.dpu.process.bloodpressure.v1;

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
public class BloodPressureCategoryWrapper_v1 {
	private BloodPressureCategory_v1 category;
}
