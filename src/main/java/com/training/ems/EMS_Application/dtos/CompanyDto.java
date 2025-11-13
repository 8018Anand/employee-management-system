package com.training.ems.EMS_Application.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.jsr310.LocalDateTimeCodec;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private String comp_id;

    @NotNull(message = "Company name must not be null")
    private String name; // unique

    private String description;

    @Min(value = 1900, message = "Found year must be no earlier than 1900")
    @Max(value = 2025, message = "Found year must be no later than the current year")
    private Integer found_year;

    private String headquaters;

    private String website_url; //unique..

}
