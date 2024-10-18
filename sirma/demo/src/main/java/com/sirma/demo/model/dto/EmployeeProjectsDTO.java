package com.sirma.demo.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        if (NULL.equals(dateFrom)) {
            this.dateFrom = LocalDate.now();
        } else {
            this.dateFrom = LocalDate.parse(dateFrom, formatter);
        }
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
