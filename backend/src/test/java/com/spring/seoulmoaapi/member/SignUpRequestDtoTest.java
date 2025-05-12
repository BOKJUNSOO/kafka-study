package com.spring.seoulmoaapi.member;

import com.spring.seoulmoaapi.domain.member.dto.SignUpRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignUpRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private SignUpRequestDto createValidDto() {
        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setUsername("validUser");
        dto.setPassword("Password123!"); // 올바른 비밀번호: 최소 8자 이상, 영문자, 숫자, 특수문자 포함
        dto.setNickname("Tester");
        dto.setAge(25);
        dto.setGender("남성");
        dto.setAddress("서울시 강남구");
        return dto;
    }

    @Test
    @DisplayName("유효한 DTO는 위반이 없어야 한다")
    void whenValidDto_thenNoViolations() {
        // given
        SignUpRequestDto dto = createValidDto();

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(dto);

        // then
        assertTrue(violations.isEmpty(), "유효한 DTO에는 위반이 없어야 한다");
    }

    @Test
    @DisplayName("비밀번호 유효성 검사 실패: 조건에 맞지 않는 비밀번호")
    void whenInvalidPassword_thenViolation() {
        // given
        SignUpRequestDto dto = createValidDto();
        dto.setPassword("pass"); // 조건 미충족: 8자 미만, 영문자/숫자/특수문자 포함 불가

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(dto);

        // then: 비밀번호 위반 메시지가 포함되어 있어야 함
        assertEquals(1, violations.size(), "비밀번호 위반이 1건 발생해야 한다");
        ConstraintViolation<SignUpRequestDto> violation = violations.iterator().next();
        // 메시지는 정규식 어노테이션에 설정한 메시지
        assertEquals("비밀번호는 8자 이상이며, 영문자, 숫자, 특수문자를 포함해야 합니다.", violation.getMessage());
    }

    @Test
    @DisplayName("닉네임 유효성 검사 실패: 빈 닉네임")
    void whenBlankNickname_thenViolation() {
        // given
        SignUpRequestDto dto = createValidDto();
        dto.setNickname(""); // 빈 문자열

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(dto);

        // then
        assertEquals(1, violations.size(), "닉네임 위반이 1건 발생해야 한다");
        ConstraintViolation<SignUpRequestDto> violation = violations.iterator().next();
        assertEquals("닉네임은 필수입니다.", violation.getMessage());
    }

    @Test
    @DisplayName("나이 유효성 검사 실패: 음수 값")
    void whenNegativeAge_thenViolation() {
        // given
        SignUpRequestDto dto = createValidDto();
        dto.setAge(-1); // 음수 값

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(dto);

        // then
        assertEquals(1, violations.size(), "나이 위반이 1건 발생해야 한다");
        ConstraintViolation<SignUpRequestDto> violation = violations.iterator().next();
        assertEquals("나이는 0 이상이어야 합니다.", violation.getMessage());
    }

    @Test
    @DisplayName("성별 유효성 검사 실패: 빈 성별")
    void whenBlankGender_thenViolation() {
        // given
        SignUpRequestDto dto = createValidDto();
        dto.setGender(""); // 빈 문자열

        // when
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(dto);

        // then
        assertEquals(1, violations.size(), "성별 위반이 1건 발생해야 한다");
        ConstraintViolation<SignUpRequestDto> violation = violations.iterator().next();
        assertEquals("성별은 필수입니다.", violation.getMessage());
    }
}