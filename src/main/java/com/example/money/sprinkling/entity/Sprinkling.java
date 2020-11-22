package com.example.money.sprinkling.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "sprinkling")
public class Sprinkling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userIdCreated;

    private String roomIdTargeted;

    private int amount;

    private int numPeople;

    private String token;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "sprinkling_id")
    private Collection<SprinklingItem> sprinklingItems;

    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    public Sprinkling(int amount, int numPeople, Long userIdCreated, String roomIdTargeted, String token, LocalDateTime createdAt) {
        this.amount = amount;
        this.numPeople = numPeople;
        this.userIdCreated = userIdCreated;
        this.roomIdTargeted = roomIdTargeted;
        this.token = token;
        this.createdAt = createdAt;
    }
}
