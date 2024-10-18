package com.sirma.demo.converter;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SirmaCsvConverter {
    List convertMultipartCsvToList(MultipartFile uploadedFile, Class clazz) throws IOException;
}
