package org.openmhealth.dpu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SchemaIdVersion {
	private String schemaId;
	private String schemaVersion;
}
