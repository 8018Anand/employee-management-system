package com.training.ems.EMS_Application.repository;

import com.training.ems.EMS_Application.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee,String> {

    boolean existsByCompany_compId(String id);

    List<Employee> findByCompany_compId(String companyId);


}
