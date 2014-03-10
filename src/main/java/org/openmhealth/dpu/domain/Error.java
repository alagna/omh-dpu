package org.openmhealth.dpu.domain;

import lombok.Data;

/**
 * Failure result
 * 
 * @author Alberto Lagna
 *
 */
@Data
public class Error {
	private String code;
	private String description;
}
