package org.openmhealth.dpu.engine;

import org.apache.log4j.Logger;
import org.openmhealth.dpu.domain.SchemaIdVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for the DPU
 * 
 * @author alagna
 *
 */
@Controller
@RequestMapping(Version1Controller.PATH)
public class Version1Controller {
	
	/**
	 * Current version
	 */
	public static final String VERSION = "v1";
	
	/**
	 * The root path for queries to this version of the API.
	 */
	public static final String PATH="/dpu/" + VERSION;
	
	/**
	 * The parameter for the unique identifier for a schema. This is sometimes
	 * used as part of the URI for the RESTful implementation.
	 */
	public static final String PARAM_SCHEMA_ID = "schema_id";
	/**
	 * The parameter for the version of a schema. This is sometimes used as
	 * part of the URI for the RESTful implementation.
	 */
	public static final String PARAM_SCHEMA_VERSION = "schema_version";

	/**
	 * The parameter for the data when it is being uploaded.
	 */
	public static final String PARAM_DATA = "data";

	/**
	 * The parameter for the process name
	 */
	private static final String PARAM_PROCESS_NAME = "processName";

	private static Logger log = Logger.getLogger(Version1Controller.class);
	
	/**
	 * Receives data that belongs to one schema ID-version pair and returns data 
	 * that belongs to another schema ID-version pair
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(
		value = "process",
		method = RequestMethod.POST)
	@ResponseBody
	public String process(@RequestBody String data) {
			
		// TODO look for the right process and forwards the call to it 

		// log.debug(processName+ "([" +schemaId + ":" + schemaVersion + "]\n" + data + "\n)");
		log.debug(data);
		
		return data;
	}

	/**
	 * It returns the schema ID-version pairs that can be processed and the schema-ID 
	 * version pairs that are returned
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(
		value = "{" + PARAM_PROCESS_NAME + "}",
		method = RequestMethod.GET)
	@ResponseBody 		
	public Object readRegistry(
		@PathVariable(PARAM_PROCESS_NAME) final String processName) {
		
		// TODO look for the right process and forwards the call to it
		
		log.debug("readRegistry (" + processName+ ")");
		
		return new SchemaIdVersion(processName, VERSION);
	}


	
}
