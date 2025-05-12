package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class ReviewUserDto {
    private Long reviewId;
    private Long userId;
    private Long eventId;
    private String eventTitle;
    private LocalDateTime calendarDay;
    private String content;
    private String imageUrl;

    public String getCalendarDay() {
        return calendarDay != null
                ? calendarDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;
    }
}

