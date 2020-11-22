package com.example.money.sprinkling.service;

import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.entity.SprinklingItem;

import java.util.Collection;
import java.util.List;

public interface SprinklingItemService {
    List<SprinklingItem> generateSprinklingItemListFrom(Sprinkling sprinkling);
    SprinklingItem updateSprinklingItem(SprinklingItem sprinklingItem);
    int updateUserIdReceivedFrom(Collection<SprinklingItem> sprinklingItemCollection, Long userId);
}
