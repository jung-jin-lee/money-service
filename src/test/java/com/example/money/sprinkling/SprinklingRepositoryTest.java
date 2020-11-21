package com.example.money.sprinkling;

import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.repository.SprinklingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SprinklingRepositoryTest {

    @Autowired
    SprinklingRepository sprinklingRepository;

    @AfterEach
    public void cleanup() {
        sprinklingRepository.deleteAll();
    }

    @Test
    public void getAllAfterSave() {
        // given
        int amount = 15000;
        int numPeople = 3;

        sprinklingRepository.save(
                Sprinkling.builder()
                        .amount(amount)
                        .numPeople(numPeople)
                        .build()
        );

        // when
        List<Sprinkling> sprinklingList = sprinklingRepository.findAll();

        // then
        Sprinkling sprinkling = sprinklingList.get(0);
        assertThat(sprinkling.getAmount()).isEqualTo(amount);
        assertThat(sprinkling.getNumPeople()).isEqualTo(numPeople);
    }
}
