package com.training.ems.EMS_Application.dtos.response;

import com.training.ems.EMS_Application.dtos.CompanyDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CompanyListResponse extends Response {
    List<CompanyDto> companies;
}
