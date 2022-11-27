package com.dk.prac.whiteshipApi.repository;


import com.dk.prac.whiteshipApi.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
