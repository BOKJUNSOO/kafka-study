package com.spring.seoulmoaapi.domain.memberInteraction.repository;

import com.spring.seoulmoaapi.domain.memberInteraction.dto.ReviewEventDto;
import com.spring.seoulmoaapi.domain.memberInteraction.dto.ReviewUserDto;
import com.spring.seoulmoaapi.domain.memberInteraction.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT new com.spring.seoulmoaapi.domain.memberInteraction.dto.ReviewEventDto(r.reviewId, r.userId, r.eventId, m.nickname, r.createDate, r.content) " +
           "FROM Review r " +
           "INNER JOIN Member m ON r.userId = m.userId " +
           // "INNER JOIN datawarehouse. "
           "WHERE r.eventId = :eventId " +
           "ORDER BY r.createDate DESC")
    Page<ReviewEventDto> findEventReviews(@Param("eventId") Long eventId, Pageable pageable);

    int countByEventId(Long eventId);

    @Query("SELECT new com.spring.seoulmoaapi.domain.memberInteraction.dto.ReviewUserDto(r.reviewId, r.userId, r.eventId, e.title, r.createDate, r.content, e.imageUrl) " +
            "FROM Review r " +
            "INNER JOIN Event e ON r.eventId = e.eventId " +
            "WHERE r.userId = :userId " +
            "ORDER BY r.createDate DESC")
    Page<ReviewUserDto> findUserReviews(@Param("userId") Long userId, Pageable pageable);

    int countByUserId(Long eventId);

    @Query(value = """
    SELECT *
    FROM (
        SELECT DISTINCT ON (r.event_id)
            r.review_id,
            r.create_date,
            r.update_date,
            r.content,
            r.event_id,
            r.user_id
        FROM datawarehouse.reviews r
        ORDER BY r.event_id, r.create_date DESC
    ) AS sub
    ORDER BY sub.create_date DESC
    OFFSET :offset
    LIMIT :limit
    """, nativeQuery = true)
    List<Review> findDistinctReviewsWithPaging(
            @Param("offset") int offset,
            @Param("limit") int limit
    );


    @Query(value = """
    SELECT COUNT(*)
    FROM (
        SELECT DISTINCT ON (r.event_id)
            r.review_id
        FROM datawarehouse.reviews r
        ORDER BY r.event_id, r.create_date DESC
    ) AS sub
    """, nativeQuery = true)
    Long countDistinctReviews();


    boolean existsReviewByUserIdAndEventId(Long userId, Long eventId);
}
