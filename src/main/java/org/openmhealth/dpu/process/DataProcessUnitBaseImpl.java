package org.openmhealth.dpu.process;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmhealth.dpu.process.exception.BusinessException;
import org.openmhealth.dpu.process.exception.SystemException;
import org.springframework.stereotype.Service;

/**
 * Data Process Unit common implementation
 * @author Alberto Lagna
 *
 */
@Service
public class DataProcessUnitBaseImpl {

	public static final String processName="blood_pressure";

	protected ObjectMapper mapper = new ObjectMapper();
	private static final Logger log = Logger.getLogger(DataProcessUnitBaseImpl.class);

	
	/**
	 * Converts the JSON input into the calculate() input and the calculate() output into JSON.
	 * The error management is an important part of the method.
	 */
	protected Object unmarshllProcessInput(String jsonInput, boolean preserveRawInputData, Class inputClass) 
		throws BusinessException, SystemException {
		
		Object input;
		
		// unmarshalling the input JSON string into the target object
		try {
			input = mapper.readValue(jsonInput, inputClass);
		} catch (JsonParseException e) {
			throw new BusinessException(BusinessException.JSON_NOT_WELL_FORMED, e, log, jsonInput);		
		} catch (JsonMappingException e) {
			throw new BusinessException(BusinessException.JSON_MAPPING_ERROR, e, log, jsonInput, inputClass);		
		} catch (IOException e) {
			// It should never happen, but you never know
			throw new SystemException(SystemException.UNABLE_TO_READ_INPUT, e, log, jsonInput);		
		}
		return input;
	}
	
	/**
	 * 	Marshalling the result into a JSON string
	 * @param o
	 * @return
	 */
	protected String marshallProcessOutput(Object o) throws SystemException {
		try {
			return mapper.writeValueAsString(o);
		} catch (IOException e) {
			throw new SystemException(SystemException.MARSHALL_ERROR, e, log, o);
		}
	}

}
