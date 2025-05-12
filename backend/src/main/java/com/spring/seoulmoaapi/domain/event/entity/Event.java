package com.spring.seoulmoaapi.domain.event.entity;

import com.spring.seoulmoaapi.global.common.batch.dto.EventSync;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "event")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(name = "title")
    private String title;

    @Column(name = "category_name")
    private String categoryName;

    @JoinColumn(name = "category_id",nullable = true)
    @ManyToOne
    private Category categoryId;

    @Column(name = "gu")
    private String gu;

    @Column(name = "location")
    private String location;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "fee")
    private String fee;

    @Column(name = "is_free")
    private Boolean isFree;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "detail_url", columnDefinition = "text")
    private String detailUrl;

    @Column(name = "target_user")
    private String targetUser;

    @Column(name = "event_description", columnDefinition = "text")
    private String eventDescription;

    public void updateFromSync(EventSync sync) {
        this.title = sync.getTitle();
        this.categoryName = sync.getCategoryName();
        this.gu = sync.getGu();
        this.location = sync.getLocation();
        this.startDate = sync.getStartDate();
        this.endDate = sync.getEndDate();
        this.fee = sync.getFee();
        this.isFree = sync.getIsFree();
        this.latitude = sync.getLatitude();
        this.longitude = sync.getLongitude();
        this.homepage = sync.getHomepage();
        this.imageUrl = sync.getImageUrl();
        this.targetUser = sync.getTargetUser();
//        this.eventDescription = sync.getEventDescription();
    }
}

