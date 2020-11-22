package com.example.money.sprinkling.dto;

import com.example.money.sprinkling.entity.Sprinkling;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SprinklingCreateRequest {
    @NotNull
    @Positive(message = "ERROR_AMOUNT_BIGGER_THAN_ZERO")
    int amount;

    @NotNull
    @Positive(message = "ERROR_NUM_PEOPLE_BIGGER_THAN_ZERO")
    int numPeople;

    private static String generateToken() {
        int origin = 48; // "0"
        int bound = 122; // "z"
        int tokenLength = 3;

        SecureRandom random = new SecureRandom();

        return random.ints(origin, bound)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(tokenLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

    public Sprinkling toEntity(Long userIdCreated, String roomIdTargeted) {
        return Sprinkling.builder()
                .amount(this.amount)
                .numPeople(this.numPeople)
                .userIdCreated(userIdCreated)
                .roomIdTargeted(roomIdTargeted)
                .token(generateToken())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
