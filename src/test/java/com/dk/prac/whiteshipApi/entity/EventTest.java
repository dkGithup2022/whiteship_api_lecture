package com.dk.prac.whiteshipApi.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder().build();
        assertNotNull(event);
    }

    @Test
    public void javaBean(){
        Event event = new Event();
        event.setBasePrice(10);
        event.setName("jb");
        event.setDescription("created by java bean spec");

        assertEquals(event.getName(),"jb");
    }
}