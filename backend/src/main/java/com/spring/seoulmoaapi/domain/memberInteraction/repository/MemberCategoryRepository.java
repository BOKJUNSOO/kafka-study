package com.spring.seoulmoaapi.domain.memberInteraction.repository;

import com.spring.seoulmoaapi.domain.memberInteraction.entity.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
    List<MemberCategory> findAllByMember_UserId(Long memberId);
    // 또는 특정 관심사와 연관된 회원 목록을 조회할 수도 있습니다.
    List<MemberCategory> findByCategory_CategoryId(Long interestId);

    void deleteAllByMember_UserId(Long memberId);
}