package com.training.ems.EMS_Application.dtos.request;

import com.training.ems.EMS_Application.dtos.EmployeeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest extends Request{
    @NotNull(message = "To create/edit Data was must")
    @Valid
    protected EmployeeDto employee;
}
