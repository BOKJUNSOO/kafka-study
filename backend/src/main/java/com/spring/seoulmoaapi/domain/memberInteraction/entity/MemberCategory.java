package com.spring.seoulmoaapi.domain.memberInteraction.entity;

import com.spring.seoulmoaapi.domain.TimeStamp;
import com.spring.seoulmoaapi.domain.event.entity.Category;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
// 멤버 관심사 관련 테이블
public class MemberCategory extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원(Member)과의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    // 관심사(Interest)와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public MemberCategory(Member member, Category category) {
        this.member = member;
        this.category = category;
    }
}