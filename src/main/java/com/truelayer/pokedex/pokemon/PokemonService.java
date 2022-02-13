package com.truelayer.pokedex.pokemon;

import com.truelayer.pokedex.dto.response.PokemonResponse;
import org.springframework.stereotype.Component;

@Component
public class PokemonService {
    public PokemonResponse getPokemonByName(String pokemonName)  {
        return PokemonResponse.builder()
                .name("pikachu")
                .habitat("forest")
                .isLegendary(false)
                .description("Electric type pokemon")
                .build();
    }
}
