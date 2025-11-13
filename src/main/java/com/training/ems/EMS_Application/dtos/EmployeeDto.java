package com.training.ems.EMS_Application.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    protected String emp_id;
    @NotNull(message = "First Name is required")
    protected String first_name;
    protected String last_name;
    @NotNull(message = "Email is reqired") //unique
    protected String email;
    @NotNull(message = "phone is requied") //unique
    protected String phone;
    protected String designation;
    protected LocalDate date_of_joining;
    protected String comp_id;
}
