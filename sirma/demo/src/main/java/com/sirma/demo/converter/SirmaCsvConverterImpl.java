package com.sirma.demo.converter;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class SirmaCsvConverterImpl implements SirmaCsvConverter {

    @Override
    public List convertMultipartCsvToList(MultipartFile file, Class clazz) throws IOException {
        CsvMapper jacksonCsvMapper = new CsvMapper();
        jacksonCsvMapper.registerModule(new JavaTimeModule());
        CsvSchema jacksonCsvSchema = jacksonCsvMapper.typedSchemaFor(clazz).withHeader().withColumnSeparator(',');
        MappingIterator<Object> mapIter = jacksonCsvMapper
                .readerWithTypedSchemaFor(clazz)
                .with(jacksonCsvSchema)
                .readValues(new String(file.getBytes(), StandardCharsets.UTF_8));
        return mapIter.readAll();
    }
}
