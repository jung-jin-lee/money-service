package com.example.money.sprinkling.service;

import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.entity.SprinklingItem;
import com.example.money.sprinkling.repository.SprinklingItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SprinklingItemServiceTest {

    private SprinklingItemService sprinklingItemService;

    @Mock
    private SprinklingItemRepository sprinklingItemRepository;

    @BeforeEach
    public void init() {
        sprinklingItemService = new SprinklingItemServiceImpl(sprinklingItemRepository);
    }

    @Test
    public void 금액이_22222원이고_뿌려야_할_인원이_3명이면_반환되는_아이템은_3개여야_한다() {
        int numPeople = 3;

        Sprinkling sprinkling = Sprinkling.builder()
                .amount(22222)
                .numPeople(numPeople)
                .build();

        List<SprinklingItem> itemList = sprinklingItemService.generateSprinklingItemListFrom(sprinkling);

        assertThat(itemList.size()).isEqualTo(numPeople);
    }


    @Test
    public void 금액이_22222원이고_뿌려야_할_인원이_3명이면_반환되는_아이템의_총금액은_22222원이어야_한다() {
        int amount = 22222;

        Sprinkling sprinkling = Sprinkling.builder()
                .amount(amount)
                .numPeople(3)
                .build();

        List<SprinklingItem> itemList = sprinklingItemService.generateSprinklingItemListFrom(sprinkling);

        assertThat(
                itemList.stream()
                        .map((item) -> item.getAmount())
                        .reduce(0, Integer::sum)
        ).isEqualTo(amount);
    }
}
