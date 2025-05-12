package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewEventResponseDto {
    List<ReviewEventDto> reviewList;
    Integer totalCount;
    Integer offset;
    Integer limit;
}