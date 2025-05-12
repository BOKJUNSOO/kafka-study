package com.spring.seoulmoaapi.domain.event.repository;

import com.spring.seoulmoaapi.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// JPA Criteria를 JpaRepository와 함께 사용하기위해 EventCustomRepository를 함께 상속 
public interface EventRepository extends JpaRepository<Event, Long>, EventCustomRepository {
    Optional<Event> findByHomepage(String homepage);
}
