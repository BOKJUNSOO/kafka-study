package com.spring.seoulmoaapi.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponseDto {
    List<EventDto> eventList;
    Integer offset;
    Integer limit;
    Integer totalCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Integer totalPages;
}
