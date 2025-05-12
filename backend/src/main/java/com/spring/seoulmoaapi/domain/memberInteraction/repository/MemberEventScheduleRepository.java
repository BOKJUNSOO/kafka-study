package com.spring.seoulmoaapi.domain.memberInteraction.repository;

import com.spring.seoulmoaapi.domain.member.entity.Member;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberEventSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberEventScheduleRepository extends JpaRepository<MemberEventSchedule,Long> {
    Optional<MemberEventSchedule> findByMember_UserIdAndEvent_EventId(Long userId, Long eventId);
    void deleteByMember_UserIdAndEvent_EventId(Long userId, Long eventId);

    List<MemberEventSchedule> findAllByMember(Member member);

    boolean existsByMemberAndScheduleTime(Member member, LocalDateTime schedule);
}
