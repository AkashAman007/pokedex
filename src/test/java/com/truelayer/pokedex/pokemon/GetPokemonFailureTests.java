package com.truelayer.pokedex.pokemon;

import com.fasterxml.jackson.databind.JsonNode;
import com.truelayer.pokedex.exception.BusinessException;
import com.truelayer.pokedex.exception.ResourceNotFoundException;
import com.truelayer.pokedex.service.PokeApiService;
import com.truelayer.pokedex.service.PokemonService;
import com.truelayer.pokedex.util.ErrorCodes;
import com.truelayer.pokedex.util.ErrorStrings;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetPokemonFailureTests {


    private final String API_URL = "/pokemon/";
    private final String UNKNOWN_POKEMON = "gizmoland";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private PokeApiService pokeApiService;

    @InjectMocks
    @Spy
    private PokemonService pokemonService;

    private ResponseEntity<JsonNode>  getApiResponse(String pokemon) {
        String uri = API_URL + pokemon;
        HttpEntity entity = new HttpEntity(new HttpHeaders());
        ResponseEntity<JsonNode> responseEntity = this.testRestTemplate.exchange(uri, HttpMethod.GET,
                entity, JsonNode.class);
        return responseEntity;
    }

    @Test
    public void testPokemonStatusCodeIfPokemonNotFound() throws BusinessException, IOException, ResourceNotFoundException {
        Mockito.when(pokeApiService.getPokemon(any()))
                .thenThrow(new ResourceNotFoundException(UNKNOWN_POKEMON+" : Not Found"));
        ResponseEntity<JsonNode> response = getApiResponse(UNKNOWN_POKEMON);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testPokemonErrorMessageIfPokemonNotFound() throws BusinessException, IOException, ResourceNotFoundException {
        Mockito.when(pokeApiService.getPokemon(any()))
                .thenThrow(new ResourceNotFoundException(UNKNOWN_POKEMON+" : Not Found"));
        ResponseEntity<JsonNode> response = getApiResponse(UNKNOWN_POKEMON);
        Assert.assertEquals(UNKNOWN_POKEMON+" : Not Found", Objects.requireNonNull(response.getBody()).get("error").asText());
    }

    @Test
    public void testPokemonStatusCodeIfBusinessExceptionIsThrown() throws BusinessException, IOException, ResourceNotFoundException {
        Mockito.when(pokeApiService.getPokemon(any()))
                .thenThrow(new BusinessException(ErrorStrings.POKEAPI_COULD_NOT_PROCESS_REQUEST,
                        ErrorCodes.POKEAPI_COULD_NOT_PROCESS_REQUEST));
        ResponseEntity<JsonNode> response = getApiResponse(UNKNOWN_POKEMON);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
