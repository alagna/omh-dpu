package org.openmhealth.dpu.process;

import org.openmhealth.dpu.domain.Person;

public interface PersonService {
	public Person getRandom();
	public Person getById(Long id);
	public void save(Person person);
}
