package com.truelayer.pokedex.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.truelayer.pokedex.dto.mapper.ResponseDeserializer;
import com.truelayer.pokedex.dto.model.TranslationDto;
import com.truelayer.pokedex.dto.request.TranslationRequest;
import com.truelayer.pokedex.exception.BusinessException;
import com.truelayer.pokedex.exception.ResourceNotFoundException;
import com.truelayer.pokedex.util.ErrorCodes;
import com.truelayer.pokedex.util.ErrorStrings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.truelayer.pokedex.util.Constants.FUN_TRANSLATION_URL;

@Slf4j
@Component
public class TranslationService {

    private final RestTemplate restTemplate;

    @Autowired
    public TranslationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("value=translationDto")
    public TranslationDto translate(String text, String language) throws IOException, ResourceNotFoundException, BusinessException {
        TranslationRequest translationRequest = TranslationRequest.builder().text(text).build();
        HttpEntity<TranslationRequest> httpEntity = new HttpEntity<>(translationRequest, new HttpHeaders());
        try {
            ResponseEntity<JsonNode> responseEntity = this.restTemplate.postForEntity(FUN_TRANSLATION_URL + language,
                    httpEntity, JsonNode.class);
            TranslationDto translationDto = ResponseDeserializer.deserialize(responseEntity.getBody(), TranslationDto.class);
            return translationDto;
        } catch (RestClientResponseException e) {
            log.error("Error {}",e.getMessage());
            if(e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new ResourceNotFoundException(language+" : Not Found");
            }
            throw new BusinessException(ErrorStrings.FUNAPI_COULD_NOT_PROCESS_REQUEST,
                    ErrorCodes.FUNAPI_COULD_NOT_PROCESS_REQUEST);
        }
    }
}
