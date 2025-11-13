package com.training.ems.EMS_Application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@EnableMongoAuditing -> check out this later
@Document(collection = "Employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    private String emp_id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String designation;
    private LocalDate date_of_joining;
    private LocalDateTime created_at; //ISO
    private LocalDateTime updated_at; //ISO
    @DBRef
    private Company company; // "company": { "$ref": "Company", "$id": "abc123" } this was stored like this so to but the actual field that we can access from outside was company_id
}

















// employee id
// employee first_name
// employee last_name
// employee email
// employee phone
// employee designation // backend, frontend, fullstack developers
// employee date_of_joining
// employee comany_id --> Referencing id of company
// record Created_at
// record Updated_at
