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
	
	public BusinessException(String errorDescription[], Exception thrownBy, Logger log, Object ... args) {
		super(errorDescription, thrownBy, log, args);
	}

}
