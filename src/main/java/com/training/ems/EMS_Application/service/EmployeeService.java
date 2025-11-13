package com.training.ems.EMS_Application.service;

import com.training.ems.EMS_Application.dtos.CompanyDto;
import com.training.ems.EMS_Application.dtos.EmployeeDto;
import com.training.ems.EMS_Application.dtos.response.*;
import com.training.ems.EMS_Application.entity.Company;
import com.training.ems.EMS_Application.entity.Employee;
import com.training.ems.EMS_Application.exception.CompanyNotFoundException;
import com.training.ems.EMS_Application.exception.EmployeeNotFoundException;
import com.training.ems.EMS_Application.repository.CompanyRepository;
import com.training.ems.EMS_Application.repository.EmployeeRepository;
import com.training.ems.EMS_Application.dtos.request.EmployeeRequest;
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
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    //create
    //edit
    //delete

    public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest) throws  CompanyNotFoundException {
//        System.out.println(employeeRequest.getEmployee().getDesignation());
        Employee convertedEmployee = employeeDtoToEmployee(employeeRequest.getEmployee());
        Employee savedEmployee = employeeRepository.save(convertedEmployee);
        EmployeeDto employeedto = employeeToEmployeeDto(savedEmployee);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(employeedto);
        employeeResponse.setStatusResponse(new StatusResponse(200, "Employee saved successfully"));
        return employeeResponse;
    }

    public EmployeeListResponse allEmployeesByCompId(String companyId) throws CompanyNotFoundException {
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException("Company not found");
        }
        List<EmployeeDto> employees = employeeRepository.findByCompany_compId(companyId)
                .stream()
                .map((this::employeeToEmployeeDto))
                .toList();

        EmployeeListResponse employeeListResponse = new EmployeeListResponse();
        employeeListResponse.setEmployees(employees);
        employeeListResponse.setStatusResponse(new StatusResponse(200, "All employees fetched successfully"));
        return employeeListResponse;
    }

    public EmployeeResponse getEmployeeById(String empId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if(employee.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        EmployeeDto employeeDto = employeeToEmployeeDto(employee.get());
        EmployeeResponse empResponse = new EmployeeResponse();
        empResponse.setEmployee(employeeDto);
        empResponse.setStatusResponse(new StatusResponse(200, "Employee found successfully"));
        return empResponse;
    }


    public Response deleteEmployee(String emp_id) throws  EmployeeNotFoundException{
        if(!employeeRepository.existsById(emp_id)) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        employeeRepository.deleteById(emp_id);
        return new Response(new StatusResponse(200, "Employee deleted successfully"));
    }

    public EmployeeResponse editEmployee(String emp_id, EmployeeRequest employeeRequest)
            throws EmployeeNotFoundException, CompanyNotFoundException {

        EmployeeDto employeeDto = employeeRequest.getEmployee();
        String compId = employeeDto.getComp_id();

        // 🔍 Validate company existence
        if (!companyRepository.existsById(compId)) {
            throw new CompanyNotFoundException("Company not Found Exception");
        }

        // 🔍 Validate employee existence
        Employee employee = employeeRepository.findById(emp_id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        // 🔄 Replace all fields (PUT semantics)
        employee.setFirst_name(employeeDto.getFirst_name());
        employee.setLast_name(employeeDto.getLast_name());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhone(employeeDto.getPhone());
        employee.setDate_of_joining(employeeDto.getDate_of_joining());

        // 💾 Save updated employee
        Employee savedEmployee = employeeRepository.save(employee);

        // 📦 Build response
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(employeeToEmployeeDto(savedEmployee));
        employeeResponse.setStatusResponse(new StatusResponse(200, "Employee updated successfully"));

        return employeeResponse;
    }

    public EmployeeResponse optionalEditEmployee(String emp_id, EmployeeRequest employeeRequest) throws EmployeeNotFoundException, CompanyNotFoundException {
//        System.out.println("test" + employeeRequest.getEmployee().getComp_id());
        EmployeeDto employeeDto = employeeRequest.getEmployee();
        String compId = employeeDto.getComp_id();
        if(!companyRepository.existsById(compId)) {
            throw new CompanyNotFoundException("Company not Found");
        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(emp_id);
        if(optionalEmployee.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        Employee employee = optionalEmployee.get();
        applyPatch(employee, employeeDto);
        employeeRepository.save(employee);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(employeeToEmployeeDto(employee));
        employeeResponse.setStatusResponse(new StatusResponse(200, "Employee updated successfully"));
        return employeeResponse;
    }
    private void applyPatch(Employee employee, EmployeeDto dto) {
        if (StringUtils.hasText(dto.getFirst_name())) employee.setFirst_name(dto.getFirst_name());
        if (StringUtils.hasText(dto.getLast_name())) employee.setLast_name(dto.getLast_name());
        if (StringUtils.hasText(dto.getEmail())) employee.setEmail(dto.getEmail());
        if (StringUtils.hasText(dto.getPhone())) employee.setPhone(dto.getPhone());
        if(StringUtils.hasText(dto.getDesignation())) employee.setDesignation(dto.getDesignation());
        if (dto.getDate_of_joining() != null) employee.setDate_of_joining(dto.getDate_of_joining());
    }



    public PaginatedEmployeeListResponse getPaginatedEmployees(
            int page, int size,
            String sortBy, String direction,
            String emp_id
    ) {
        Query query = new Query();

        List<Criteria> criteriaList = new ArrayList<>();

        if (sortBy != null && !sortBy.isEmpty() && direction != null && !direction.isEmpty()) {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            query.with(Sort.by(sortDirection, sortBy));
        }

        query.skip((long)page * size).limit(size);


        List<EmployeeDto> employees = mongoTemplate.find(query, Employee.class)
                .stream()
                .map((this::employeeToEmployeeDto))
                .toList();



        long totalItems = mongoTemplate.count(new Query(), Employee.class);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        PaginatedEmployeeListResponse pageData = new PaginatedEmployeeListResponse();
        pageData.setStatusResponse(new StatusResponse(200, "Paginated Company List fetched Successfully"));
        pageData.setEmployees(employees);
        pageData.setCurrentPage(page);
        pageData.setPageSize(size);
        pageData.setTotalPages(totalPages);


        return pageData;
    }



    private EmployeeDto employeeToEmployeeDto(Employee savedEmployee) {
        return new EmployeeDto(savedEmployee.getEmp_id(), savedEmployee.getFirst_name(), savedEmployee.getLast_name(), savedEmployee.getEmail(), savedEmployee.getPhone(), savedEmployee.getDesignation(), savedEmployee.getDate_of_joining(), savedEmployee.getCompany().getCompId());
    }

    private Employee employeeDtoToEmployee(EmployeeDto employeeRequest) throws  CompanyNotFoundException {
        String company_id = employeeRequest.getComp_id();
        Optional<Company> optCompany = companyRepository.findById(company_id);
        if(optCompany.isEmpty()) {
            throw new  CompanyNotFoundException("Company not found");
        }
        Employee employee = new  Employee();
        employee.setFirst_name(employeeRequest.getFirst_name());
        employee.setLast_name(employeeRequest.getLast_name());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPhone(employeeRequest.getPhone());
        employee.setCompany(optCompany.get());
        employee.setCreated_at(LocalDateTime.now());
        employee.setUpdated_at(LocalDateTime.now());
        employee.setDate_of_joining(employeeRequest.getDate_of_joining());
        employee.setDesignation(employeeRequest.getDesignation());
        return employee;
    }
}




















