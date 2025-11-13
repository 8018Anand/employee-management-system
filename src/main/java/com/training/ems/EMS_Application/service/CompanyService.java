package com.training.ems.EMS_Application.service;

import com.training.ems.EMS_Application.dtos.CompanyDto;
import com.training.ems.EMS_Application.dtos.response.*;
import com.training.ems.EMS_Application.entity.Company;
import com.training.ems.EMS_Application.exception.CompanyNotFoundException;
import com.training.ems.EMS_Application.exception.ConflictWhileDeletingCompany;
import com.training.ems.EMS_Application.repository.CompanyRepository;
import com.training.ems.EMS_Application.repository.EmployeeRepository;
import com.training.ems.EMS_Application.dtos.request.CompanyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    //saves the new employee to the database....
    public CompanyResponse SaveCompany(CompanyRequest companyRequest) {
        CompanyResponse companyResponse = new CompanyResponse();
        CompanyDto savedCompanyDto = companyToCompanyDto(companyRepository.save(companyDtoToCompany(companyRequest.getCompany())));
        StatusResponse statusResponse = new StatusResponse(200, "Company Added Successfully");
        companyResponse.setStatusResponse(statusResponse);
        companyResponse.setCompany(savedCompanyDto);
        return companyResponse;
    }

    //delete employee with id....
    public Response removeCompany(String id) throws CompanyNotFoundException, ConflictWhileDeletingCompany {
        Response response = new Response();
        if (!companyRepository.existsById(id)) {
            throw new CompanyNotFoundException("Company not found");
        }
        if (employeeRepository.existsByCompany_compId(id)) {
            throw new ConflictWhileDeletingCompany("Cannot delete company: employees are still associated");
        }

        companyRepository.deleteById(id);
        StatusResponse statusResponse = new StatusResponse(200, "Company Removed Successfully");
        response.setStatusResponse(statusResponse);
        return response;
    }


    //Edit employee by id --> Put => All the editable fields must be sent in the request....
    public CompanyResponse editCompany(CompanyRequest companyRequest, String id) throws CompanyNotFoundException {
        CompanyResponse companyResponse = new CompanyResponse();
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("Company Not found"));
        company.setName(companyRequest.getCompany().getName());
        company.setDescription(companyRequest.getCompany().getDescription());
        company.setHeadquaters(companyRequest.getCompany().getHeadquaters());
        company.setWebsite_url(companyRequest.getCompany().getWebsite_url());
        company.setFound_year(companyRequest.getCompany().getFound_year());
        company.setUpdated_at(LocalDateTime.now());
        companyRepository.save(company);
        StatusResponse statusResponse = new StatusResponse(200, "Company Edited Successfully");
        companyResponse.setStatusResponse(statusResponse);
        CompanyDto companyDtoToCompany = companyToCompanyDto(company);
        companyResponse.setCompany(companyDtoToCompany);
        return companyResponse;
    }

    //Edit employee by id --> Patch => no need to send all the fields....
    public CompanyResponse optionalEditCompany(CompanyRequest companyRequest, String id)
            throws CompanyNotFoundException {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company Not found"));

        CompanyDto dto = companyRequest.getCompany();

        // 🔄 Apply only non-null, non-empty fields
        applyCompanyPatch(company, dto);

        // 💾 Save updated company
        Company updatedCompany = companyRepository.save(company);

        // 📦 Build response
        CompanyResponse response = new CompanyResponse();
        response.setCompany(companyToCompanyDto(updatedCompany));
        response.setStatusResponse(new StatusResponse(200, "Company Edited Successfully"));

        return response;
    }

    private void applyCompanyPatch(Company company, CompanyDto dto) {
        if (StringUtils.hasText(dto.getName())) {
            company.setName(dto.getName());
        }
        if (StringUtils.hasText(dto.getDescription())) {
            company.setDescription(dto.getDescription());
        }
        if (StringUtils.hasText(dto.getHeadquaters())) {
            company.setHeadquaters(dto.getHeadquaters());
        }
        if (StringUtils.hasText(dto.getWebsite_url())) {
            company.setWebsite_url(dto.getWebsite_url());
        }
        if (dto.getFound_year() != null) {
            company.setFound_year(dto.getFound_year());
        }
    }

    public CompanyListResponse allCompanies() {
        CompanyListResponse companyListResponse = new CompanyListResponse();
        List<CompanyDto> companies = companyRepository.findAll()
                .stream()
                .map((this::companyToCompanyDto))
                .toList();
        StatusResponse statusResponse = new StatusResponse(200, "Companies fetched Successfully");
        companyListResponse.setStatusResponse(statusResponse);
        companyListResponse.setCompanies(companies);
        return companyListResponse;
    }

    public CompanyResponse fetchCompany(String id) throws CompanyNotFoundException {
        CompanyResponse companyResponse = new CompanyResponse();
        Optional<Company> companyOptional = companyRepository.findById(id);
        if(companyOptional.isEmpty()) {
            throw new CompanyNotFoundException("company not found");
        }
        StatusResponse statusResponse = new StatusResponse(200, "Company Fetched Successfully");
        companyResponse.setStatusResponse(statusResponse);
        companyResponse.setCompany(companyToCompanyDto(companyOptional.get()));
        return companyResponse;
    }

    public PaginatedCompanyListResponse getPaginatedCompanies(
            int page, int size,
            String sortBy, String direction,
            String name, String location
    ) {
        Query query = new Query();

        List<Criteria> criteriaList = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            criteriaList.add(Criteria.where("name").regex(".*" + name + ".*", "i"));
        }

        if (location != null && !location.isEmpty()) {
            criteriaList.add(Criteria.where("location").regex(".*" + location + ".*", "i"));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        if (sortBy != null && !sortBy.isEmpty() && direction != null && !direction.isEmpty()) {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            query.with(Sort.by(sortDirection, sortBy));
        }

        query.skip((long)page * size).limit(size);

        List<CompanyDto> companies = mongoTemplate.find(query, Company.class)
                .stream()
                .map((this::companyToCompanyDto))
                .toList();



        long totalItems = mongoTemplate.count(new Query(), Company.class);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        PaginatedCompanyListResponse pageData = new PaginatedCompanyListResponse();
        pageData.setStatusResponse(new StatusResponse(200, "Paginated Company List fetched Successfully"));
        pageData.setCompanies(companies);
        pageData.setCurrentPage(page);
        pageData.setPageSize(size);
        pageData.setTotalPages(totalPages);


        return pageData;
    }





    // Helper function to convert company to companyDto....
    private CompanyDto companyToCompanyDto(Company savedCompany) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setComp_id(savedCompany.getCompId());
        companyDto.setName(savedCompany.getName());
        companyDto.setDescription(savedCompany.getDescription());
        companyDto.setHeadquaters(savedCompany.getHeadquaters());
        companyDto.setWebsite_url(savedCompany.getWebsite_url());
        companyDto.setFound_year(savedCompany.getFound_year());
        return companyDto;
    }

    //Helper function to convert companyDto to company....
    private Company companyDtoToCompany(CompanyDto companyDto) {
        Company company = new Company();
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setHeadquaters(companyDto.getHeadquaters());
        company.setFound_year(companyDto.getFound_year());
        company.setWebsite_url(companyDto.getWebsite_url());
        company.setCreated_at(LocalDateTime.now());
        company.setUpdated_at(LocalDateTime.now());
        return company;
    }

}
