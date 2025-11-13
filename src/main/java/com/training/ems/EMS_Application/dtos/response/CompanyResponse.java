package com.training.ems.EMS_Application.dtos.response;

import com.training.ems.EMS_Application.dtos.CompanyDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse extends Response {
    protected CompanyDto company;
}