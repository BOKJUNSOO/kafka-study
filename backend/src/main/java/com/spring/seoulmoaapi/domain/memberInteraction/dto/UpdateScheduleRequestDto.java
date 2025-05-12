package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateScheduleRequestDto {
    private LocalDateTime scheduleTime;
    private Long scheduleId;
}
