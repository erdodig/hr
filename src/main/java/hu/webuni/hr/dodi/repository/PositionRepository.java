package hu.webuni.hr.dodi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.dodi.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

	public List<Position> findByName(String name);

	@EntityGraph(attributePaths = "employees")
	@Query("SELECT p From Position p WHERE id = :id")
	public Position findByIdWithAllEmployees(int id);

}
