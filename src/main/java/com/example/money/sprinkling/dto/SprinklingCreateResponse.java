package com.example.money.sprinkling.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SprinklingCreateResponse {
    private String token;

    @Builder
    public SprinklingCreateResponse(String token) {
        this.token = token;
    }
}
