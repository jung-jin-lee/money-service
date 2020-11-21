package com.example.money.sprinkling.service;

import com.example.money.sprinkling.dto.SprinklingCreateRequest;
import com.example.money.sprinkling.dto.SprinklingCreateResponse;
import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.entity.SprinklingItem;
import com.example.money.sprinkling.repository.SprinklingItemRepository;
import com.example.money.sprinkling.repository.SprinklingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
