package com.training.ems.EMS_Application.controller;

import com.training.ems.EMS_Application.dtos.response.*;
import com.training.ems.EMS_Application.exception.CompanyNotFoundException;
import com.training.ems.EMS_Application.exception.EmployeeNotFoundException;
import com.training.ems.EMS_Application.dtos.request.EmployeeRequest;
import com.training.ems.EMS_Application.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ems/v1/company/employee")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add-employee")
    public EmployeeResponse addEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) throws  CompanyNotFoundException {
        return employeeService.saveEmployee(employeeRequest);
    }

    @GetMapping("/all-employees/{comp_id}")
    public EmployeeListResponse getEmployees(@PathVariable String comp_id) throws CompanyNotFoundException {
        return employeeService.allEmployeesByCompId(comp_id);
    }

    @GetMapping("/get-employee/{emp_id}")
    public EmployeeResponse getEmployee(@PathVariable String emp_id) throws EmployeeNotFoundException {
        return employeeService.getEmployeeById(emp_id);
    }

    @DeleteMapping("/delete-employee/{emp_id}")
    public Response deleteEmployee(@PathVariable String emp_id) throws  EmployeeNotFoundException{
        return employeeService.deleteEmployee(emp_id);
    }

    @PutMapping("/edit-employee/{emp_id}")
    public EmployeeResponse editEmployee(@Valid @PathVariable String emp_id, @RequestBody EmployeeRequest employeeRequest) throws EmployeeNotFoundException, CompanyNotFoundException {
        return employeeService.editEmployee(emp_id, employeeRequest);
    }

    @PatchMapping("/optEdit-employee/{emp_id}")
    public EmployeeResponse optionalEditEmployee(@Valid @PathVariable String emp_id, @RequestBody EmployeeRequest employeeRequest) throws EmployeeNotFoundException, CompanyNotFoundException {
//        System.out.println(emp_id);
        return employeeService.optionalEditEmployee(emp_id,  employeeRequest);
    }

    @GetMapping("/get-employees")
    public PaginatedEmployeeListResponse getCompany(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction, String emp_id
    ) {
        return employeeService.getPaginatedEmployees(page, size, sortBy, direction, emp_id);
    }
}
