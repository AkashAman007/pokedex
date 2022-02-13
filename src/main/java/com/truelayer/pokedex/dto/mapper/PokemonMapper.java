package com.truelayer.pokedex.dto.mapper;

import com.truelayer.pokedex.dto.model.PokeApiDto;
import com.truelayer.pokedex.dto.response.PokemonResponse;

public class PokemonMapper {
    public static PokemonResponse toPokemonResponse(PokeApiDto pokemonApiDto, String description) {
        return PokemonResponse.builder()
                .name(pokemonApiDto.getName())
                .habitat(pokemonApiDto.getHabitat().getName())
                .isLegendary(pokemonApiDto.getIsLegendary())
                .description(description)
                .build();
    }
}