package com.example.money.sprinkling.controller;

import com.example.money.common.code.ErrorCode;
import com.example.money.common.dto.ErrorResponse;
import com.example.money.sprinkling.dto.*;
import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.exception.*;
import com.example.money.sprinkling.service.SprinklingItemService;
import com.example.money.sprinkling.service.SprinklingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SprinklingController {

    private final SprinklingService sprinklingService;
    private final SprinklingItemService sprinklingItemService;

    @Autowired
    public SprinklingController(SprinklingService sprinklingService, SprinklingItemService sprinklingItemService) {
        this.sprinklingService = sprinklingService;
        this.sprinklingItemService = sprinklingItemService;
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

    @PutMapping("/sprinkling/receive")
    @ResponseBody
    public ResponseEntity<SprinklingReceiveResponse> receiveSprinkling(
            @RequestHeader(value = "X-USER-ID") Long userId,
            @RequestHeader(value = "X-ROOM-ID") String roomId,
            @Valid @RequestBody SprinklingReceiveRequest request
    ) throws RuntimeException {
        Sprinkling sprinkling = sprinklingService.findSprinklingByToken(request.getToken());

        sprinklingService.validateSprinklingReceivable(sprinkling, userId, roomId);

        int amountReceived = sprinklingItemService.updateUserIdReceivedFrom(sprinkling.getSprinklingItems(), userId);

        SprinklingReceiveResponse response = SprinklingReceiveResponse.builder().amount(amountReceived).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/sprinkling/stats")
    @ResponseBody
    public ResponseEntity<SprinklingStatisticsResponse> getSprinklingStatistics(
            @RequestHeader(value = "X-USER-ID") Long userId,
            @Valid @RequestBody SprinklingStatisticsRequest request
    ) throws RuntimeException {
        Sprinkling sprinkling = sprinklingService.findSprinklingByTokenAndUserIdCreated(request.getToken(), userId);

        sprinklingService.validateSprinklingStatisticsViewable(sprinkling);

        SprinklingStatisticsResponse response = SprinklingStatisticsResponse.of(sprinkling);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(AlreadyReceivedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyReceivedException(AlreadyReceivedException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.SPRINKLING_ALREADY_RECEIVED_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotReceivedUserIdCreatedException.class)
    public ResponseEntity<ErrorResponse> handleCannotReceivedUserIdCreatedException(CannotReceivedUserIdCreatedException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.SPRINKLING_CANNOT_RECEIVED_USER_ID_CREATED_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidReceiveDateTimeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidReceiveDateTimeException(InvalidReceiveDateTimeException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.SPRINKLING_INVALID_RECEIVE_DATE_TIME_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotReceivedDifferentRoomIdException.class)
    public ResponseEntity<ErrorResponse> handleCannotReceivedDifferentRoomIdException(CannotReceivedDifferentRoomIdException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.SPRINKLING_CANNOT_RECEIVED_DIFFERENT_ROOM_ID_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EverySprinklingItemExhaustedException.class)
    public ResponseEntity<ErrorResponse> handleEverySprinklingItemExhaustedException(EverySprinklingItemExhaustedException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.SPRINKLING_EXHAUSTED_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidStatisticsViewableDateTimeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatisticsViewableDateTimeException(InvalidStatisticsViewableDateTimeException ex) {
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.SPRINKLING_INVALID_STATISTICS_VIEWABLE_DATE_TIME_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
