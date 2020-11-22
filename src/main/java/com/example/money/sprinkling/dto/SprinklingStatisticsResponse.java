package com.example.money.sprinkling.dto;

import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.entity.SprinklingItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class SprinklingStatisticsResponse {
    private LocalDateTime sprinkledAt;

    private int amountSprinkled;

    private int amountReceived;

    private List<ReceivedInfo> receivedInfoList;

    private SprinklingStatisticsResponse(LocalDateTime sprinkledAt, int amountSprinkled) {
        this.sprinkledAt = sprinkledAt;
        this.amountSprinkled = amountSprinkled;
        this.amountReceived = 0;
        this.receivedInfoList = new ArrayList<>();
    }

    private SprinklingStatisticsResponse(
            LocalDateTime sprinkledAt,
            int amountSprinkled, int amountReceived,
            List<ReceivedInfo> receivedInfoList
    ) {
      this.sprinkledAt = sprinkledAt;
      this.amountSprinkled = amountSprinkled;
      this.amountReceived = amountReceived;
      this.receivedInfoList = receivedInfoList;
    }

    public static SprinklingStatisticsResponse of(Sprinkling sprinkling) {
        List<SprinklingItem> itemReceivedList = sprinkling.getSprinklingItems().stream()
                .filter((item) -> item.getUserIdReceived() != null)
                .collect(Collectors.toList());
        if (itemReceivedList == null || itemReceivedList.size() == 0) {
            return new SprinklingStatisticsResponse(sprinkling.getCreatedAt(), sprinkling.getAmount());
        }

        int amountReceived = itemReceivedList.stream().map((item) -> item.getAmount()).reduce(0, Integer::sum);

        return new SprinklingStatisticsResponse(
                sprinkling.getCreatedAt(),
                sprinkling.getAmount(),
                amountReceived,
                itemReceivedList.stream()
                        .map((item) -> SprinklingStatisticsResponse.ReceivedInfo.of(item.getAmount(), item.getUserIdReceived()))
                        .collect(Collectors.toList())
        );
    }

    @Getter
    @NoArgsConstructor
    public static class ReceivedInfo {
        private int amount;

        private Long userId;

        private ReceivedInfo(int amount, Long userId) {
            this.amount = amount;
            this.userId = userId;
        }

        public static ReceivedInfo of(int amount, Long userId) {
            return new ReceivedInfo(amount, userId);
        }
    }
}
