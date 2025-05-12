package com.spring.seoulmoaapi.domain.memberInteraction.controller;

import com.spring.seoulmoaapi.domain.member.entity.Member;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.ReviewRequestDto;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.ScheduleDto;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.ScheduledEventDto;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.UpdateScheduleRequestDto;
import com.spring.seoulmoaapi.domain.memberInteraction.service.MemberInteractionService;
import com.spring.seoulmoaapi.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interaction")
@Tag(name = "Interaction API", description = "좋아요, 관심사 설정, 리뷰 등의 유저 활동관련 api")
public class MemberInteractionController {

    private final MemberInteractionService memberInteractionService;

    @PostMapping("/event/like/{eventId}")
    @Operation(summary = "문화행사 좋아요 토글", description = "로그인한 사용자가 해당 이벤트에 좋아요 또는 좋아요 취소를 수행합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 처리됨")
    public ResponseEntity<?> handleMemberEvent(@AuthenticationPrincipal Member member,@PathVariable Long eventId){
        memberInteractionService.handleMemberEvent(member.getUserId(), eventId);
        return ResponseEntity.ok(SuccessResponse.success("좋아요 처리 완료"));
    }

    @PostMapping("/event/review/{reviewId}")
    @Operation(summary = "리뷰 작성/수정", description = "로그인한 사용자가 해당 이벤트의 리뷰를 작성하거나 수정합니다. reviewId을 -1로 넣어주시면 작성/reviewId를 넣어주시면 수정")
    @ApiResponse(responseCode = "200", description = "성공적으로 처리됨")
    public ResponseEntity<?> insertReviewEvent(@PathVariable Long reviewId, @Valid @RequestBody ReviewRequestDto dto, @AuthenticationPrincipal Member member){
        if (reviewId == -1) {
            memberInteractionService.createReview(dto, member.getUserId());
            return ResponseEntity.ok(SuccessResponse.success("리뷰 작성 완료"));
        } else {
            memberInteractionService.updateReview(reviewId, dto);
            return ResponseEntity.ok(SuccessResponse.success("리뷰 수정 완료"));
        }
    }

    @PostMapping("/event/review/delete/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "로그인한 사용자가 해당 리뷰를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 처리됨")
    public ResponseEntity<?> deleteReviewEvent(@PathVariable Long reviewId){
        memberInteractionService.deleteReview(reviewId);
        return ResponseEntity.ok(SuccessResponse.success("리뷰 삭제 완료"));
    }

    @GetMapping("/event/reviews/event")
    public ResponseEntity<?> getReviewsByEventId(@RequestParam Long eventId,
                                                 @RequestParam int offset,
                                                 @RequestParam int limit) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        return ResponseEntity.ok(SuccessResponse.success(memberInteractionService.getEventReviews(eventId, pageable)));
    }

    @GetMapping("/event/reviews/user")
    public ResponseEntity<?> getReviewsByUserId(@AuthenticationPrincipal Member member,
                                                @RequestParam int offset,
                                                @RequestParam int limit) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        return ResponseEntity.ok(SuccessResponse.success( memberInteractionService.getUserReviews(member.getUserId(), pageable)));
    }

    @GetMapping("/event/most/liked")
    public ResponseEntity<?> getMostLikedEvents(@AuthenticationPrincipal Member member,
                                                @RequestParam int offset,
                                                @RequestParam int limit) {
        int page = offset / limit;
        Pageable pageable = PageRequest.of(page, limit);
        return ResponseEntity.ok(SuccessResponse.success( memberInteractionService.getMostLikedEvents(member, pageable)));
    }

    // 일정 등록
    @PostMapping("/event/schedule")
    public ResponseEntity<?> registerSchedule(@AuthenticationPrincipal Member member, @RequestBody ScheduleDto dto){
        memberInteractionService.registerSchedule(member,dto);
        return ResponseEntity.ok(SuccessResponse.success());
    }
    @PostMapping("/event/schedule/update")
    public ResponseEntity<?> updateScheduleTime(@AuthenticationPrincipal Member member, @RequestBody UpdateScheduleRequestDto dto){
        memberInteractionService.updateScheduleTime(member,dto.getScheduleTime(),dto.getScheduleId());
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @PostMapping("/event/schedule/delete")
    public ResponseEntity<?> deleteSchedule(@AuthenticationPrincipal Member member, @RequestBody Long scheduleId){
        memberInteractionService.deleteSchedule(member,scheduleId);
        return ResponseEntity.ok(SuccessResponse.success());
    }

    @GetMapping("/event/schedule/list")
    public ResponseEntity<?> getScheduleList(@AuthenticationPrincipal Member member){
        List<ScheduledEventDto> dto = memberInteractionService.getScheduleList(member);
        return ResponseEntity.ok(SuccessResponse.success(dto));
    }
}
