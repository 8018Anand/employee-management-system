package com.training.ems.EMS_Application.dtos.request;
import com.training.ems.EMS_Application.dtos.CompanyDto;
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
public class CompanyRequest extends Request{
    @Valid
    @NotNull(message = "To create/edit company data was must")
    protected CompanyDto company;
}
