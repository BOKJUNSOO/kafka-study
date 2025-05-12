package com.spring.seoulmoaapi.domain.memberInteraction.dto;

import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberCategory;
import lombok.Data;

@Data
public class MemberCategoryDto {
    private Long categoryId;
    private String categoryName;

    public static MemberCategoryDto from(MemberCategory mc) {
        return new MemberCategoryDto(
                mc.getCategory().getCategoryId(),
                mc.getCategory().getName()
        );
    }
    public MemberCategoryDto(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}