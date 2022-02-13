package com.truelayer.pokedex.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslationRequest {
    String text;
}
