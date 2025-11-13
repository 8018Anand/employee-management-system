package com.training.ems.EMS_Application.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedCompanyListResponse extends CompanyListResponse{
    private int currentPage;
    private int pageSize;
    private int totalPages;
}
