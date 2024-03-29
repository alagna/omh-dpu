package org.openmhealth.dpu.controller;

import javax.annotation.PostConstruct;

import lombok.Setter;

import org.apache.log4j.Logger;
import org.openmhealth.dpu.process.DataProcessUnit;
import org.openmhealth.dpu.process.exception.BusinessException;
import org.openmhealth.dpu.process.exception.SystemException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * It is the access point to the DPUs. 
 * It exposes a REST interface with the two DPU methods. 
 * Given the <process name> of the requests dynamically looks for DPUs and forwards 
 * the JSON call to them. 
 * 
 * It catches the exception thrown by the engine and converts them into the proper 
 * error message and HTTP code.
 * 
 * It also logs requests and responses for better tracing and debugging
 * 
 * @author Alberto Lagna
 *
 */
@Controller
@RequestMapping(DataProcessUnitController.PATH)
public class DataProcessUnitController {
	
	@Setter @Autowired
	private ApplicationContext applicationContext;
	
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

		String res;
		try {
			res = getDPU(processName).process(input, true);
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
	public String readRegistry(
		@PathVariable(PARAM_PROCESS_NAME) final String processName) {
		
		log.debug("IN readRegistry (" + processName+ ")");

		String res;
		try {
			res = getDPU(processName).registryRead();
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
	 * Gives the right DPU, given the process name.
	 * The DPU have the following naming convention:
	 * DPU name = <process name camel case> + "DPU"
	 * 
	 * @param processname
	 */
	private DataProcessUnit getDPU(String processname) throws BusinessException {
		try {
			return (DataProcessUnit)applicationContext.getBean(processname+"DPU");
		} catch (BeansException be) {
			throw new BusinessException(BusinessException.NO_SUCH_PROCESS, be, log, processname);
		}
	}


	
}
