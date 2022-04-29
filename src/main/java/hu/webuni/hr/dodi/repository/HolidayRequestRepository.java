package hu.webuni.hr.dodi.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.dodi.model.HolidayRequest;

public interface HolidayRequestRepository extends JpaRepository<HolidayRequest, Long>, JpaSpecificationExecutor<HolidayRequest> {

//	@EntityGraph(value = "employee.position.employees")
	@Query("SELECT h FROM HolidayRequest h WHERE h.id = :id")
	HolidayRequest getHolidayRequestAndPositionWithEmployees(long id);

}
