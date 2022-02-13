package com.truelayer.pokedex.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.truelayer.pokedex.dto.mapper.ResponseDeserializer;
import com.truelayer.pokedex.dto.model.PokeApiDto;
import com.truelayer.pokedex.exception.BusinessException;
import com.truelayer.pokedex.exception.ResourceNotFoundException;
import com.truelayer.pokedex.util.ErrorCodes;
import com.truelayer.pokedex.util.ErrorStrings;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import static com.truelayer.pokedex.util.Constants.POKE_API_SPECIES_URL;


@Component
@Slf4j
public class PokeApiService {

    private RestTemplate restTemplate;

    @Autowired
    public PokeApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PokeApiDto getPokemon(String pokemonName) throws IOException, ResourceNotFoundException, BusinessException {
        URI uri = preparePokemonRequestURI(pokemonName);
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
        try {
            ResponseEntity<JsonNode> response = this.restTemplate.exchange(uri, HttpMethod.GET,
                    entity, JsonNode.class);
            return ResponseDeserializer.deserialize(response.getBody(), PokeApiDto.class);
        } catch (RestClientResponseException e) {
            log.error("Error {}",e.getMessage());
            if(e.getRawStatusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new ResourceNotFoundException(pokemonName+" : Not Found");
            }
            throw new BusinessException(ErrorStrings.POKEAPI_COULD_NOT_PROCESS_REQUEST,
                    ErrorCodes.POKEAPI_COULD_NOT_PROCESS_REQUEST);
        }
    }

    private URI preparePokemonRequestURI(String pokemonName) {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("name", pokemonName);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(POKE_API_SPECIES_URL);
        return builder.buildAndExpand(urlParams).toUri();
    }
}
