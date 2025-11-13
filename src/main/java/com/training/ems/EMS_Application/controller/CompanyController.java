package com.training.ems.EMS_Application.controller;

import com.training.ems.EMS_Application.dtos.response.PaginatedCompanyListResponse;
import com.training.ems.EMS_Application.exception.CompanyNotFoundException;
import com.training.ems.EMS_Application.exception.ConflictWhileDeletingCompany;
import com.training.ems.EMS_Application.dtos.request.CompanyRequest;
import com.training.ems.EMS_Application.dtos.response.CompanyListResponse;
import com.training.ems.EMS_Application.dtos.response.CompanyResponse;
import com.training.ems.EMS_Application.dtos.response.Response;
import com.training.ems.EMS_Application.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ems/v1/company")
@CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {
    @Autowired
    private CompanyService companyService;


    //You need to add the validations...
    //you need to create the pagination
    //try to implement the function which will give outputs like page 0 to 10, 11 to 20, 21 to 30

    @PostMapping("/add-company")
    public CompanyResponse addCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        return companyService.SaveCompany(companyRequest);
    }

    @DeleteMapping("/delete-company/{id}")
    public Response deleteCompany(@PathVariable String id) throws CompanyNotFoundException, ConflictWhileDeletingCompany {
        return companyService.removeCompany(id);
    }

    @PutMapping("/update-company/{id}")
    public CompanyResponse updateCompany(@Valid @RequestBody CompanyRequest companyRequest, @PathVariable String id) throws CompanyNotFoundException {
        return companyService.editCompany(companyRequest, id);
    }


    @PatchMapping("/optionalUpdate-company/{id}")
    public CompanyResponse patch(@Valid @RequestBody CompanyRequest companyRequest, @PathVariable String id) throws CompanyNotFoundException{
        return companyService.optionalEditCompany(companyRequest, id);
    }

    @GetMapping("/get-company/{id}")
    public CompanyResponse getCompany(@PathVariable String id) throws CompanyNotFoundException {
        return companyService.fetchCompany(id);
    }

    @GetMapping("/get-companies")
    public PaginatedCompanyListResponse getCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location
    ) {
        return companyService.getPaginatedCompanies(page, size, sortBy, direction, name, location);
    }



    @GetMapping("/all-companies")
    public CompanyListResponse allCompanies() {
        return  companyService.allCompanies();
    }

}
