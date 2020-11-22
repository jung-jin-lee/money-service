package com.example.money.sprinkling.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class SprinklingStatisticsRequest {

    @NotNull
    @Size(min = 3, max = 3, message = "ERROR_TOKEN_SIZE_ONLY_3")
    private String token;
}
