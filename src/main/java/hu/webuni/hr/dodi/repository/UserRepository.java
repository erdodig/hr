package hu.webuni.hr.dodi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.dodi.model.HrUser;

public interface UserRepository extends JpaRepository<HrUser, String> {

}
