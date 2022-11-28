package com.dk.prac.whiteshipApi.controller;

import com.dk.prac.whiteshipApi.domain.Event;
import com.dk.prac.whiteshipApi.domain.dto.EventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception{

        Event event = Event.builder()
                .name("name")
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2022,10,10,10,10))
                .endEventDateTime(LocalDateTime.of(2022,10,11,10,10))
                .basePrice(100)
                .maxPrice(200)
                .location("강남역")
                .build();


        //enumerated 된 값을 이용한 테스트
        mockMvc.perform(
                post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(event))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andDo(print());

    }

    @Test
    @DisplayName("create event with bad input / expect code : 400 / no name data caught by valid annotation")
    public void test_create_event_bad_input() throws Exception{
        // event info with no name
        EventDto eventDto =  EventDto.builder()
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2022,10,10,10,10))
                .endEventDateTime(LocalDateTime.of(2022,10,11,10,10))
                .basePrice(100)
                .maxPrice(200)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto))
        )
        .andExpect(status().is4xxClientError())
        .andDo(print());
    }


    @Test
    @DisplayName("create event with bad input / expect code : 400 / wrong base price caught by valid()")
    public void test_create_event_bad_input2() throws Exception{
        // event info with no name
        EventDto eventDto =  EventDto.builder()
                .name("name")
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2022,10,10,10,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .endEventDateTime(LocalDateTime.of(2022,10,11,10,10))
                .basePrice(2000)
                .maxPrice(200)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }


    @Test
    @DisplayName("create event with bad input / expect code : 400 / check body of response of 400 bad request")
    public void test_create_event_bad_input3() throws Exception{
        // event info with no name
        EventDto eventDto =  EventDto.builder()
                .name("name")
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2022,10,10,10,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .endEventDateTime(LocalDateTime.of(2022,10,11,10,10))
                .basePrice(2000)
                .maxPrice(200)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejected").exists())

                .andDo(print());
    }

    @Test
    @DisplayName("event 응답에서 update 생성 필드 확인 : offline, free  -> false")
    public void testEventUpdatedField() throws Exception{
        EventDto eventDto =  EventDto.builder()
                .name("name")
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2022,10,10,10,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .endEventDateTime(LocalDateTime.of(2022,10,11,10,10))
                .basePrice(20)
                .maxPrice(200)
                .location("강남역")
                .build();

        mockMvc.perform(
                post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
        ).andDo(print())
        .andExpect(jsonPath("offline").value(false))
        .andExpect(jsonPath("free").value(false))
        ;
    }
    @Test
    @DisplayName("event 응답에서 update 생성 필드 확인 : offline, free -> true ")
    public void testEventUpdatedField2() throws Exception{
        EventDto eventDto =  EventDto.builder()
                .name("name")
                .description("description")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2022,10,10,10,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,10,10,10,10))
                .endEventDateTime(LocalDateTime.of(2022,10,11,10,10))
                .basePrice(0)
                .maxPrice(0)
                .build();

        mockMvc.perform(
                post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(eventDto))
        ).andDo(print())
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("free").value(true))
        ;
    }
}

