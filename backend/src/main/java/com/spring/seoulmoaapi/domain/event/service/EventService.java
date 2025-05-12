package com.spring.seoulmoaapi.domain.event.service;

import com.spring.seoulmoaapi.domain.event.dto.EventDto;
import com.spring.seoulmoaapi.domain.event.dto.EventResponseDto;
import com.spring.seoulmoaapi.domain.event.dto.EventSearchDto;
import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.event.repository.EventRepository;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.ReviewResponseDto;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberEvent;
import com.spring.seoulmoaapi.domain.memberInteraction.repository.MemberEventRepository;
import com.spring.seoulmoaapi.domain.memberInteraction.service.MemberInteractionService;
import com.spring.seoulmoaapi.domain.subway.dto.SubwayStationDto;
import com.spring.seoulmoaapi.domain.subway.service.SubwayStationService;
import com.spring.seoulmoaapi.global.common.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    private final MemberEventRepository memberEventRepository;
    private final SubwayStationService subwayStationService;
    private final MemberInteractionService memberInteractionService;


    // 문화행사 리스트 조건 검색
    public EventResponseDto searchEvents(EventSearchDto dto, Long userId) {
        return eventRepository.search(dto, userId);
    }

    public EventDto getEventById(Long eventId, Member userdetails) {
        Event found = eventRepository.findById(eventId).orElseThrow(() -> new CustomException("존재하지 않은 행사 정보입니다"));
        EventDto dto = null;
        int likeCount = memberEventRepository.findLikeCountsByEventIds(List.of(eventId)).size();
        if (userdetails != null) {
            Boolean isLiked = memberEventRepository.findByMember_UserIdAndEvent_EventId(userdetails.getUserId(), eventId).isPresent();
            dto = EventDto.from(found, isLiked, likeCount);
        } else {
            dto = EventDto.from(found, likeCount);
        }
        SubwayStationDto nearestStation = subwayStationService.findNearestStation(found.getLatitude(), found.getLongitude(), 50000);
        dto.setNearestStation(nearestStation);
        return dto;
    }

    public EventResponseDto getLikedEvents(Member userDetails, Pageable pageable) {
        Long userId = userDetails.getUserId();

        // 좋아요 누른 이벤트 조회 (페이징 포함)
        Page<MemberEvent> memberEvents = memberEventRepository.findAllByMember_UserIdOrderByCreateDateDesc(userId, pageable);

        // 해당 이벤트 ID 목록
        List<Event> events = memberEvents.getContent().stream()
                .map(MemberEvent::getEvent)
                .toList();

        List<Long> eventIds = events.stream()
                .map(Event::getEventId)
                .toList();
        // 각 이벤트별 좋아요 수 조회
        List<Object[]> likeCountsRaw = memberEventRepository.findLikeCountsByEventIds(eventIds);
        // → select me.event.eventId, count(me.id) from MemberEvent me where me.event.eventId in :eventIds group by me.event.eventId

        Map<Long, Integer> likeCountMap = likeCountsRaw.stream()
                .collect(Collectors.toMap(
                        r -> (Long) r[0],
                        r -> ((Long) r[1]).intValue()
                ));
        // DTO 변환
        List<EventDto> eventList = events.stream()
                .map(event -> EventDto.from(event, true, likeCountMap.getOrDefault(event.getEventId(), 0)))
                .toList();

        // 전체 개수 조회 (페이징 totalCount용)
        int totalCount = memberEventRepository.countByMember_UserId(userId);
        int limit = pageable.getPageSize();
        Integer totalPage = (int) Math.ceil((double) totalCount / limit);
        return EventResponseDto.builder()
                .eventList(eventList)
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .totalCount(totalCount)
                .totalPages(totalPage)
                .build();
    }
//
//    public EventResponseDto getRecentlyReviewedEvents(Member member, Pageable pageable) {
//        LocalDateTime start = LocalDateTime.now();
//        boolean isLogined;
//        Long userId;
//        if (member != null) {
//            userId = member.getUserId();
//            isLogined = true;
//        } else {
//            isLogined = false;
//            userId = 0L;
//        }
//        List<ReviewResponseDto> reviews = memberInteractionService.getRecentlyReview(pageable);
//        Integer totalCount = memberInteractionService.getRecentlyReviewEventsTotalCount();
//        List<Long> eventIds = reviews.stream().map(ReviewResponseDto::getEventId).toList();
//        List<Event> events = eventRepository.findAllById(eventIds);
//        // 가져온 이벤트를 ID로 맵핑 (ID -> Event)
//        Map<Long, Event> eventMap = events.stream()
//                .collect(Collectors.toMap(Event::getEventId, e -> e));
//        // eventIds 순서대로 다시 정렬
//        List<Event> sortedEvents = eventIds.stream()
//                .map(eventMap::get)
//                .collect(Collectors.toList());
//
//        log.info("sortedEvents : {}", sortedEvents.stream().map(Event::getEventId).toList());
//
//
//        // 각 이벤트별 좋아요 수 조회
//        List<Object[]> likeCountsRaw = memberEventRepository.findLikeCountsByEventIds(eventIds);
//        // → select me.event.eventId, count(me.id) from MemberEvent me where me.event.eventId in :eventIds group by me.event.eventId
//        LocalDateTime time2 = LocalDateTime.now();
//        Map<Long, Integer> likeCountMap = likeCountsRaw.stream()
//                .collect(Collectors.toMap(
//                        r -> (Long) r[0],
//                        r -> ((Long) r[1]).intValue()
//                ));
//        // DTO 변환
//        List<EventDto> eventList = sortedEvents.stream()
//                .map(event -> {
//                    Long eventId = event.getEventId();
//                    EventDto eventDto;
//                    if (isLogined) {
//                        Optional<MemberEvent> like = memberEventRepository.findByMember_UserIdAndEvent_EventId(userId, eventId);
//                        eventDto = EventDto.from(event, like.isPresent(), likeCountMap.getOrDefault(event.getEventId(), 0));
//                    } else {
//                        eventDto = EventDto.from(event, false, likeCountMap.getOrDefault(event.getEventId(), 0));
//                    }
//                    Pageable pageable2 = PageRequest.of(0, 1);
////                    eventDto.setRecentlyReview(memberInteractionService.getEventReviews(eventId, pageable2).getReviewList().get(0));
//                    return eventDto;
//                })
//                .toList();
//        int limit = pageable.getPageSize();
//        Integer totalPage = (int) Math.ceil((double) totalCount / limit);
//        LocalDateTime time3 = LocalDateTime.now();
//        log.info("time1 : {}", time2.getSecond()-start.getSecond());
//        log.info("time2 : {}", time3.getSecond() - time2.getSecond());
//        return EventResponseDto.builder()
//                .eventList(eventList)
//                .totalCount(totalCount)
//                .offset(pageable.getPageNumber())
//                .limit(pageable.getPageSize())
//                .totalPages(totalPage)
//                .build();
//    }

    @Transactional()
    public EventResponseDto getRecentlyReviewedEvents(Member member, Pageable pageable) {
        long start = System.currentTimeMillis();

        boolean isLogined = (member != null);
        Long userId = isLogined ? member.getUserId() : 0L;

        // 최근 리뷰 가져오기 (페이징)
        List<ReviewResponseDto> reviews = memberInteractionService.getRecentlyReview(pageable);
        Integer totalCount = memberInteractionService.getRecentlyReviewEventsTotalCount();

        // 리뷰에 연결된 이벤트 ID 추출
        List<Long> eventIds = reviews.stream()
                .map(ReviewResponseDto::getEventId)
                .distinct()
                .toList();

        // 이벤트 ID → 이벤트 객체 매핑
        List<Event> events = eventRepository.findAllById(eventIds);
        Map<Long, Event> eventMap = events.stream()
                .collect(Collectors.toMap(Event::getEventId, e -> e));

        // 원래 순서대로 정렬된 이벤트 리스트
        List<Event> sortedEvents = eventIds.stream()
                .map(eventMap::get)
                .filter(Objects::nonNull)
                .toList();

        // 좋아요 수 집계
        Map<Long, Integer> likeCountMap = memberEventRepository.findLikeCountsByEventIds(eventIds)
                .stream()
                .collect(Collectors.toMap(
                        r -> (Long) r[0],
                        r -> ((Long) r[1]).intValue()
                ));

        // 로그인된 유저가 좋아요한 이벤트 ID 전체 조회 (한 번에 처리)
        Set<Long> likedEventIds = isLogined
                ? new HashSet<>(memberEventRepository.findLikedEventIdsByUserId(userId, eventIds))
                : Collections.emptySet();

        // DTO 변환
        List<EventDto> eventList = sortedEvents.stream()
                .map(event -> EventDto.from(
                        event,
                        isLogined && likedEventIds.contains(event.getEventId()),
                        likeCountMap.getOrDefault(event.getEventId(), 0)
                ))
                .toList();

        int totalPages = (int) Math.ceil((double) totalCount / pageable.getPageSize());

        log.info("getRecentlyReviewedEvents 실행시간: {}ms", System.currentTimeMillis() - start);

        return EventResponseDto.builder()
                .eventList(eventList)
                .totalCount(totalCount)
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .totalPages(totalPages)
                .build();
    }
}
