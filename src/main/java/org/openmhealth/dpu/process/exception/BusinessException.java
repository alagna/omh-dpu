package org.openmhealth.dpu.process.exception;

import org.apache.log4j.Logger;


/**
 * Error due to the business logic of the application (process)
 * 
 * @author Alberto Lagna
 *
 */
public class BusinessException extends BaseException {

	private static final long serialVersionUID = -5965720802633509643L;
	
	// error dictionary
	public static final String INVALID_INPUT[] = {"invalid input", "invalid input parameter: {0}"};
	public static final String[] JSON_NOT_WELL_FORMED = {"json not well formed", "the json input is not well formed: {0}"};
	public static final String[] JSON_MAPPING_ERROR = {"json mapping error", "the json parser was not able to unmarshall the json string into the given domain class({1}): {0}"};
	public static final String[] NO_SUCH_PROCESS = {"no such process", "The process {0} is undefined"};
	
	public BusinessException(String errorDescription[], Exception thrownBy, Logger log, Object ... args) {
		super(errorDescription, thrownBy, log, args);
	}

}
