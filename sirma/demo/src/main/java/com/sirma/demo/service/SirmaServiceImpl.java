package com.sirma.demo.service;


import com.sirma.demo.model.dto.EmployeeProjectsDTO;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SirmaServiceImpl implements SirmaService {

    /**
     * Uses the provided list to
     * @param data the input data
     * @return map with key of <smaller employeeId>,<larger employeeId> and value of the number of days
     * employees have worked together
     */
    @Override
    public Map<String, Integer> determinePairDays(List<EmployeeProjectsDTO> data) {
        Set<Long> projectIds = data.stream().map(EmployeeProjectsDTO::getProjectId).collect(Collectors.toSet());
        Map<String, Integer> pairDaysMap = new HashMap<>();

        projectIds.forEach(projectId -> {
            for (int firstEmpIndex = 0; firstEmpIndex < data.size(); firstEmpIndex++) {
                EmployeeProjectsDTO firstDto = data.get(firstEmpIndex);
                if (firstDto.getProjectId() == projectId) {
                    for (int secondEmpIndex = firstEmpIndex + 1; secondEmpIndex < data.size(); secondEmpIndex++) { // second index starts from first+1 because all previous are already evaluated
                        EmployeeProjectsDTO secondDto = data.get(secondEmpIndex);
                        if (secondDto.getProjectId() == projectId) {
                            addToPairDaysMap(pairDaysMap, firstDto, secondDto);
                        }
                    }
                }
            }
        });
        return pairDaysMap;
    }

    /**
     * Adds to the provided Map a key of <smaller employeeId>,<larger employeeId> and a value of the number of days
     * they have worked together. The order the employee DTOs are provided into the parameters does not matter.
     * @param pairDaysMap the map the result will be added to
     * @param firstDto one of the emlpoyee entries
     * @param secondDto the other of the employee entry
     */
    private void addToPairDaysMap(Map<String, Integer> pairDaysMap, EmployeeProjectsDTO firstDto, EmployeeProjectsDTO secondDto) {
        String key = new String(firstDto.getEmployeeId() < secondDto.getEmployeeId() ?
                firstDto.getEmployeeId() + "," + secondDto.getEmployeeId() :
                secondDto.getEmployeeId() + "," + firstDto.getEmployeeId());
        Integer value;
        if (secondDto.getDateTo() != null &&
                (firstDto.getDateFrom().isBefore(secondDto.getDateTo()) || firstDto.getDateFrom().isEqual(secondDto.getDateTo()))) { // check if they worked in same time period
            LocalDate earlierDate = firstDto.getDateFrom().isBefore(secondDto.getDateFrom()) ? secondDto.getDateFrom() : firstDto.getDateFrom();
            value = calculateDaysBetweenStartAndEndInclusive(earlierDate, secondDto.getDateTo());
        } else if (firstDto.getDateTo() != null &&
                (secondDto.getDateFrom().isBefore(firstDto.getDateTo()) || secondDto.getDateFrom().isEqual(firstDto.getDateTo()))) {
            LocalDate earlierDate = firstDto.getDateFrom().isBefore(secondDto.getDateFrom()) ? secondDto.getDateFrom() : firstDto.getDateFrom();
            value = calculateDaysBetweenStartAndEndInclusive(earlierDate, firstDto.getDateTo());
        } else {
            // the two employees didn't work in the same time period
            return;
        }
        Integer existingValue = pairDaysMap.get(key);
        if (existingValue == null) {
            pairDaysMap.put(key,value);
        } else {
            pairDaysMap.put(key, existingValue + value);
        }
    }

    /**
     * Order of dates matters, first param is the earlier date, second param is the later date
     * @param earlierDate
     * @param laterDate
     * @return the number of days between the earlier date at 00:00 and
     * the later date at 23:59, counting the partial day as an entire day
     */
    private int calculateDaysBetweenStartAndEndInclusive(LocalDate earlierDate, LocalDate laterDate) {
        int result = Math.toIntExact(Duration.between(earlierDate.atStartOfDay(), laterDate.atStartOfDay()).toDays());
        // +1 necessary because we want to measure from start of first day to end of last day
        result = result+1;
        return result;
    }
}
