package com.training.ems.EMS_Application.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

//@EnableMongoAuditing -> check out this later
@Document(collection = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    private String compId;
    private String name;
    private String description;
    private Integer found_year;
    private String headquaters;
    private String website_url;
    private LocalDateTime created_at; //ISo
    private LocalDateTime updated_at; //ISO
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

