package com.truelayer.pokedex.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PokemonResponse {

    private String name;
    private String description;
    private String habitat;
    private Boolean isLegendary;

}
