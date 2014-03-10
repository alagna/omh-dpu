package org.openmhealth.dpu.engine;

import java.util.List;

import lombok.Setter;

import org.apache.log4j.Logger;
import org.openmhealth.dpu.domain.SchemaIdVersion;
import org.openmhealth.dpu.process.BloodPressureCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@Setter @Autowired
	private BloodPressureCalculator bloodPressureCalculator;
	
	/**
	 * Current version
	 */
	public static final String VERSION = "v1";
	
	/**
	 * The root path for queries to this version of the API.
	 */
	public static final String PATH="/dpu/" + VERSION;

	/**
	 * The parameter for the process name
	 */
	private static final String PARAM_PROCESS_NAME = "processName";

	private static Logger log = Logger.getLogger(Version1Controller.class);
	
	/**
	 * Receives input data and forwards them to the right DPU.
	 * Returns the data received by the DPU.
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(
		value = "{" + PARAM_PROCESS_NAME + "}",
		method = RequestMethod.POST)
	@ResponseBody
	public String process(
		@PathVariable(PARAM_PROCESS_NAME) final String processName,
		@RequestBody String input) {

		log.debug("IN " + processName + "(\n" + input + "\n)");

		// TODO look for the right process and forwards the call to it 

		String res = bloodPressureCalculator.process(input, true);	
		log.debug ("OUT " + res);

		return res;
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
	public List<SchemaIdVersion> readRegistry(
		@PathVariable(PARAM_PROCESS_NAME) final String processName) {
		
		log.debug("IN readRegistry (" + processName+ ")");

		// TODO look for the right process and forward the call to it
		List<SchemaIdVersion> res = bloodPressureCalculator.registryRead();
		
		log.debug ("OUT " + res);
		return res;
	}


	
}
