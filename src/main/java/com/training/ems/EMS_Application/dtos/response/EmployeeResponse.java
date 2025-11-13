package com.training.ems.EMS_Application.dtos.response;

import com.training.ems.EMS_Application.dtos.EmployeeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse extends Response{
    public EmployeeDto employee;
}
