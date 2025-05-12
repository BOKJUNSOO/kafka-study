package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class MostLikedDto {
    private Long eventId;
    private String eventTitle;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private Long categoryId;
    private String categoryName;
    private String gu;
    private String imageUrl;
    private Long likeCount;
    private String likeYn;

    public String getStartDate() {
        return startDate != null
                ? startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;
    }

    public String getEndDate() {
        return endDate != null
                ? endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;
    }
}
