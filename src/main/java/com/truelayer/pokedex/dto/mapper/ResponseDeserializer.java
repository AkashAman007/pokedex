package com.truelayer.pokedex.dto.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ResponseDeserializer {
    public static <T> T deserialize(JsonNode data, Class<T> toValueType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(data, toValueType);
    }
}
