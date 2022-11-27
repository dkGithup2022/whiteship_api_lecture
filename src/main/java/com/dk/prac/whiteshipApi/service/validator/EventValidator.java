package com.dk.prac.whiteshipApi.service.validator;

import com.dk.prac.whiteshipApi.domain.dto.EventDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0){
            errors.rejectValue("basePrice","wrongValue","BasePrice is wrong.");
            errors.rejectValue("maxPrice","wrongValue","maxPrice is wrong.");
        }
        if(      eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEventDateTime())
                || eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEnrollmentDateTime())
                || eventDto.getEndEventDateTime().isBefore(eventDto.getCloseEnrollmentDateTime())
                || eventDto.getCloseEnrollmentDateTime().isBefore(eventDto.getBeginEnrollmentDateTime())
        ){
            errors.rejectValue("endEventTime","wrongValue","event time data is wrong.");
        }
    }
}
