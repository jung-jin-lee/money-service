package com.example.money.sprinkling.service;


import com.example.money.sprinkling.dto.SprinklingCreateRequest;
import com.example.money.sprinkling.dto.SprinklingCreateResponse;
import com.example.money.sprinkling.entity.Sprinkling;

public interface SprinklingService {
    SprinklingCreateResponse createSprinkling(SprinklingCreateRequest request, Long userId, String roomId);
    Sprinkling findSprinklingByToken(String token);
    Sprinkling findSprinklingByTokenAndUserIdCreated(String token, Long userIdCreated);
    void validateSprinklingReceivable(Sprinkling sprinkling, Long userId, String roomId);
    void validateSprinklingStatisticsViewable(Sprinkling sprinkling);
}
