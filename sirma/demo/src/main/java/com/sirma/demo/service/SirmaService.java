package com.sirma.demo.service;

import com.sirma.demo.model.dto.EmployeeProjectsDTO;

import java.util.List;
import java.util.Map;

public interface SirmaService {
    Map<String, Integer> determinePairDays(List<EmployeeProjectsDTO> data);
}
