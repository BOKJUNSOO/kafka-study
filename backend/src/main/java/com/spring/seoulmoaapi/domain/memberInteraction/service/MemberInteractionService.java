package com.spring.seoulmoaapi.domain.memberInteraction.service;

import com.spring.seoulmoaapi.domain.event.dto.EventDto;
import com.spring.seoulmoaapi.domain.event.entity.Category;
import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.event.repository.EventRepository;
import com.spring.seoulmoaapi.domain.member.repository.MemberRepository;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.*;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberCategory;
import com.spring.seoulmoaapi.domain.event.repository.CategoryRepository;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberEvent;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberEventSchedule;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.Review;
import com.spring.seoulmoaapi.domain.memberInteraction.repository.*;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import com.spring.seoulmoaapi.global.common.exception.CustomException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberInteractionService {

    private final CategoryRepository categoryRepository;
    private final MemberCategoryRepository memberCategoryRepository;
    private final MemberEventRepository memberEventRepository;
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final ReviewRepository reviewRepository;
    private final MostLikedRepository mostLikedRepository;
    private final MemberEventScheduleRepository memberEventScheduleRepository;

    @Transactional
    public void addMemberCategory(Member member, List<Long> categoryIds) {
        memberCategoryRepository.deleteAllByMember_UserId(member.getUserId());
        if (categoryIds != null && !categoryIds.isEmpty()) {
            categoryIds.forEach(categoryId -> {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 관심사 ID입니다: " + categoryId));

                MemberCategory memberInterest = MemberCategory.builder()
                        .member(member)
                        .category(category)
                        .build();
                memberCategoryRepository.save(memberInterest);
            });
        }
    }
    public List<MemberCategoryDto> getMemberCategory(Long memberId) {
        List<MemberCategoryDto> result = memberCategoryRepository.findAllByMember_UserId(memberId)
                .stream().map(MemberCategoryDto::from)
                .toList();
        return result;
    }
    @Transactional
    public void handleMemberEvent(Long memberId, Long eventId) {
        Optional<MemberEvent> like = memberEventRepository.findByMember_UserIdAndEvent_EventId(memberId, eventId);
        if (like.isPresent()) {
            memberEventRepository.delete(like.get()); // 좋아요 취소
        }else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CustomException("회원이 존재하지 않습니다."));
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new CustomException("행사가 존재하지 않습니다."));
            memberEventRepository.save(MemberEvent.builder()
                    .member(member)
                    .event(event)
                    .build());
        }
    }
    @Transactional
    // review 작성 수정 조회 삭제
    public ReviewResponseDto createReview(ReviewRequestDto dto, Long userId) {
        Review review = Review.builder()
                .userId(userId)
                .eventId(dto.getEventId())
                .content(dto.getContent())
                .build();

        return toDto(reviewRepository.save(review));
    }
    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
        review.update(dto.getContent());
        return toDto(reviewRepository.save(review));
    }
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public ReviewEventResponseDto getEventReviews(Long eventId, Pageable pageable) {
        List<ReviewEventDto> reviewList = reviewRepository.findEventReviews(eventId, pageable).getContent();

        int totalCount = reviewRepository.countByEventId(eventId);

        return ReviewEventResponseDto.builder()
                .reviewList(reviewList)
                .offset((int) pageable.getOffset())
                .limit(pageable.getPageSize())
                .totalCount(totalCount)
                .build();
    }

    public ReviewUserResponseDto getUserReviews(Long userId, Pageable pageable) {
        List<ReviewUserDto> reviewList = reviewRepository.findUserReviews(userId, pageable).getContent();

        int totalCount = reviewRepository.countByUserId(userId);

        return ReviewUserResponseDto.builder()
                .reviewList(reviewList)
                .offset((int) pageable.getOffset())
                .limit(pageable.getPageSize())
                .totalCount(totalCount)
                .build();
    }

    private ReviewResponseDto toDto(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUserId())
                .eventId(review.getEventId())
                .content(review.getContent())
                .build();
    }

    public List<ReviewResponseDto> getRecentlyReview(Pageable pageable){
        Integer offset = pageable.getPageNumber();
        Integer limit = pageable.getPageSize();
//        List<Review> reviews = reviewRepository.findDistinctReviewsWithPaging(pageable.getPageNumber(), pageable.getPageSize());
        List<Review> reviews = reviewRepository.findDistinctReviewsWithPaging(offset*limit, limit);
        log.info("reviews : {}", reviews.toString());
        return reviews.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Integer getRecentlyReviewEventsTotalCount(){
        Integer totalElement = reviewRepository.countDistinctReviews().intValue();
        return totalElement;
    }

    public MostLikedResponseDto getMostLikedEvents(Member member, Pageable pageable) {
        List<MostLikedDto> mostLikedList = mostLikedRepository.findMostLikedEvents(Optional.ofNullable(member).map(Member::getUserId).orElse(null), pageable).getContent();

        int totalCount = eventRepository.findAll().size();

        return MostLikedResponseDto.builder()
                .mostLikedList(mostLikedList)
                .offset((int) pageable.getOffset())
                .limit(pageable.getPageSize())
                .totalCount(totalCount)
                .build();
    }

    @Transactional
    public void registerSchedule(Member member, ScheduleDto dto){
        MemberEventSchedule newEntity = new MemberEventSchedule();
        newEntity.setMember(member);
        Event event = eventRepository.findById(dto.getEventId()).orElseThrow(()->new CustomException("해당 행사 정보가 없습니다"));
        LocalDateTime endDate = event.getEndDate();
        LocalDateTime startDate = event.getStartDate();
        LocalDateTime scheduleTime = dto.getScheduleTime();
        if(scheduleTime.isAfter(endDate.plusDays(1))) {
            throw new CustomException("행사 종료 일정 이후로 예약할 수 없습니다");
        }else if(scheduleTime.isBefore(startDate)) {
            throw new CustomException("행사 시작전 날짜로 예약할 수 없습니다");
        }
        // ✅ 중복 스케줄 체크
        Optional<MemberEventSchedule> exist = memberEventScheduleRepository.findByMember_UserIdAndEvent_EventId(member.getUserId(), event.getEventId());
        if (exist.isPresent()) {
            LocalDate existDate = exist.get().getScheduleTime().toLocalDate();
            if(existDate==scheduleTime.toLocalDate()){
                return;
            }
        }
        newEntity.setEvent(event);
        newEntity.setScheduleTime(dto.getScheduleTime());
        memberEventScheduleRepository.save(newEntity);
    }
    @Transactional(readOnly=true)
    public void deleteSchedule(Member member, Long scheduleId) {
        MemberEventSchedule found = memberEventScheduleRepository.findById(scheduleId).orElseThrow(()->new CustomException("해당 일정 정보가 존재하지 않습니다"));
        if(found.getMember().getUserId().equals(member.getUserId())){
            memberEventScheduleRepository.delete(found);
        }else{
            throw new CustomException("타인의 일정을 삭제할수없습니다");
        }
    }

    public List<ScheduledEventDto> getScheduleList(Member member) {
        LocalDateTime now = LocalDateTime.now();
        return memberEventScheduleRepository.findAllByMember(member).stream()
                .map(schedule -> {
                    Event event = schedule.getEvent();
                    ScheduledEventDto dto = new ScheduledEventDto();
                    dto.setScheduleId(schedule.getScheduleId());
                    dto.setScheduleTime(schedule.getScheduleTime());
                    EventDto eventDto = EventDto.from(event, null, -1);
                    boolean hasReview = reviewRepository.existsReviewByUserIdAndEventId(member.getUserId(),event.getEventId());
                    eventDto.setHasReview(hasReview);
                    dto.setEvent(eventDto);
                    dto.setPast(now.isAfter(event.getEndDate()));
                    dto.setPastScheduled(now.isAfter(schedule.getScheduleTime()));
                    return dto;
                })
                .sorted(Comparator.comparing(dto -> Duration.between(now, dto.getScheduleTime()).toMillis()))
                .toList();
    }

    @Transactional
    public void updateScheduleTime(Member member, LocalDateTime scheduleTime, Long scheduleId) {
        MemberEventSchedule schedule = memberEventScheduleRepository.findById(scheduleId).orElseThrow(()->new CustomException("해당 일정 정보가 없습니다"));
        Event event = schedule.getEvent();
        LocalDateTime startDate = event.getStartDate();
        LocalDateTime endDate = event.getEndDate();
        if(scheduleTime.isAfter(endDate)||scheduleTime.isBefore(startDate)) {
            throw new CustomException("해당 행사기간 범위에서 벗어난 일정입니다");
        }
        if(schedule.getMember().getUserId().equals(member.getUserId())){
            schedule.setScheduleTime(scheduleTime);
        }else{
            throw new CustomException("타인의 일정을 수정할 수 없습니다.");
        }
    }
}