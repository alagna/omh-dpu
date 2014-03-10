package org.openmhealth.dpu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Schema id and Schema version metedata information
 * 
 * @author Alberto Lagna
 *
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class SchemaIdVersion {
	private String schemaId;
	private String schemaVersion;
}
