package com.spring.seoulmoaapi.global.common.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSync {
    private String title;
    private String categoryName;
    private String gu;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String fee;
    private Boolean isFree;
    private Double latitude;
    private Double longitude;
    private String homepage;
    private String imageUrl;
    private String detailUrl;
    private String targetUser;
    private String eventDescription;
}
