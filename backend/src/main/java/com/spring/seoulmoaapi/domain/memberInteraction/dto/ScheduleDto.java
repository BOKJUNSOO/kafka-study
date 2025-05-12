package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDto {
    private Long eventId;
    private LocalDateTime scheduleTime;
}
