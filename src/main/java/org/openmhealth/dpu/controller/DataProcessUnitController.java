package org.openmhealth.dpu.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.Setter;

import org.apache.log4j.Logger;
import org.openmhealth.dpu.domain.SchemaIdVersion;
import org.openmhealth.dpu.process.bloodpressure.v1.BloodPressureDPU_v1;
import org.openmhealth.dpu.process.exception.BusinessException;
import org.openmhealth.dpu.process.exception.SystemException;
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
 * @author Alberto Lagna
 *
 */
@Controller
@RequestMapping(DataProcessUnitController.PATH)
public class DataProcessUnitController {
	
	@Setter @Autowired
	private BloodPressureDPU_v1 bloodPressureCalculator;
	
	/**
	 * Add here all configuration info you want to show at startup
	 */
	@PostConstruct
	public void init(){
		log.debug("DPU engine is ready");
	}
	
	/**
	 * The root path for queries to the DPU controller.
	 */
	public static final String PATH="/dpu";

	/**
	 * The parameter for the process name
	 */
	private static final String PARAM_PROCESS_NAME = "processName";

	private static Logger log = Logger.getLogger(DataProcessUnitController.class);
	
	/**
	 * Receives input data and forwards them to the right DPU (depending on the process name).
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

		log.debug("+---------------------------");
		log.debug("| " + processName + "(\n" + input + "\n)");

		// TODO look for the right DPU (depending on the process name)

		String res;
		try {
			res = bloodPressureCalculator.process(input, true);
			log.debug ("| response" + res);
			log.debug("+---------------------------");
		} catch (BusinessException | SystemException e) {
			res = e.getJsonString();
			log.debug ("| error" + res);
			log.debug("+---------------------------");
		}	

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
