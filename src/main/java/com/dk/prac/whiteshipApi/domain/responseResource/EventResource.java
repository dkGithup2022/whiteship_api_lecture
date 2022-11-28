package com.dk.prac.whiteshipApi.domain.responseResource;

import com.dk.prac.whiteshipApi.domain.Event;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

public class EventResource extends RepresentationModel {
    @Getter
    @Setter
    @JsonUnwrapped
    private Event event;

    public EventResource (Event event){
        this.event = event;
    }

}
