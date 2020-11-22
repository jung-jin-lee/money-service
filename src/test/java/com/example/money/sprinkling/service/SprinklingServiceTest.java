package com.example.money.sprinkling.service;

import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.exception.InvalidStatisticsViewableDateTimeException;
import com.example.money.sprinkling.repository.SprinklingItemRepository;
import com.example.money.sprinkling.repository.SprinklingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SprinklingServiceTest {

    private SprinklingService sprinklingService;

    @Mock
    private SprinklingItemService sprinklingItemService;

    @Mock
    private SprinklingRepository sprinklingRepository;

    @Mock
    private SprinklingItemRepository sprinklingItemRepository;

    @BeforeEach
    public void init() {
        sprinklingService = new SprinklingServiceImpl(sprinklingItemService, sprinklingRepository, sprinklingItemRepository);
    }

    @Test
    public void 뿌리기_통계는_7일_동안_볼_수_있다() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eightDaysAgo = now.minusDays(8);
        Long userId = 1L;
        Sprinkling sprinkling = Sprinkling.builder()
                .amount(5000)
                .numPeople(3)
                .userIdCreated(userId)
                .roomIdTargeted("test")
                .createdAt(eightDaysAgo)
                .build();

        assertThatThrownBy(() -> {
            sprinklingService.validateSprinklingStatisticsViewable(sprinkling);
        }).isInstanceOf(InvalidStatisticsViewableDateTimeException.class);
    }
}
