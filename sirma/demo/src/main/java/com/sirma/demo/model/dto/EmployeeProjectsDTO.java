package com.sirma.demo.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.pojava.datetime.DateTime;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeProjectsDTO {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String NULL = "NULL";

    @JsonProperty(value = "EmpID", index = 0)
    private Long employeeId;

    @JsonProperty(value = "ProjectID", index = 1)
    private Long projectId;

    @JsonProperty(value = "DateFrom", index = 2)
    private LocalDate dateFrom;

    @JsonProperty(value = "DateTo", index = 3)
    private LocalDate dateTo;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = parseStringToLocalDate(dateFrom);
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = parseStringToLocalDate(dateTo);
    }

    private LocalDate parseStringToLocalDate(String date) {
        LocalDate retVal;
        if (NULL.equals(date)) {
            retVal = LocalDate.now();
        } else {
            try {
                retVal = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                // try to parse date of ANY kind of string; Note it will produce unexpected results if the date format
                // is different than one expected of the locale, as it relies on locale to figure out how to parse
                // ambiguous dates such as 01-01-01
                retVal = DateTime.parse(date).toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        }
        return retVal;
    }
}
