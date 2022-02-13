package com.truelayer.pokedex.pokemon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truelayer.pokedex.exception.BusinessException;
import com.truelayer.pokedex.exception.ResourceNotFoundException;
import com.truelayer.pokedex.service.PokeApiService;
import com.truelayer.pokedex.service.PokemonService;
import com.truelayer.pokedex.service.TranslationService;
import com.truelayer.pokedex.util.Constants;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FunTranslationTest {

    private final String API_URL = "/pokemon/translated/";
    private final String PIKACHU = "pikachu";
    private final String MEWTWO_DESC_YODA = "Created by a scientist after years of horrific gene splicing and dna engineering experiments,  it was.";
    private final String PIKACHU_DESC_SHAKESPEARE = "At which hour several of these pokémon gather,  their electricity couldst buildeth and cause lightning storms.";


    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    @Spy
    private TranslationService translationService;

    @InjectMocks
    @Spy
    private PokeApiService pokeApiService;

    @Autowired
    private PokemonService pokemonService;

    private ResponseEntity<JsonNode> getApiResponse(String pokemon) {
        String uri = API_URL + pokemon;
        HttpEntity entity = new HttpEntity(new HttpHeaders());
        ResponseEntity<JsonNode> responseEntity = this.testRestTemplate.exchange(uri, HttpMethod.GET,
                entity, JsonNode.class);
        return responseEntity;
    }

    @Test
    public void testGetSuccessfulResponse() throws IOException, BusinessException, ResourceNotFoundException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockPikachuResponse());
        Mockito.when(restTemplate.postForEntity(anyString(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulYodaTranslation());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        JsonNode pokemonResponse = response.getBody();
        Assert.assertNotNull(pokemonResponse);
    }

    @Test
    public void testLanguageSelectionForLegendaryPokemon() throws IOException {
        boolean isLegendary = true;
        String language = pokemonService.getTranslationLanguageForPokemonType("forest", isLegendary);
        Assert.assertEquals(Constants.YODA_LANGUAGE_IDENTIFIER, language);
    }

    @Test
    public void testLanguageSelectionForCaveHabitatButNonLegendaryPokemon() {
        boolean isLegendary = false;
        String language = pokemonService.getTranslationLanguageForPokemonType("cave", isLegendary);
        Assert.assertEquals(Constants.YODA_LANGUAGE_IDENTIFIER, language);
    }

    @Test
    public void testLanguageSelectionForCaveHabitatAndLegendaryPokemon() {
        boolean isLegendary = true;
        String language = pokemonService.getTranslationLanguageForPokemonType("cave", isLegendary);
        Assert.assertEquals(Constants.YODA_LANGUAGE_IDENTIFIER, language);
    }

    @Test
    public void testLanguageSelectionForNonCaveNonLegendaryPokemon() {
        boolean isLegendary = false;
        String language = pokemonService.getTranslationLanguageForPokemonType("forest", isLegendary);
        Assert.assertEquals(Constants.SHAKESPEARE_LANGUAGE_IDENTIFIER, language);
    }

    @Test
    public void testYodaTranslationForLegendaryPokemon() throws IOException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockMewtwoResponse());
        Mockito.when(restTemplate.postForEntity(anyString(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulYodaTranslation());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        JsonNode pokemonResponse = response.getBody();
        Assert.assertNotNull(pokemonResponse);
        Assert.assertEquals(MEWTWO_DESC_YODA, pokemonResponse.get("description").asText());
    }

    @Test
    public void testShakespeareTranslationForLegendaryPokemon() throws IOException {
        Mockito.when(restTemplate.exchange(any(), any(), any(), eq(JsonNode.class)))
                .thenReturn(mockPikachuResponse());
        Mockito.when(restTemplate.postForEntity(anyString(), any(), eq(JsonNode.class)))
                .thenReturn(mockSuccessfulShakespeareTranslation());
        ResponseEntity<JsonNode> response = getApiResponse(PIKACHU);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        JsonNode pokemonResponse = response.getBody();
        Assert.assertNotNull(pokemonResponse);
        Assert.assertEquals(PIKACHU_DESC_SHAKESPEARE, pokemonResponse.get("description").asText());
    }

    public ResponseEntity<JsonNode> mockPikachuResponse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = JsonNode.class.getResourceAsStream("/PikachuDataFromPokeAPI.json");
        JsonNode pokemonData = mapper.readValue(is, JsonNode.class);
        return ResponseEntity.status(HttpStatus.OK).body(pokemonData);
    }

    public ResponseEntity<JsonNode> mockMewtwoResponse() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = JsonNode.class.getResourceAsStream("/MewtwoDataFromPokeAPI.json");
        JsonNode pokemonData = mapper.readValue(is, JsonNode.class);
        return ResponseEntity.status(HttpStatus.OK).body(pokemonData);
    }

    public ResponseEntity<JsonNode> mockSuccessfulShakespeareTranslation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = JsonNode.class.getResourceAsStream("/PikachuDescriptionShakespeareTranslationFromFunAPI.json");
        JsonNode translationData = mapper.readValue(is, JsonNode.class);
        return ResponseEntity.status(HttpStatus.OK).body(translationData);
    }

    public ResponseEntity<JsonNode> mockSuccessfulYodaTranslation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = JsonNode.class.getResourceAsStream("/MewtwoDescriptionYodaTranslationFromFunAPI.json");
        JsonNode translationData = mapper.readValue(is, JsonNode.class);
        return ResponseEntity.status(HttpStatus.OK).body(translationData);
    }
}
