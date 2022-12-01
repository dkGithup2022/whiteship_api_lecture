package com.dk.prac.whiteshipApi.controller;

import com.dk.prac.whiteshipApi.domain.dto.EventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class EventControllerRestDocsTest {

    MockMvc mockMvc;

    RestDocumentationResultHandler restDocumentationResultHandler;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider){
        this.restDocumentationResultHandler = document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(restDocumentationContextProvider).snippets().withEncoding("ISO-8859-1"))
                .alwaysDo(this.restDocumentationResultHandler)
                .build();
    }



    @Test
    @DisplayName("GENREATE REST DOCS / path : '/api/event' / method: EventController.createEvent()")
    public void  create_event_docs() throws Exception {

        EventDto eventDto = EventDto.builder()
                .name("name")
                .description("description")
                .location("home")
                .basePrice(10)
                .maxPrice(100)
                .beginEventDateTime(LocalDateTime.of(2022, 10, 10, 10, 10))
                .endEventDateTime(LocalDateTime.of(2022, 10, 10, 10, 10))
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 10, 10, 10, 10))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 10, 10, 10, 10))
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("free").exists())
                .andExpect(jsonPath("offline").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andDo(document("create-event-docs")
                )
                .andDo(print());
    }

    /*

        mockMvc.perform(post("/api/events")
                .contentType(MediaTypes.HAL_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto))
        )
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("free").exists())
                .andExpect(jsonPath("offline").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andDo(document("create-events",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-events").description("link to update an existing event"),
                                linkWithRel("profile").description("profile document link")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header "),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("begin enrollment time of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("close enrollment time of new event"),
                                fieldWithPath("beginEventDateTime").description("begin event time , it checks endEventTime"),
                                fieldWithPath("endEventDateTime").description("end event time , it checks beginEventTime"),
                                fieldWithPath("location").description("location of event, if it is empty, this event goes offline  "),
                                fieldWithPath("basePrice").description("base price "),
                                fieldWithPath("maxPrice").description(" max price"),
                                fieldWithPath("limitOfEnrollment").description("limit of entollment")
                                ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("this url"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("response is HAL + JSON")
                        ),responseFields(
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("begin enrollment time of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("close enrollment time of new event"),
                                fieldWithPath("beginEventDateTime").description("begin event time , it checks endEventTime"),
                                fieldWithPath("endEventDateTime").description("end event time , it checks beginEventTime"),
                                fieldWithPath("location").description("location of event, if it is empty, this event goes offline  "),
                                fieldWithPath("basePrice").description("base price "),
                                fieldWithPath("maxPrice").description(" max price"),
                                fieldWithPath("limitOfEnrollment").description("limit of entollment"),
                                fieldWithPath("offline").description("offline meeting or not : T/F"),
                                fieldWithPath("free").description(" check if both base, max price is 0 : T/F"),
                                fieldWithPath("eventStatus").description(" event stauts "),
                                fieldWithPath("id").description(" generated id "),
                                fieldWithPath("_links.*").ignored(),
                                fieldWithPath("_links.query-events.*").ignored(),
                                fieldWithPath("_links.update-events.*").ignored(),
                                fieldWithPath("_links.self.*").ignored(),
                                fieldWithPath("_links.profile.*").ignored()
                                )

                ))
                .andDo(print());
    }

     */


}
