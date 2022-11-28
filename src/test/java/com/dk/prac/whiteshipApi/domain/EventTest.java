package com.dk.prac.whiteshipApi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @Test
    public void testFree_false(){
        //given
        Event event = Event.builder()
                .basePrice(10)
                .maxPrice(10)
                .build();
        //when
        event.update();
        //then
        assertEquals(event.isFree(), false);
    }

    @Test
    public void testFree_true(){
        //given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //when
        event.update();
        //then
        assertEquals(event.isFree(), true);
    }

    @Test
    public void testOffline_false(){
        Event event = Event.builder().location("home").build();
        event.update();
        assertEquals(event.isOffline(),false);
    }

    @Test
    public void testOffline_true(){
        Event event = Event.builder().location("").build();
        Event event1 = Event.builder().build();

        event.update();
        event1.update();

        assertEquals(event.isOffline(),true);
        assertEquals(event1.isOffline(),true);
    }

    @DisplayName("event.update() 테스트 ")
    @ParameterizedTest(name="{displayName} 's {index} trial :  location:{0}, basePrice : {1} , expected Offline : {2}, expected free {3} ")
    @CsvSource({"'home',20, false, false", "'',20, true, false","'home',0, false, true"})
    public void method_test_update(String location, int basePrice, boolean expectedOffline, boolean expectedFree){
        System.out.println("location:"+ location);
        System.out.println("basePrice:"+ basePrice);
        System.out.println("expectedOffline:"+ expectedOffline);
        System.out.println("expectedFree:"+ expectedFree);

        Event event = Event.builder().location(location).basePrice(basePrice).build();

        event.update();
        assertEquals(event.isOffline(), expectedOffline);
        assertEquals(event.isFree(),expectedFree);

    }
}