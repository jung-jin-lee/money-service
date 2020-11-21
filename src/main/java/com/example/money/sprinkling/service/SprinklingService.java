package com.example.money.sprinkling.service;


import com.example.money.sprinkling.dto.SprinklingCreateRequest;
import com.example.money.sprinkling.dto.SprinklingCreateResponse;

public interface SprinklingService {
    SprinklingCreateResponse createSprinkling(SprinklingCreateRequest request, Long userId, String roomId);
}
