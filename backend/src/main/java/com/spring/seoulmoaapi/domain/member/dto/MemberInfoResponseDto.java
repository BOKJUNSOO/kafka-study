package com.spring.seoulmoaapi.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.seoulmoaapi.domain.event.entity.Category;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.MemberCategoryDto;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoResponseDto {
    private Long userId;

    private String username;
    private String nickname;
    private int age;
    private String gender;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MemberCategoryDto> memberCategories = new ArrayList<>();
    private List<Long> memberCategoryIds = new ArrayList<>();
}
