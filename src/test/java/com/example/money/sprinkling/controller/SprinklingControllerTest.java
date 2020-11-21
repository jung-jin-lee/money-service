package com.example.money.sprinkling.controller;

import com.example.money.common.code.ErrorCode;
import com.example.money.sprinkling.dto.SprinklingCreateRequest;
import com.example.money.sprinkling.dto.SprinklingCreateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SprinklingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 요청_헤더에_사용자_아이디와_방번호가_없으면_400_코드와_함께_필수_헤더가_빠졌다_라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.MISSING_REQUEST_HEADER_ERROR;
        mockMvc.perform(
                post("/sprinkling")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 요청_헤더에_방번호가_없으면_400_코드와_함께_필수_헤더가_빠졌다_라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.MISSING_REQUEST_HEADER_ERROR;
        mockMvc.perform(
                post("/sprinkling")
                        .header("X-USER-ID", 1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 요청_헤더에_사용자_아이디가_없으면_400_코드와_함께_필수_헤더가_빠졌다_라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.MISSING_REQUEST_HEADER_ERROR;
        mockMvc.perform(
                post("/sprinkling")
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 요청_바디에_파라미터가_없으면_400_코드와_함께_유효하지_않은_바디라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY_ERROR;
        mockMvc.perform(
                    post("/sprinkling")

                            .header("X-USER-ID", 1)
                            .header("X-ROOM-ID", "test")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }


    @Test
    public void 요청_바디에_금액이_0_이하이면_400_코드와_함께_유효하지_않은_바디라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY_ERROR;
        mockMvc.perform(
                post("/sprinkling")

                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
                        .param("amount", "0")
                        .param("numPeople", "5")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 요청_바디에_뿌릴_인원이_0_이하이면_400_코드와_함께_유효하지_않은_바디라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY_ERROR;
        mockMvc.perform(
                post("/sprinkling")

                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
                        .param("amount", "5000")
                        .param("numPeople", "0")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 요청_바디에_금액과_뿌릴_인원이_값이_유효하면_201_코드와_함께_3자리_토큰을_리턴해야한다() throws Exception {
        SprinklingCreateRequest request = new SprinklingCreateRequest();
        request.setAmount(5000);
        request.setNumPeople(3);
        String requestBody = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(
                post("/sprinkling")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        SprinklingCreateResponse response = objectMapper.readValue(content, SprinklingCreateResponse.class);

        assertThat(response.getToken().length()).isEqualTo(3);
    }
}