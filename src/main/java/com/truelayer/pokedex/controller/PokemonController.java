package com.truelayer.pokedex.controller;

import com.truelayer.pokedex.dto.response.PokemonResponse;
import com.truelayer.pokedex.exception.BusinessException;
import com.truelayer.pokedex.exception.ResourceNotFoundException;
import com.truelayer.pokedex.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/{pokemonName}")
    public ResponseEntity<?> getPokemon(@PathVariable String pokemonName) throws IOException, ResourceNotFoundException, BusinessException {
        PokemonResponse pokemonResponse = pokemonService.getPokemonByName(pokemonName);
        return new ResponseEntity<PokemonResponse>(pokemonResponse, HttpStatus.OK);
    }
}
