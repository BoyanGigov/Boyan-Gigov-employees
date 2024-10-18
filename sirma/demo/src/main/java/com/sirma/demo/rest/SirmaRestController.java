package com.sirma.demo.rest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.sirma.demo.converter.SirmaCsvConverter;
import com.sirma.demo.model.dto.EmployeeProjectsDTO;
import com.sirma.demo.service.SirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("v1/sirma/")
public class SirmaRestController {

    private static final String TEXT_CSV = "text/csv"; // org.springframework.http.MediaType for text/csv doesn't exist

    private SirmaService sirmaService;
    private SirmaCsvConverter sirmaCsvConverter;

    @Autowired
    public SirmaRestController(SirmaService sirmaService, SirmaCsvConverter sirmaCsvConverter) {
        this.sirmaService = sirmaService;
        this.sirmaCsvConverter = sirmaCsvConverter;
    }

    @PostMapping("uploadCSV")
    public ResponseEntity<String> uploadCSV(@RequestPart("file") MultipartFile uploadedFile) throws Exception {
        if (!TEXT_CSV.equals(uploadedFile.getContentType())) {
            return ResponseEntity.badRequest().body("The expected file format is csv");
        }

        List<EmployeeProjectsDTO> data;
        try {
            data = sirmaCsvConverter.convertMultipartCsvToList(uploadedFile, EmployeeProjectsDTO.class);
        } catch (JsonMappingException e) {
            System.out.println("Exception during parsing: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to parse the csv file: " + e.getMessage().substring(0, e.getMessage().indexOf(" at ")));
        } catch (Exception e) {
            System.out.println("Exception during parsing: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to parse the csv file");
        }
        Map<String, Integer> pairDaysMap;

        try {
            pairDaysMap = sirmaService.determinePairDays(data);
        } catch (Exception e) {
            System.out.println("Exception during data comparison:" + e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to compare the data");
        }
        Optional<Map.Entry<String, Integer>> result = pairDaysMap.entrySet().stream().max(Map.Entry.comparingByValue());
        if (!result.isPresent()) {
            return ResponseEntity.ok("None of the provided employees have worked on the same project at the same time");
        }
        Map.Entry<String, Integer> finalEntry = result.get();
        return ResponseEntity.ok("Employee pair " + finalEntry.getKey() + " has worked the most together, " + finalEntry.getValue() + " days");
    }

}
