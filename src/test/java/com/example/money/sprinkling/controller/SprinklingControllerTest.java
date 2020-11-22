package com.example.money.sprinkling.controller;

import com.example.money.common.code.ErrorCode;
import com.example.money.sprinkling.dto.*;
import com.example.money.sprinkling.entity.Sprinkling;
import com.example.money.sprinkling.repository.SprinklingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SprinklingControllerTest {

    @Autowired
    private SprinklingRepository sprinklingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 뿌리기_API_는_요청_헤더에_사용자_아이디와_방번호가_없으면_400_코드와_함께_필수_헤더가_빠졌다_라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.MISSING_REQUEST_HEADER_ERROR;
        mockMvc.perform(
                post("/sprinkling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 뿌리기_API_는_요청_헤더에_방번호가_없으면_400_코드와_함께_필수_헤더가_빠졌다_라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.MISSING_REQUEST_HEADER_ERROR;
        mockMvc.perform(
                post("/sprinkling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 뿌리기_API_는_요청_헤더에_사용자_아이디가_없으면_400_코드와_함께_필수_헤더가_빠졌다_라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.MISSING_REQUEST_HEADER_ERROR;
        mockMvc.perform(
                post("/sprinkling")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 뿌리기_API_는_요청_바디에_파라미터가_없으면_400_코드와_함께_유효하지_않은_바디라는_메시지를_리턴해야한다() throws Exception {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY_ERROR;
        mockMvc.perform(
                    post("/sprinkling")
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
    public void 뿌리기_API_는_요청_바디에_금액이_0_이하이면_400_코드와_함께_유효하지_않은_바디라는_메시지를_리턴해야한다() throws Exception {
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(0, 5);
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY_ERROR;
        mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
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
    public void 뿌리기_API_는_요청_바디에_뿌릴_인원이_0_이하이면_400_코드와_함께_유효하지_않은_바디라는_메시지를_리턴해야한다() throws Exception {
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(5000, 0);
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY_ERROR;
        mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
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
    public void 뿌리기_API_는_요청_바디에_금액과_뿌릴_인원이_값이_유효하면_201_코드와_함께_3자리_토큰을_리턴해야한다() throws Exception {
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
    public void 받기_API_는_뿌리기_응답으로_받은_토큰을_사용하지_않으면_404_코드를_반환해야_한다() throws Exception {
        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString("c0k");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_ERROR;
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
    public void 받기_API_는_뿌리기_금액이_5000원이면_받기_응답_금액은_1600원에서_1700원_사이여야_한다() throws Exception {
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
    public void 받기_API_는_동일한_사람이_받기를_2번_요청하면_2번쨰_요청은_400_응답을_반환해야_한다() throws Exception {
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
    public void 받기_API_는_뿌린_사람이_받기를_요청하면_400_응답을_반환해야_한다() throws Exception {
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
    public void 받기_API_는_받는_사람의_방이_뿌린_사람이_선택한_방과_다르면_400_응답을_반환해야_한다() throws Exception {
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
    public void 받기_API_는_뿌리고_10분이_지났으면_400_응답을_반환해야_한다() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fifteenMinutesAgo = now.minusMinutes(15);
        String token = "c0p";
        Long userIdCreated = 3L;
        Sprinkling sprinkling = Sprinkling.builder()
                .roomIdTargeted("test")
                .numPeople(3)
                .amount(5000)
                .token(token)
                .userIdCreated(userIdCreated)
                .createdAt(fifteenMinutesAgo)
                .build();

        sprinklingRepository.save(sprinkling);

        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString(token);
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

    @Test
    public void 조회_API_의_요청_바디에_값이_없이_요청하면_400_응답을_반환해야_한다() throws Exception {
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY_ERROR;
        mockMvc.perform(
                post("/sprinkling/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 3)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 조회_API_의_요청_바디에_토큰_길이가_3자리가_아닌채로_요청하면_400_응답을_반환해야_한다() throws Exception {
        String sprinklingStatisticsRequestBody = createSprinklingStatisticsRequestString("c0");
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY_ERROR;
        mockMvc.perform(
                post("/sprinkling/stats")
                        .content(sprinklingStatisticsRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 3)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));

        String sprinklingStatisticsRequestBody2 = createSprinklingStatisticsRequestString("c0f9");
        mockMvc.perform(
                post("/sprinkling/stats")
                        .content(sprinklingStatisticsRequestBody2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 3)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 조회_API_를_뿌린_사람이_아닌_다른_사람이_요청하면_404_응답을_반환해야_한다() throws Exception {
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

        String sprinklingStatisticsRequestBody = createSprinklingStatisticsRequestString(sprinklingCreateResponse.getToken());
        ErrorCode errorCode = ErrorCode.NOT_FOUND_ERROR;
        mockMvc.perform(
                post("/sprinkling/stats")
                        .content(sprinklingStatisticsRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 3)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 조회_API_를_유효하지_않은_토큰으로_요청하면_404_응답을_반환해야_한다() throws Exception {
        String sprinklingStatisticsRequestBody = createSprinklingStatisticsRequestString("c0p");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_ERROR;
        mockMvc.perform(
                post("/sprinkling/stats")
                        .content(sprinklingStatisticsRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 3)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 조회_API_를_7일이_지난_뿌리기_토큰으로_요청하면_400_응답을_반환해야_한다() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eightDaysAgo = now.minusDays(8);
        String token = "c0p";
        Long userIdCreated = 3L;
        Sprinkling sprinkling = Sprinkling.builder()
                .roomIdTargeted("test")
                .numPeople(3)
                .amount(5000)
                .token(token)
                .userIdCreated(3L)
                .createdAt(eightDaysAgo)
                .build();

        sprinklingRepository.save(sprinkling);
        String sprinklingStatisticsRequestBody = createSprinklingStatisticsRequestString(token);
        ErrorCode errorCode = ErrorCode.SPRINKLING_INVALID_STATISTICS_VIEWABLE_DATE_TIME_ERROR;
        mockMvc.perform(
                post("/sprinkling/stats")
                        .content(sprinklingStatisticsRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", userIdCreated)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    public void 조회_API_요청이_유효한_경우_현재_상태를_응답해야_한다() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.now();
        int amount = 5000;
        String sprinklingCreateRequestBody = createSprinklingCreateRequestString(amount, 3);
        MvcResult result = mockMvc.perform(
                post("/sprinkling")
                        .content(sprinklingCreateRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 3)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isCreated())
                .andReturn();

        SprinklingCreateResponse sprinklingCreateResponse = createSprinklingCreateResponse(result);

        String sprinklingReceiveRequestBody = createSprinklingReceiveRequestString(sprinklingCreateResponse.getToken());
        MvcResult resultReceived1 = mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 1)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isOk())
                .andReturn();

        SprinklingReceiveResponse sprinklingReceiveResponse1 = createSprinklingReceiveResponse(resultReceived1);

        MvcResult resultReceived2 = mockMvc.perform(
                put("/sprinkling/receive")
                        .content(sprinklingReceiveRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 2)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isOk())
                .andReturn();

        SprinklingReceiveResponse sprinklingReceiveResponse2 = createSprinklingReceiveResponse(resultReceived2);

        String sprinklingStatisticsRequestBody = createSprinklingStatisticsRequestString(sprinklingCreateResponse.getToken());
        MvcResult resultStatistics = mockMvc.perform(
                post("/sprinkling/stats")
                        .content(sprinklingStatisticsRequestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", 3)
                        .header("X-ROOM-ID", "test")
        )
                .andExpect(status().isOk())
                .andReturn();

        SprinklingStatisticsResponse sprinklingStatisticsResponse = createSprinklingStatisticsResponse(resultStatistics);

        LocalDateTime endDateTime = LocalDateTime.now();
        int amountReceived = sprinklingReceiveResponse1.getAmount() + sprinklingReceiveResponse2.getAmount();

        assertThat(sprinklingStatisticsResponse.getSprinkledAt()).isBetween(startDateTime, endDateTime);
        assertThat(sprinklingStatisticsResponse.getAmountSprinkled()).isEqualTo(amount);
        assertThat(sprinklingStatisticsResponse.getAmountReceived()).isEqualTo(amountReceived);
        assertThat(sprinklingStatisticsResponse.getReceivedInfoList().size()).isEqualTo(2);
        assertThat(sprinklingStatisticsResponse.getReceivedInfoList()).extracting("userId").containsOnly(1L, 2L);
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

    private SprinklingStatisticsRequest createSprinklingStatisticsRequest(String token) {
        SprinklingStatisticsRequest sprinklingStatisticsRequest = new SprinklingStatisticsRequest();
        sprinklingStatisticsRequest.setToken(token);

        return sprinklingStatisticsRequest;
    }

    private String createSprinklingCreateRequestString(int amount, int numPeople) throws Exception {
        SprinklingCreateRequest sprinklingCreateRequest = createSprinklingCreateRequest(amount, numPeople);

        return objectMapper.writeValueAsString(sprinklingCreateRequest);
    }

    private String createSprinklingReceiveRequestString(String token) throws Exception {
        SprinklingReceiveRequest sprinklingReceiveRequest = createSprinklingReceiveRequest(token);

        return objectMapper.writeValueAsString(sprinklingReceiveRequest);
    }

    private String createSprinklingStatisticsRequestString(String token) throws Exception {
        SprinklingStatisticsRequest sprinklingStatisticsRequest = createSprinklingStatisticsRequest(token);

        return objectMapper.writeValueAsString(sprinklingStatisticsRequest);
    }

    private SprinklingCreateResponse createSprinklingCreateResponse(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString();

        return objectMapper.readValue(content, SprinklingCreateResponse.class);
    }

    private SprinklingReceiveResponse createSprinklingReceiveResponse(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString();

        return objectMapper.readValue(content, SprinklingReceiveResponse.class);
    }

    private SprinklingStatisticsResponse createSprinklingStatisticsResponse(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString();

        return objectMapper.readValue(content, SprinklingStatisticsResponse.class);
    }
}