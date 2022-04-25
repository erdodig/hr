package hu.webuni.hr.dodi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.dodi.model.Education;

public interface EducationRepository extends JpaRepository<Education, Long> {

	Education findByName(String name);
}
