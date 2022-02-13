package com.truelayer.pokedex.pokemon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truelayer.pokedex.exception.BusinessException;
import com.truelayer.pokedex.exception.ResourceNotFoundException;
import com.truelayer.pokedex.service.PokeApiService;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetPokemonInformationTest {

    private final String API_URL = "/pokemon/";
    private final String PIKACHU = "pikachu";
    private final String PIKACHU_HABITAT = "forest";
    private final boolean PIKACHU_LEGENDARY_STATUS = false;
    private final String PIKACHU_DESCRIPTION = "When several of these POKéMON gather, their electricity could build and cause lightning storms.";
    private final String UNKNOWN_POKEMON = "gizmoland";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    @Spy
    private PokeApiService pokeApiService;


    private ResponseEntity<JsonNode>  getApiResponse(String pokemon) {
        String uri = API_URL + pokemon;
        HttpEntity entity = new HttpEntity(new HttpHeaders());
        ResponseEntity<JsonNode> responseEntity = this.testRestTemplate.exchange(uri, HttpMethod.GET,
                entity, JsonNode.class);
        return responseEntity;
    }

    @Test
    public void testGetSuccessfulResponse() throws IOException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulPokeApiResponse());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        JsonNode pokemonResponse = response.getBody();
        Assert.assertNotNull(pokemonResponse);

    }

    @Test
    public void testPokemonName() throws IOException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulPokeApiResponse());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        JsonNode pokemon = response.getBody();
        Assert.assertEquals(PIKACHU, pokemon.get("name").asText());
    }

    @Test
    public void testPokemonHabitat() throws IOException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulPokeApiResponse());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        JsonNode pokemon = response.getBody();
        Assert.assertEquals(PIKACHU_HABITAT, pokemon.get("habitat").asText());
    }

    @Test
    public void testPokemonLegendaryStatus() throws IOException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulPokeApiResponse());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        JsonNode pokemon = response.getBody();
        Assert.assertEquals(PIKACHU_LEGENDARY_STATUS, pokemon.get("isLegendary").asBoolean());
    }

    @Test
    public void testPokemonDescriptionIsFirstEnglishDescription() throws IOException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulPokeApiResponse());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        JsonNode pokemon = response.getBody();
        Assert.assertEquals(PIKACHU_DESCRIPTION, pokemon.get("description").asText());
    }

    @Test
    public void testPokemonDescriptionIfNoEnglishDescriptionIsPresent() throws IOException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulPokeApiResponseWithNoEnglishDescription());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        JsonNode pokemon = response.getBody();
        Assert.assertEquals("", pokemon.get("description").asText());
    }

    @Test
    public void testPokemonNotFound() {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenThrow(new RestClientException(UNKNOWN_POKEMON+" : Not Found"));
        ResponseEntity<JsonNode> response = getApiResponse(UNKNOWN_POKEMON);
        Assert.assertNotEquals(HttpStatus.OK, response.getStatusCode());
    }


    public ResponseEntity<JsonNode> mockSuccessfulPokeApiResponse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = JsonNode.class.getResourceAsStream("/PikachuDataFromPokeAPI.json");
        JsonNode pokemonData = mapper.readValue(is, JsonNode.class);
        return ResponseEntity.status(HttpStatus.OK).body(pokemonData);
    }

    public ResponseEntity<JsonNode> mockSuccessfulPokeApiResponseWithNoEnglishDescription() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = JsonNode.class.getResourceAsStream("/PikachuDataWithNoEnglishDescription.json");
        JsonNode pokemonData = mapper.readValue(is, JsonNode.class);
        return ResponseEntity.status(HttpStatus.OK).body(pokemonData);
    }

}
