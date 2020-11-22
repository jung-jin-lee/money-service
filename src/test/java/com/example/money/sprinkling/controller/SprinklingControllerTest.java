package com.example.money.sprinkling.controller;

import com.example.money.common.code.ErrorCode;
import com.example.money.sprinkling.dto.SprinklingCreateRequest;
import com.example.money.sprinkling.dto.SprinklingCreateResponse;
import com.example.money.sprinkling.dto.SprinklingReceiveRequest;
import com.example.money.sprinkling.dto.SprinklingReceiveResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(5000, 3);
        MvcResult result = mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isCreated())
                .andReturn();

        SprinklingCreateResponse response = createSprinklingCreateResponse(result);

        assertThat(response.getToken().length()).isEqualTo(3);
    }

    @Test
    public void 뿌리기_응답으로_받은_토큰을_사용하지_않으면_404_코드를_반환해야_한다() throws Exception {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_ERROR;
        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString("c0k");
        mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 2)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 뿌리기_금액이_5000원이면_받기_응답_금액은_1600원에서_1700원_사이여야_한다() throws Exception {
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(5000, 3);
        MvcResult result = mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isCreated())
                .andReturn();

        SprinklingCreateResponse sprinklingCreateResponse = createSprinklingCreateResponse(result);

        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString(sprinklingCreateResponse.getToken());
        MvcResult resultReceived = mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 2)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isOk())
                .andReturn();

        SprinklingReceiveResponse sprinklingReceiveResponse = createSprinklingReceiveResponse(resultReceived);
        assertThat(sprinklingReceiveResponse.getAmount()).isBetween(1600, 1700);
    }

    @Test
    public void 동일한_사람이_받기를_2번_요청하면_2번쨰_요청은_400_응답을_반환해야_한다() throws Exception {
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(5000, 3);
        MvcResult result = mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isCreated())
                .andReturn();

        SprinklingCreateResponse sprinklingCreateResponse = createSprinklingCreateResponse(result);

        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString(sprinklingCreateResponse.getToken());
        MvcResult resultReceived = mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 2)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isOk())
                .andReturn();

        ErrorCode errorCode = ErrorCode.SPRINKLING_ALREADY_RECEIVED_ERROR;
        mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 2)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 뿌린_사람이_받기를_요청하면_400_응답을_반환해야_한다() throws Exception {
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(5000, 3);
        MvcResult result = mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isCreated())
                .andReturn();

        SprinklingCreateResponse sprinklingCreateResponse = createSprinklingCreateResponse(result);

        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString(sprinklingCreateResponse.getToken());
        ErrorCode errorCode = ErrorCode.SPRINKLING_CANNOT_RECEIVED_USER_ID_CREATED_ERROR;
        mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 받는_사람의_방이_뿌린_사람이_선택한_방과_다르면_400_응답을_반환해야_한다() throws Exception {
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(5000, 3);
        MvcResult result = mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isCreated())
                .andReturn();

        SprinklingCreateResponse sprinklingCreateResponse = createSprinklingCreateResponse(result);

        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString(sprinklingCreateResponse.getToken());
        ErrorCode errorCode = ErrorCode.SPRINKLING_CANNOT_RECEIVED_DIFFERENT_ROOM_ID_ERROR;
        mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 2)
                        .header("X-ROOM-ID", "test2")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 뿌리고_10분이_지났으면_400_응답을_반환해야_한다() throws Exception {
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(5000, 3);
        MvcResult result = mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isCreated())
                .andReturn();

        SprinklingCreateResponse sprinklingCreateResponse = createSprinklingCreateResponse(result);

        TimeUnit.MINUTES.sleep(11);

        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString(sprinklingCreateResponse.getToken());
        ErrorCode errorCode = ErrorCode.SPRINKLING_INVALID_RECEIVE_DATE_TIME_ERROR;
        mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 2)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    private SprinklingCreateRequest createSprinklingCreateRequest(int amount, int numPeople) {
        SprinklingCreateRequest sprinklingCreateRequest = new SprinklingCreateRequest();
        sprinklingCreateRequest.setAmount(amount);
        sprinklingCreateRequest.setNumPeople(numPeople);

        return sprinklingCreateRequest;
    }

    private SprinklingReceiveRequest createSprinklingReceiveRequest(String token) {
        SprinklingReceiveRequest sprinklingReceiveRequest = new SprinklingReceiveRequest();
        sprinklingReceiveRequest.setToken(token);

        return sprinklingReceiveRequest;
    }


    private String createSprinklingCreateRequestString(int amount, int numPeople) throws Exception {
        SprinklingCreateRequest sprinklingCreateRequest = createSprinklingCreateRequest(amount, numPeople);

        return objectMapper.writeValueAsString(sprinklingCreateRequest);
    }

    private String createSprinklingReceiveRequestString(String token) throws Exception {
        SprinklingReceiveRequest sprinklingReceiveRequest = createSprinklingReceiveRequest(token);

        return objectMapper.writeValueAsString(sprinklingReceiveRequest);
    }

    private SprinklingCreateResponse createSprinklingCreateResponse(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString();

        return objectMapper.readValue(content, SprinklingCreateResponse.class);
    }

    private SprinklingReceiveResponse createSprinklingReceiveResponse(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString();

        return objectMapper.readValue(content, SprinklingReceiveResponse.class);
    }
}