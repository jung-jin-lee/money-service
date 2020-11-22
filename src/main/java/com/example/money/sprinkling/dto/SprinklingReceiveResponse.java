package com.example.money.sprinkling.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SprinklingReceiveResponse {
    private int amount;

    @Builder
    public SprinklingReceiveResponse(int amount) {
        this.amount = amount;
    }
}
