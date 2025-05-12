package com.spring.seoulmoaapi.domain.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "이벤트 검색 조건 DTO", example = """
{
  "title": "전시",
  "gu": ["강남구","성동구"],
  "isFree": true,
  "categoryId": [1,2,3],
  "descriptionKeyword": "전시",
  "isOpen" : true,
  "startDate": "2018-10-16T00:00:00",
  "endDate": "2025-10-24T00:00:00",
  "offset": 0,
  "limit": 10
}
""")
public class EventSearchDto {
    private String title;
    private List<String> gu;
    private Boolean isFree;
//    private String categoryId;
    private List<Long> categoryId;
    private String descriptionKeyword;
    private Boolean isOpen;
    //2024-10-24T00:00:00
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer offset = 0;
    private Integer limit = 3;

    private Long userId;
}
