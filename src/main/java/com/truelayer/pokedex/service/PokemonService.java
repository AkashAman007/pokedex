package com.truelayer.pokedex.service;

import com.truelayer.pokedex.dto.mapper.PokemonMapper;
import com.truelayer.pokedex.dto.model.FlavorTextEntry;
import com.truelayer.pokedex.dto.model.PokeApiDto;
import com.truelayer.pokedex.dto.model.TranslationDto;
import com.truelayer.pokedex.dto.response.PokemonResponse;
import com.truelayer.pokedex.exception.BusinessException;
import com.truelayer.pokedex.exception.ResourceNotFoundException;
import com.truelayer.pokedex.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.truelayer.pokedex.util.Constants.*;
import static com.truelayer.pokedex.util.Constants.SHAKESPEARE_LANGUAGE_IDENTIFIER;

@Slf4j
@Component
public class PokemonService {

    private final PokeApiService pokeApiService;
    private final TranslationService translationService;

    @Autowired
    public PokemonService(PokeApiService pokeApiService, TranslationService translationService) {
        this.pokeApiService = pokeApiService;
        this.translationService = translationService;
    }

    public PokemonResponse getPokemonByName(String pokemonName) throws IOException, ResourceNotFoundException, BusinessException {
        PokeApiDto pokeApiDto = this.pokeApiService.getPokemon(pokemonName.toLowerCase());
        String pokemonDescription = this.getPokemonDescription(pokeApiDto, ENGLISH_LANGUAGE_IDENTIFIER);
        return PokemonMapper.toPokemonResponse(pokeApiDto, pokemonDescription);
    }

    public PokemonResponse getPokemonWithFunTranslation(String pokemonName) throws IOException, ResourceNotFoundException, BusinessException {
        PokemonResponse pokemonResponse = this.getPokemonByName(pokemonName);
        String translationLanguage = getTranslationLanguageForPokemonType(pokemonResponse.getHabitat(), pokemonResponse.getIsLegendary());
        try {
            TranslationDto translationDto = this.translationService.translate(pokemonResponse.getDescription(), translationLanguage);
            pokemonResponse.setDescription(translationDto.getContents().getTranslated());
        } catch (Exception e) {
            log.error("Could not translate description for {} in {}. Reverting to basic description", pokemonName, translationLanguage);
        }
        return pokemonResponse;
    }

    public String getTranslationLanguageForPokemonType(String habitat, boolean isLegendary) {
        if(habitat.equalsIgnoreCase(CAVE_HABITAT) || isLegendary) {
            return YODA_LANGUAGE_IDENTIFIER;
        }
        return SHAKESPEARE_LANGUAGE_IDENTIFIER;
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
