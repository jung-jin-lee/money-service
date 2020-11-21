package com.example.money.sprinkling.repository;

import com.example.money.sprinkling.entity.SprinklingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprinklingItemRepository extends JpaRepository<SprinklingItem, Long> {
}
