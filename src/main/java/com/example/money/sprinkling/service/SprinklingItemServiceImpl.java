package com.example.money.sprinkling.service;

import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.entity.SprinklingItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("sprinklingItemService")
public class SprinklingItemServiceImpl implements SprinklingItemService {
    public List<SprinklingItem> generateSprinklingItemListFrom(Sprinkling sprinkling) {
        List<SprinklingItem> items = new ArrayList<SprinklingItem>();

        int amount = sprinkling.getAmount();
        int numPeople = sprinkling.getNumPeople();
        int amountPerPerson = (int) Math.floor(amount / numPeople);
        int bonusOfFirstPerson = amount - (amountPerPerson * numPeople);

        // sprinkling item of first person
        items.add(
                SprinklingItem.builder()
                        .amount(amountPerPerson+bonusOfFirstPerson)
                        .sprinklingId(sprinkling.getId())
                        .build()
        );
        for (var i = 1; i < numPeople; i++) {
            items.add(
                    SprinklingItem.builder()
                            .amount(amountPerPerson)
                            .sprinklingId(sprinkling.getId())
                            .build()
            );
        }

        return items;
    }
}
