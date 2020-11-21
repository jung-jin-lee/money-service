package com.example.money.sprinkling.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "sprinkling_item")
public class SprinklingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sprinkling_id")
    private Long sprinklingId;

    private Long userIdReceived;

    private int amount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Builder
    public SprinklingItem(Long sprinklingId, Long userIdReceived, int amount) {
        this.sprinklingId = sprinklingId;
        this.userIdReceived = userIdReceived;
        this.amount = amount;
    }
}
