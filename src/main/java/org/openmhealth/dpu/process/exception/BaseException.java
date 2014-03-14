package org.openmhealth.dpu.process.exception;

import java.text.MessageFormat;

import lombok.Getter;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

/**
 * Base Exception for all the omh.dpu exceptions.
 * The subclasses need to contain the error dictionary, a group of static string:
 * - their name is the error code,
 * - their description will be used to create along with the args to create the real message 
 * 
 * @author Alberto Lagna
 *
 */
public abstract class BaseException extends Exception {

	private static final long serialVersionUID = -5571557404950367827L;

	@Getter 
	private String code;
	@Getter 
	private Throwable thrownBy;
	
	/**
	 * If the error has to be returned to the user, the HttpStatus to use.
	 * TODO a possible extension could be to customize the status, depending on the error code
	 * http://stackoverflow.com/questions/16232833/how-to-respond-with-http-400-error-in-a-spring-mvc-responsebody-method-returnin
	 */
	@Getter
	protected HttpStatus httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
	
	/**
	 * The only public constructor, used to build the message, based on the errorDescription of the
	 * error dictionary of the concrete exceptions
	 * 
	 * @param errorDescription
	 * @param thrownBy
	 * @param parameters
	 */
	public BaseException(String[] errorDescription, Throwable thrownBy, Logger log, Object ...args){
		// I would have to check if the errorDescription array is at least long 2, but 
		// (for the time being) I trust people writing error dictionary in the proper way
		super(MessageFormat.format(errorDescription[1], args));
		code=errorDescription[0];
		if (thrownBy!=null)
			log.error(getMessage(), thrownBy);
		else
			log.error(getMessage());
	}
	
	/**
	 * Transforms the current Exception into the proper Json string, conforming to the 
	 * {@Link org.openmhealth.dpu.process.exception.schema.BusinessException.json} and 
	 * {@Link org.openmhealth.dpu.process.exception.schema.SystemException.json} concordia schema
	 * @return
	 */
	public String getJsonString(){
		return "{\"" +getClass().getSimpleName() + "\":" + 
			"{\"code\":\""+ code + "\"},"+
			"{\"message\":\""+ getMessage() + "\"}"+
			"}";
	}
	
}
