package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewRequestDto {
    @NotNull(message = "이벤트 아이디가 존재하지 않습니다.")
    private Long eventId;

    @NotBlank(message = "리뷰가 입력되지 않았습니다.")
    private String content;
}
