package org.openmhealth.dpu.process.exception;

import org.apache.log4j.Logger;


/**
 * Error due to the underlying platform: the file system is full, the db is not reachable are
 * typical errors that are thrown by this exception.
 * 
 * @author Alberto Lagna
 *
 */
public class SystemException extends BaseException {

	private static final long serialVersionUID = -5965720802633509643L;
	
	// Error dictionary
	public static final String UNMARSHALL_ERROR[]={"unmarshall error", "Unable to convert string into JSON object:{0}"};
	public static final String MARSHALL_ERROR[] = {"marshall error", "Unable to convert JSON object into string:{0}"};
	public static final String[] UNABLE_TO_READ_INPUT = {"unable to read input", "Unable to read an input source:{0}"};;
	
	public SystemException(String errorDescription[], Exception thrownBy, Logger log, Object ... args) {
		super(errorDescription, thrownBy, log, args);
	}

}
