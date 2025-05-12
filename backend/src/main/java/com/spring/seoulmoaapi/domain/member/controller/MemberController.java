package com.spring.seoulmoaapi.domain.member.controller;

import com.spring.seoulmoaapi.domain.member.dto.LoginRequestDto;
import com.spring.seoulmoaapi.domain.member.dto.MemberUpdateDto;
import com.spring.seoulmoaapi.domain.member.dto.SignUpRequestDto;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import com.spring.seoulmoaapi.domain.member.service.MemberService;
import com.spring.seoulmoaapi.global.common.response.ErrorResponse;
import com.spring.seoulmoaapi.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
@Tag(name = "Member API", description = "회원가입 및 회원정보 관리를 위한 API")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;


    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록하며, 선택적으로 관심사(interestIds)도 함께 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청이 성공했습니다.",
                            content = @Content(schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(value = "{\"status\": \"SUCCESS\", \"message\": \"요청이 성공했습니다.\", \"data\": null}"))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    examples = {
                                            @ExampleObject(name = "중복 계정", value = "{\"status\": \"ERROR\", \"message\": \"이미 사용 중인 아이디입니다.\", \"data\": null}"),
                                            @ExampleObject(name = "비밀번호 실패", value = "{\"status\": \"ERROR\", \"message\": \"비밀번호는 8자 이상이며, 영문자, 숫자, 특수문자를 포함해야 합니다.\", \"data\": null}")
                                    }))
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.register(signUpRequestDto);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @Operation(summary = "회원 정보 수정", description = "로그인한 사용자의 닉네임, 성별, 나이 등을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class)))
    @PostMapping("/update/me")
    public ResponseEntity<?> updateMemberInfo(@AuthenticationPrincipal Member userDetails,
                                              @Valid @RequestBody MemberUpdateDto memberUpdateDto) {
        log.info("update call");
        memberService.updateInfo(userDetails.getUserId(), memberUpdateDto);
        return ResponseEntity.ok(SuccessResponse.success("회원수정 성공"));
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인합니다. 세션 기반 인증 사용.")
    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                   HttpServletRequest request) {
        return ResponseEntity.ok(SuccessResponse.success(memberService.login(loginRequestDto,request)));
    }

    @Operation(summary = "로그아웃", description = "현재 세션을 종료하여 로그아웃 처리합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class)))
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(SuccessResponse.success("로그아웃 성공"));
    }
    @Operation(
            summary = "회원 정보 확인 테스트",
            description = "현재 로그인된 사용자의 정보를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원 정보 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(
                                            name = "회원 정보 조회 성공 예시",
                                            value = """
                                                    {
                                                      "status": "SUCCESS",
                                                      "message": "요청이 성공했습니다.",
                                                      "data": {
                                                        "userId": 2,
                                                        "username": "testUser123",
                                                        "nickname": "개발꿈나무-수정3",
                                                        "age": 32,
                                                        "gender": "M",
                                                        "memberCategories": [
                                                          {
                                                            "categoryId": 1,
                                                            "categoryName": "기타"
                                                          },
                                                          {
                                                            "categoryId": 2,
                                                            "categoryName": "교육/체험"
                                                          },
                                                          {
                                                            "categoryId": 5,
                                                            "categoryName": "영화"
                                                          }
                                                        ]
                                                      }
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/detail/me")
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal Member userDetails) {
        Long loginUserId = userDetails.getUserId();
        return ResponseEntity.ok(SuccessResponse.success(memberService.getMemberInfo(loginUserId)));
    }
}
