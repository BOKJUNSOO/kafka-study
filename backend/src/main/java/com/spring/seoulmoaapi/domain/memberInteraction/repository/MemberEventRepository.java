package com.spring.seoulmoaapi.domain.memberInteraction.repository;

import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberEventRepository extends JpaRepository<MemberEvent, Long> {
    Optional<MemberEvent> findByMember_UserIdAndEvent_EventId(Long userId, Long eventId);

    Page<MemberEvent> findAllByMember_UserIdOrderByCreateDateDesc(Long userId, Pageable pageable);

    int countByMember_UserId(Long userId);

    @Query("select me.event.eventId, count(me.id) from MemberEvent me where me.event.eventId in :eventIds group by me.event.eventId")
    List<Object[]> findLikeCountsByEventIds(@Param("eventIds") List<Long> eventIds);


    // 좋아요한 이벤트 ID 목록
    @Query("""
                select me.event.eventId
                from MemberEvent me
                where me.member.userId = :userId
                  and me.event.eventId in :eventIds
            """)
    List<Long> findLikedEventIdsByUserId(@Param("userId") Long userId, @Param("eventIds") List<Long> eventIds);
}
