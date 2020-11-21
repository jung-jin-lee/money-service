package com.example.money.sprinkling.service;

import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.entity.SprinklingItem;

import java.util.List;

public interface SprinklingItemService {
    List<SprinklingItem> generateSprinklingItemListFrom(Sprinkling sprinkling);
}
