package com.truelayer.pokedex.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlavorTextEntry {

    @JsonProperty("flavor_text")
    private String flavorText;

    private NamedApiResource language;

}
