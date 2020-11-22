package com.example.money.sprinkling.service;

import com.example.money.common.exception.NotFoundException;
import com.example.money.sprinkling.dto.SprinklingCreateRequest;
import com.example.money.sprinkling.dto.SprinklingCreateResponse;
import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.entity.SprinklingItem;
import com.example.money.sprinkling.exception.*;
import com.example.money.sprinkling.repository.SprinklingItemRepository;
import com.example.money.sprinkling.repository.SprinklingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("sprinklingService")
public class SprinklingServiceImpl implements SprinklingService {

    private final SprinklingItemService sprinklingItemService;
    private final SprinklingRepository sprinklingRepository;
    private final SprinklingItemRepository sprinklingItemRepository;

    @Autowired
    public SprinklingServiceImpl(
            SprinklingItemService sprinklingItemService,
            SprinklingRepository sprinklingRepository,
            SprinklingItemRepository sprinklingItemRepository
    ) {
        this.sprinklingItemService = sprinklingItemService;
        this.sprinklingRepository = sprinklingRepository;
        this.sprinklingItemRepository = sprinklingItemRepository;
    }

    @Transactional
    @Override
    public SprinklingCreateResponse createSprinkling(SprinklingCreateRequest request, Long userId, String roomId) {
        Sprinkling sprinkling = request.toEntity(userId, roomId);
        sprinklingRepository.save(sprinkling);

        List<SprinklingItem> sprinklingItemList = sprinklingItemService.generateSprinklingItemListFrom(sprinkling);
        sprinklingItemList.stream().forEach(sprinklingItemRepository::save);

        return SprinklingCreateResponse.builder()
                .token(sprinkling.getToken())
                .build();
    }

    @Override
    public Sprinkling findSprinklingByToken(String token) {
        Sprinkling sprinkling = sprinklingRepository.findFirstByToken(token);
        if (sprinkling == null) {
            throw new NotFoundException("sprinkling", "NOT_FOUND_SPRINKLING");
        }

        return sprinkling;
    }

    @Override
    public Sprinkling findSprinklingByTokenAndUserIdCreated(String token, Long userIdCreated) {
        Sprinkling sprinkling = sprinklingRepository.findFirstByTokenAndUserIdCreated(token, userIdCreated);
        if (sprinkling == null) {
            throw new NotFoundException("sprinkling", "NOT_FOUND_SPRINKLING");
        }

        return sprinkling;
    }

    @Override
    public void validateSprinklingReceivable(Sprinkling sprinkling, Long userId, String roomId) {
        if (sprinkling.getUserIdCreated().equals(userId)) {
            throw new CannotReceivedUserIdCreatedException("ERROR_CANNOT_RECEIVED_USER_ID_CREATED");
        }
        if (!sprinkling.getRoomIdTargeted().equals(roomId)) {
            throw new CannotReceivedDifferentRoomIdException("ERROR_NOT_EQUAL_ROOM_ID");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(sprinkling.getCreatedAt().plusMinutes(10))) {
            throw new InvalidReceiveDateTimeException("ERROR_INVALID_RECEIVE_TIME");
        }

        if (sprinkling.getSprinklingItems() == null) {
            throw new NotFoundException("sprinklingItem", "NOT_FOUND_SPRINKLING_ITEM");
        }
        if (sprinkling.getSprinklingItems().stream().anyMatch((item) -> item.getUserIdReceived() == userId)) {
            throw new AlreadyReceivedException("ERROR_ALREADY_RECEIVED");
        }
    }

    @Override
    public void validateSprinklingStatisticsViewable(Sprinkling sprinkling) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(sprinkling.getCreatedAt().plusDays(7))) {
            throw new InvalidStatisticsViewableDateTimeException("ERROR_INVALID_STATISTICS_VIEWABLE_TIME");
        }
    }
}
