package com.spring.seoulmoaapi.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.seoulmoaapi.domain.member.controller.MemberController;
import com.spring.seoulmoaapi.domain.member.dto.SignUpRequestDto;
import com.spring.seoulmoaapi.domain.member.service.MemberService;
import com.spring.seoulmoaapi.global.common.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false) // 보안 필터 비활성화
@WebMvcTest(MemberController.class)
@Import(GlobalExceptionHandler.class)       // 전역 예외 핸들러를 테스트 컨텍스트에 포함
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    // GlobalExceptionHandler 또는 JPA Auditing 등에서 사용하는 JPA 관련 빈 모의 처리
    @MockBean
    private JpaMetamodelMappingContext jpaMappingContext;

    @Test
    @DisplayName("회원가입 성공 테스트 (관심사 포함)")
    void register_success_withInterests() throws Exception {
        // given: 모든 필드와 관심사 ID들을 포함한 DTO 생성
        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setUsername("testuser");
        dto.setPassword("Password123!");
        dto.setNickname("Tester");
        dto.setAge(25);
        dto.setGender("남성");
        dto.setCategoryIds(List.of(1L, 3L));

        // when: 회원가입 API 호출
        mockMvc.perform(post("/members/register")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)    // JSON 응답 기대
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // then: MemberService.register 메서드 호출 시 DTO에 관심사 목록이 포함되어 있는지 검증
        ArgumentCaptor<SignUpRequestDto> captor = ArgumentCaptor.forClass(SignUpRequestDto.class);
        verify(memberService).register(captor.capture());
        assertEquals(List.of(1L, 3L), captor.getValue().getCategoryIds());
    }

    @Test
    @DisplayName("회원가입 유효성 검사 실패 테스트 - 빈 username (관심사 포함)")
    void register_validation_fail_blankUsername_withInterests() throws Exception {
        // given: username이 빈 값인 DTO
        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setUsername("");  // 유효성 위반
        dto.setPassword("Password123!");
        dto.setNickname("Tester");
        dto.setAge(25);
        dto.setGender("남성");
        dto.setCategoryIds(List.of(2L));

        // when: API 호출 시 400 Bad Request 응답을 기대
        mockMvc.perform(post("/members/register")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 중복 아이디 실패 테스트 (관심사 포함)")
    void register_duplicate_fail_withInterests() throws Exception {
        // given: 중복 아이디로 회원가입 요청
        SignUpRequestDto dto = new SignUpRequestDto();
        dto.setUsername("duplicateUser");
        dto.setPassword("Password123!");
        dto.setNickname("Tester");
        dto.setAge(25);
        dto.setGender("남성");
        dto.setCategoryIds(List.of(4L, 5L));

        // 서비스 호출 시 IllegalArgumentException 발생하도록 설정
        doThrow(new IllegalArgumentException("이미 사용 중인 아이디입니다."))
                .when(memberService).register(any(SignUpRequestDto.class));

        // when: API 호출 시 400 Bad Request 응답과 에러 메시지 검증
        mockMvc.perform(post("/members/register")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이미 사용 중인 아이디입니다."));
    }
}