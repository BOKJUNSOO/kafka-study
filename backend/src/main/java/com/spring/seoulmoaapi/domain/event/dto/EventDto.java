package com.spring.seoulmoaapi.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.seoulmoaapi.domain.event.entity.Category;
import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.ReviewEventDto;
import com.spring.seoulmoaapi.domain.subway.dto.SubwayStationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {
    private Long eventId;

    private String title;

    private String categoryName;

    private Category categoryId;

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

    private int likeCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isLiked;

    private SubwayStationDto nearestStation;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReviewEventDto recentlyReview;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean hasReview;

    public static EventDto from(Event e,Boolean isLiked, int likeCount) {
        return EventDto.builder()
                .eventId(e.getEventId())
                .title(e.getTitle())
                .categoryName(e.getCategoryName())
                .categoryId(e.getCategoryId()) // category 객체 자체가 필요하다면 그대로 넘김
                .gu(e.getGu())
                .location(e.getLocation())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .fee(e.getFee())
                .isFree(e.getIsFree())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .homepage(e.getHomepage())
                .imageUrl(e.getImageUrl())
                .detailUrl(e.getDetailUrl())
                .targetUser(e.getTargetUser())
                .eventDescription(e.getEventDescription())
                .likeCount(likeCount)
                .isLiked(isLiked)
                .recentlyReview(null)
                .build();
    }
    public static EventDto from(Event e, int likeCount) {
        return EventDto.builder()
                .eventId(e.getEventId())
                .title(e.getTitle())
                .categoryName(e.getCategoryName())
                .categoryId(e.getCategoryId()) // category 객체 자체가 필요하다면 그대로 넘김
                .gu(e.getGu())
                .location(e.getLocation())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .fee(e.getFee())
                .isFree(e.getIsFree())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .homepage(e.getHomepage())
                .imageUrl(e.getImageUrl())
                .detailUrl(e.getDetailUrl())
                .targetUser(e.getTargetUser())
                .eventDescription(e.getEventDescription())
                .likeCount(likeCount)
                .recentlyReview(null)
                .build();
    }
}
