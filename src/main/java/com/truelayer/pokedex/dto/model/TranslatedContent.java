package com.truelayer.pokedex.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslatedContent {

    private String translated;
    private String text;
    private String translation;
}

