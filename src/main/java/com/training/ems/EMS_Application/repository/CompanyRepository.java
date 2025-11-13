package com.training.ems.EMS_Application.repository;

import com.training.ems.EMS_Application.entity.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends MongoRepository<Company,String> {

}
