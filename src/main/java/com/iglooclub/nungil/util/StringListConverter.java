package com.iglooclub.nungil.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iglooclub.nungil.exception.GeneralException;
import com.iglooclub.nungil.exception.GlobalErrorResult;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new GeneralException(GlobalErrorResult.JSON_PROCESSING_EXCEPTION, e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {};
        try {
            return mapper.readValue(dbData, typeReference);
        } catch (IOException e) {
            throw new GeneralException(GlobalErrorResult.IO_EXCEPTION, e);
        }
    }
}
