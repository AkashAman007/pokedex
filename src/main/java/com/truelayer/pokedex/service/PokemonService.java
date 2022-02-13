package com.truelayer.pokedex.service;

import com.truelayer.pokedex.dto.mapper.PokemonMapper;
import com.truelayer.pokedex.dto.model.FlavorTextEntry;
import com.truelayer.pokedex.dto.model.PokeApiDto;
import com.truelayer.pokedex.dto.response.PokemonResponse;
import com.truelayer.pokedex.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.truelayer.pokedex.util.Constants.ENGLISH_LANGUAGE_IDENTIFIER;

@Component
public class PokemonService {

    private PokeApiService pokeApiService;

    @Autowired
    public PokemonService(PokeApiService pokeApiService) {
        this.pokeApiService = pokeApiService;
    }

    public PokemonResponse getPokemonByName(String pokemonName) throws IOException {
        PokeApiDto pokeApiDto = this.pokeApiService.getPokemon(pokemonName);
        String pokemonDescription = this.getPokemonDescription(pokeApiDto ,ENGLISH_LANGUAGE_IDENTIFIER);
        return PokemonMapper.toPokemonResponse(pokeApiDto, pokemonDescription);
    }

    private String getPokemonDescription(PokeApiDto pokeApiDto, String language) {
        Optional<FlavorTextEntry> flavorTextEntryWithEnglishDescription = pokeApiDto.getFlavorTextEntries()
                .stream()
                .filter(flavorTextEntry -> flavorTextEntry.getLanguage().getName().equals(language))
                .findFirst();
        String description = flavorTextEntryWithEnglishDescription.isPresent() ? flavorTextEntryWithEnglishDescription.get().getFlavorText() : "";
        description = Util.removeEscapeSequence(description);
        return description;
    }
}
