package org.openmhealth.dpu.process;

import org.openmhealth.dpu.process.exception.BusinessException;
import org.openmhealth.dpu.process.exception.SystemException;

/**
 * DPU common interface, it declares the methods that all the DPUs have to implement.
 * 
 * @author Alberto Lagna
 *
 */
public interface DataProcessUnit {
	
	public final String processName="DPU";
	
	/**
	 * This is the main worker call into a DPU. 
	 * Process receives data that belongs to one schema ID-version pair and returns data 
	 * that belongs to another schema ID-version pair. 
	 * 
	 * There are cases where the input and output schema ID-version pairs could be the same.
	 * 
	 * The process endpoint must include support for a parameter that instructs it to preserve 
	 * the original raw input data. 
	 * 
	 * @param jsonInput
	 * @return
	 * @throws SystemException 
	 * @throws BusinessException 
	 */
	public String process(String jsonInput, boolean preserveRawInputData) throws BusinessException, SystemException;
	
	/**
	 * Registry functions much like the DSU registry. 
	 * It returns the schema ID-version pairs that can be processed and the schema-ID 
	 * version pairs that are returned.
	 * @throws SystemException 
	 * 
	 */
	public String registryRead() throws SystemException;

}
