package com.example.money.sprinkling.controller;

import com.example.money.sprinkling.dto.SprinklingCreateRequest;
import com.example.money.sprinkling.dto.SprinklingCreateResponse;
import com.example.money.sprinkling.service.SprinklingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SprinklingController {

    private final SprinklingService sprinklingService;

    @Autowired
    public SprinklingController(SprinklingService sprinklingService) {
        this.sprinklingService = sprinklingService;
    }

    @PostMapping("/sprinkling")
    @ResponseBody
    public ResponseEntity<SprinklingCreateResponse> createSprinkling(
            @RequestHeader(value = "X-USER-ID") Long userId,
            @RequestHeader(value = "X-ROOM-ID") String roomId,
            @Valid @RequestBody SprinklingCreateRequest request
    ) {
        SprinklingCreateResponse response = sprinklingService.createSprinkling(request, userId, roomId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
