package com.spring.seoulmoaapi.domain.memberInteraction.entity;

import com.spring.seoulmoaapi.domain.TimeStamp;
import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
// 문화행사 좋아요 기능 관련 테이블
public class MemberEvent extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 좋아요를 누른 회원
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    // 좋아요 대상 행사
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id")
    private Event event;
}