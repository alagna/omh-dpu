package org.openmhealth.dpu.process;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmhealth.dpu.process.bloodpressure.BloodPressureMeasure;

/**
 * Utility class used to build a JSON string from a 
 * 
 * @author Alberto Lagna
 *
 */
public class JsonStringBuilder {
	
	private static ObjectMapper mapper = new ObjectMapper();
	private static Logger log = Logger.getLogger(JsonStringBuilder.class);

	/**
	 * Main method used to print the marshalled objects
	 * @param args
	 */
	public static void main(String[] args) {
		BloodPressureMeasure m = new BloodPressureMeasure();
		log.debug(toString(m));
	}
	
	/**
	 * Converts the object into a JSON string
	 * @param o
	 * @return
	 */
	public static String toString(Object o){
		try {
			return mapper.writeValueAsString(o);
		} catch (IOException e) {
			log.error("unable to marshall object in JSON: "+ o, e);
			return null;
		}
	}
}
