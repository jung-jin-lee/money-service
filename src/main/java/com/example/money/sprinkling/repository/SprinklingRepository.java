package com.example.money.sprinkling.repository;

import com.example.money.sprinkling.entity.Sprinkling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SprinklingRepository extends JpaRepository<Sprinkling, Long> {
    Sprinkling findFirstByToken(String token);
}
