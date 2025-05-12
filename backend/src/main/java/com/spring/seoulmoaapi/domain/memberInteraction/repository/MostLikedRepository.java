package com.spring.seoulmoaapi.domain.memberInteraction.repository;

import com.spring.seoulmoaapi.domain.event.entity.Event;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.MostLikedDto;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.ReviewEventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MostLikedRepository extends JpaRepository<Event, Long> {
    @Query("""
            SELECT new com.spring.seoulmoaapi.domain.memberInteraction.dto.MostLikedDto(
                    e.eventId,
                    e.title,
                    e.startDate,
                    e.endDate,
                    e.location,
                    e.categoryId.categoryId,
                    e.categoryName,
                    e.gu,
                    e.imageUrl,
                    COUNT(me.id),
                    CASE
                        WHEN :userId IS NULL THEN 'U'
                        WHEN EXISTS (
                            SELECT 1 FROM MemberEvent me2
                            WHERE me2.event.eventId = e.eventId AND me2.member.userId = :userId
                        ) THEN 'Y'
                        ELSE 'N'
                    END
                )
                FROM Event e
                LEFT JOIN MemberEvent me ON e.eventId = me.event.eventId
                WHERE e.endDate >= CURRENT_DATE
                GROUP BY e.eventId,
                         e.title,
                         e.startDate,
                         e.endDate,
                         e.location,
                         e.categoryId,
                         e.categoryName,
                         e.gu,
                         e.imageUrl
                ORDER BY COUNT(me.id) DESC,
                         e.eventId ASC
            """)
    Page<MostLikedDto> findMostLikedEvents(@Param("userId") Long userId, Pageable pageable);
}
