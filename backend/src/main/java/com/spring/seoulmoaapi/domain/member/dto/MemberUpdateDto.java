package com.spring.seoulmoaapi.domain.member.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
@Data
public class MemberUpdateDto {
    private String nickname;
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private int age;
    @NotBlank(message = "성별은 필수입니다.")
    private String gender;
    private List<Long> interestIds;
}
