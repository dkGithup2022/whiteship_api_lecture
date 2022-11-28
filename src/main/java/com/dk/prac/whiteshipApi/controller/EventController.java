package com.dk.prac.whiteshipApi.controller;

import com.dk.prac.whiteshipApi.domain.Event;
import com.dk.prac.whiteshipApi.domain.dto.EventDto;
import com.dk.prac.whiteshipApi.domain.responseResource.EventResource;
import com.dk.prac.whiteshipApi.repository.EventRepository;
import com.dk.prac.whiteshipApi.service.validator.EventValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors error) {

        if (error.hasErrors()) {
            System.out.println("error " + error.toString());
            return ResponseEntity.badRequest().body(error);
        }

        eventValidator.validate(eventDto, error);

        // null check 을 Validator 기본 기능에서 해주니까 이렇게 두번 나눠서 쓰는 것도 ok
        if (error.hasErrors()) {
            System.out.println("error " + error.toString());
            return ResponseEntity.badRequest().body(error);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();

        Event newEvent = eventRepository.save(event);

        // 생성된 이벤트 조회 연산 링크
        URI createdUri =
                linkTo(EventController.class).slash("{id}").toUri();

        EventResource eventResource = new EventResource(newEvent);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(linkTo(EventController.class).slash("{id}").withSelfRel());

        //TODO : api 생성 후 uri 변경
        eventResource.add(linkTo(EventController.class).withRel("update-event"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

}
