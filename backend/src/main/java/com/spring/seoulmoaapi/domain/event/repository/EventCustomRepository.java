package com.spring.seoulmoaapi.domain.event.repository;

import com.spring.seoulmoaapi.domain.event.dto.EventResponseDto;
import com.spring.seoulmoaapi.domain.event.dto.EventSearchDto;

public interface EventCustomRepository {
    EventResponseDto search(EventSearchDto dto,Long userId);
}