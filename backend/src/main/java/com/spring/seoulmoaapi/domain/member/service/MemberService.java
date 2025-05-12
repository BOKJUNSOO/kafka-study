package com.spring.seoulmoaapi.domain.member.service;

import com.spring.seoulmoaapi.domain.event.entity.Category;
import com.spring.seoulmoaapi.domain.member.dto.LoginRequestDto;
import com.spring.seoulmoaapi.domain.member.dto.MemberInfoResponseDto;
import com.spring.seoulmoaapi.domain.member.dto.MemberUpdateDto;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.MemberCategoryDto;
import com.spring.seoulmoaapi.domain.memberInteraction.service.MemberInteractionService;
import com.spring.seoulmoaapi.domain.member.dto.SignUpRequestDto;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import com.spring.seoulmoaapi.domain.member.repository.MemberRepository;
import com.spring.seoulmoaapi.global.common.exception.CustomException;
import com.spring.seoulmoaapi.global.common.response.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberInteractionService memberInteractionService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(SignUpRequestDto dto) {
        // 사용자 중복 체크
        if (memberRepository.existsByUsername(dto.getUsername())) {
            throw new CustomException("이미 사용 중인 아이디입니다.");
        }
        // 비밀번호 인코딩 (한 번만 수행)
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        // Member 엔티티 생성 (기본 역할은 ROLE_USER)
        Member member = Member.of(
                dto.getUsername(),
                encodedPassword,
                dto.getNickname(),
                dto.getAge(),
                dto.getGender().toLowerCase()
        );
        // 저장 후, 회원이 저장된 객체(식별자 포함)를 사용합니다.
        final Member savedMember = memberRepository.save(member);
        // 관심사 처리 로직은 분리된 service로 위임합니다.
        memberInteractionService.addMemberCategory(savedMember, dto.getCategoryIds());
    }

    @Transactional
    public void updateInfo(Long memberId, MemberUpdateDto dto){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException("회원 정보가 존재하지 않습니다"));
        member.updateInfo(dto.getNickname(),dto.getAge(),dto.getGender());
        memberInteractionService.addMemberCategory(member, dto.getInterestIds());
    }

    public MemberInfoResponseDto getMemberInfo(Long userId){
        Member member = memberRepository.findById(userId).orElseThrow(()->new CustomException("회원 정보가 존재하지 않습니다"));
        List<MemberCategoryDto> categoryList = memberInteractionService.getMemberCategory(userId);
        return MemberInfoResponseDto.builder()
                .userId(member.getUserId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .age(member.getAge())
                .gender(member.getGender())
                .memberCategories(categoryList)
                .build();
    }

    public MemberInfoResponseDto login( LoginRequestDto loginRequestDto,
                                        HttpServletRequest request){
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                );
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );
        Member member = (Member) authentication.getPrincipal();
        List<MemberCategoryDto> memberCategories = memberInteractionService.getMemberCategory(member.getUserId());
        List<Long> memberCategoryIds = memberCategories.stream().map(MemberCategoryDto::getCategoryId).toList();
        return  MemberInfoResponseDto.builder()
                .userId(member.getUserId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .age(member.getAge())
                .gender(member.getGender())
                .memberCategories(memberCategories)
                .memberCategoryIds(memberCategoryIds)
                .build();
    }

}
