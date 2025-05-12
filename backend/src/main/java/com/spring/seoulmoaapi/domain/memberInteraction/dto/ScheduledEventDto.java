package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import com.spring.seoulmoaapi.domain.event.dto.EventDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduledEventDto {
    private Long scheduleId;
    private EventDto event;
    private LocalDateTime scheduleTime;
    private boolean isPast;
    private boolean isPastScheduled;
}
