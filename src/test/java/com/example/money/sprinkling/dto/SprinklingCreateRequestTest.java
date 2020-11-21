package com.example.money.sprinkling.dto;

import com.example.money.sprinkling.entity.Sprinkling;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SprinklingCreateRequestTest {

    @Test
    public void 토큰의_길이는_3이어야한다() {
        SprinklingCreateRequest request = new SprinklingCreateRequest();
        Sprinkling sprinkling = request.toEntity((long) 1, "test");

        assertThat(sprinkling.getToken().length()).isEqualTo(3);
    }
}
