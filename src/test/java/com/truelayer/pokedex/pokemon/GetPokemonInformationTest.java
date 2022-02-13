package com.truelayer.pokedex.pokemon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truelayer.pokedex.dto.response.PokemonResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetPokemonInformationTest {

    private final String API_URL = "/pokemon/";
    private final String PIKACHU = "pikachu";

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<JsonNode>  getApiResponse(String pokemon) {
        String uri = API_URL + pokemon;
        HttpEntity entity = new HttpEntity(new HttpHeaders());
        ResponseEntity<JsonNode> responseEntity = this.restTemplate.exchange(uri, HttpMethod.GET,
                entity, JsonNode.class);
        return responseEntity;
    }

    @Test
    public void testGetSuccessfulResponse() {

        ResponseEntity<JsonNode> responseEntity = getApiResponse(PIKACHU);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JsonNode pokemonResponse = responseEntity.getBody();
        Assert.assertNotNull(pokemonResponse);

    }

}
