package hu.webuni.hr.dodi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.dodi.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

	Position findByJobTitle(String jobTitle);
}
