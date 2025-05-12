package com.spring.seoulmoaapi.domain.member.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class SignUpRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$",
            message = "비밀번호는 8자 이상이며, 영문자, 숫자, 특수문자(!@#$%^&*()_+=-)를 포함해야 합니다."
    )
    private String password;
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    @Min(value = 0, message = "나이는 0 이상이어야 합니다.")
    private int age;
    @NotBlank(message = "성별은 필수입니다.")
    private String gender;

    // 가입 시 선택한 관심사 ID들의 목록
    private List<Long> categoryIds;
}

