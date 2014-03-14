package org.openmhealth.dpu.controller;

import static org.junit.Assert.*;
import lombok.Setter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for the DataProcessUnit Controller
 * 
 * @author Alberto Lagna
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-servlet.xml"})
public class DataProcessUnitControllerTest {

	@Setter @Autowired
	private DataProcessUnitController controller;
	
	/**
	 * Tests the process method with the right input
	 */
	@Test
	public void testProcessOK() {
		assertTrue(controller.process("bloodPressure", "{\"systolic\":100, \"diastolic\":99}").
			equals("{\"category\":\"high_blood_pressure_stage1\"}"));
	}
	
	/**
	 * Tests the process method with a not well formed JSON
	 */
	@Test
	public void testProcessJSONNotWellFormed() {
		assertTrue(controller.process("bloodPressure", "{\"systolic\":100, \"diastolic\":99").
			contains("json not well formed"));
	}
	
	/**
	 * Tests the process method a JSON that cannot be unmarshalled into domain classes
	 */
	@Test
	public void testProcessJSONWrongMapping() {
		assertTrue(controller.process("bloodPressure", "{\"systoli\":100, \"diastolic\":99}").
			contains("json mapping error"));
	}


}
