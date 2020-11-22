package com.example.money.sprinkling.service;

import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.entity.SprinklingItem;
import com.example.money.sprinkling.exception.EverySprinklingItemExhaustedException;
import com.example.money.sprinkling.repository.SprinklingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service("sprinklingItemService")
public class SprinklingItemServiceImpl implements SprinklingItemService {

    private final SprinklingItemRepository sprinklingItemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SprinklingItemServiceImpl(SprinklingItemRepository sprinklingItemRepository) {
        this.sprinklingItemRepository = sprinklingItemRepository;
    }

    @Override
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

    @Override
    public SprinklingItem updateSprinklingItem(SprinklingItem sprinklingItem) {
        return sprinklingItemRepository.save(sprinklingItem);
    }


    @Transactional
    @Override
    public int updateUserIdReceivedFrom(Collection<SprinklingItem> sprinklingItemCollection, Long userId) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.lock.timeout", 1000L);

        List<SprinklingItem> itemList = sprinklingItemCollection.stream()
                .filter((item) -> item.getUserIdReceived() == null)
                .collect(Collectors.toList());
        int itemListLength = itemList.size();
        for (var i = 0; i < itemListLength; i++) {
            try {
                SprinklingItem sprinklingItem = itemList.get(i);
                sprinklingItem = entityManager.find(SprinklingItem.class, sprinklingItem.getId(), LockModeType.PESSIMISTIC_WRITE, properties);
                if (sprinklingItem.getUserIdReceived() != null) {
                    continue;
                }
                sprinklingItem.setUserIdReceived(userId);
                this.updateSprinklingItem(sprinklingItem);

                return sprinklingItem.getAmount();
            } catch (Exception ex) {
            }
        }

        throw new EverySprinklingItemExhaustedException("EVERY_SPRINKLING_ITEM_EXHAUSTED");
    }
}
