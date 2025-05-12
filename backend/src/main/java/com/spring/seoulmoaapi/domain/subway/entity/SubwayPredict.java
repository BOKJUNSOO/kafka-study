package com.spring.seoulmoaapi.domain.subway.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subway_predict", schema = "datawarehouse")
public class SubwayPredict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_number")
    private Integer rowNumber;  // 주의: 이 컬럼이 진짜 PK처럼 쓰이는지 확인 필요!

    @Column(name = "name")
    private String name; // 역 이름

    @Column(name = "service_date")
    private LocalDateTime serviceDate; // 예측 날짜

    @Column(name = "hour")
    private Integer hour; // 시간대 (0~23)

    @Column(name = "predicted_total")
    private Long predictedTotal; // 예측된 승하차 총 인원
}