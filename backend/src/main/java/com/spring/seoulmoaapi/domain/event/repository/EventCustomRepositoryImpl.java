package com.spring.seoulmoaapi.domain.event.repository;

import com.spring.seoulmoaapi.domain.event.dto.EventDto;
import com.spring.seoulmoaapi.domain.event.dto.EventResponseDto;
import com.spring.seoulmoaapi.domain.event.dto.EventSearchDto;
import com.spring.seoulmoaapi.domain.event.entity.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
@Slf4j
public class EventCustomRepositoryImpl implements EventCustomRepository {

    private final EntityManager em;

    @Override
    public EventResponseDto search(EventSearchDto dto, Long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 메인 쿼리
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);

        List<Predicate> predicates = buildPredicates(cb, root, dto);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
//        cq.orderBy(cb.asc(root.get("startDate")));
        cq.orderBy(cb.asc(root.get("endDate")), cb.asc(root.get("eventId")));

        TypedQuery<Event> query = em.createQuery(cq);
        query.setFirstResult(dto.getOffset()*dto.getLimit());
        query.setMaxResults(dto.getLimit());
        List<Event> events = query.getResultList();

        List<Long> eventIds = events.stream().map(Event::getEventId).toList();

        // 좋아요 수 Map<eventId, count> 조회
        Map<Long, Integer> likeCountMap;
        if (!eventIds.isEmpty()) {
            List<Object[]> likeCountsRaw = em.createQuery(
                            "select me.event.eventId, count(me.id) from MemberEvent me " +
                                    "where me.event.eventId in :eventIds group by me.event.eventId", Object[].class)
                    .setParameter("eventIds", eventIds)
                    .getResultList();

            likeCountMap = likeCountsRaw.stream()
                    .collect(Collectors.toMap(
                            r -> (Long) r[0],
                            r -> ((Long) r[1]).intValue()
                    ));
        } else {
            likeCountMap = new HashMap<>();
        }

        List<EventDto> eventList;

        // 로그인 유저가 좋아요한 이벤트 ID 목록 조회
        if (userId != null) {
            List<Long> likedEventIds = em.createQuery(
                            "select me.event.eventId from MemberEvent me where me.member.userId = :userId", Long.class)
                    .setParameter("userId", userId)
                    .getResultList();

            eventList = events.stream()
                    .map(e -> {
                        boolean isLiked = likedEventIds.contains(e.getEventId());
                        int likeCount = likeCountMap.getOrDefault(e.getEventId(), 0);
                        return EventDto.from(e, isLiked, likeCount);
                    })
                    .toList();
        } else {
            eventList = events.stream()
                    .map(e -> EventDto.from(e, null, likeCountMap.getOrDefault(e.getEventId(), 0)))
                    .toList();
        }

        // 카운트 쿼리
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Event> countRoot = countQuery.from(Event.class);
        countQuery.select(cb.count(countRoot));
        List<Predicate> countPredicates = buildPredicates(cb, countRoot, dto);
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        Integer total = em.createQuery(countQuery).getSingleResult().intValue();
        Integer limit = dto.getLimit();
        Integer totalPage = total / limit + (total % limit == 0 ? 0 : 1);
        return EventResponseDto.builder()
                .eventList(eventList)
                .totalCount(total)
                .offset(dto.getOffset())
                .limit(dto.getLimit())
                .totalPages(totalPage)
                .build();
    }

    // 조건 검색 쿼리
    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Event> root, EventSearchDto dto) {
        List<Predicate> predicates = new ArrayList<>();
        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
//            predicates.add(cb.like(root.get("title"), "%" + dto.getTitle() + "%"));
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + dto.getTitle().toLowerCase() + "%"));
        }
        if (dto.getGu() != null && !dto.getGu().isEmpty()) {
//            predicates.add(cb.equal(root.get("gu"), dto.getGu()));
            predicates.add(root.get("gu").in(dto.getGu()));
        }
        if (dto.getIsFree() != null) {
            predicates.add(cb.equal(root.get("isFree"), dto.getIsFree()));
        }
        if (dto.getCategoryId() != null && !dto.getCategoryId().isEmpty()) {
            predicates.add(root.get("categoryId").get("categoryId").in(dto.getCategoryId()));
        }
        if (dto.getDescriptionKeyword() != null) {
            predicates.add(cb.like(root.get("eventDescription"), "%" + dto.getDescriptionKeyword() + "%"));
        }
        if (dto.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), dto.getStartDate()));
        }
        if (dto.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), dto.getEndDate()));
        }
        if (dto.getIsOpen() != null && dto.getIsOpen()) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("endDate"), LocalDateTime.now()));
        }
        return predicates;
    }
}
