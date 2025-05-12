package com.spring.seoulmoaapi.domain.memberInteraction.entity;

import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_event_schedule")
@Data
public class MemberEventSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private LocalDateTime scheduleTime;

}
