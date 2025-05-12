package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private Long userId;
    private Long eventId;
    private String content;
}
