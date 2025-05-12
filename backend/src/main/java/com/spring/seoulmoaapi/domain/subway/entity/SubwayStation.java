package com.spring.seoulmoaapi.domain.subway.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "subway_station")
public class SubwayStation {

    @Id
    @Column(name = "station_id")
    private String stationId;

    @Column(name = "\"name\"")  // name은 예약어일 수 있어 이스케이프 필요
    private String name;

    @Column(name = "line")
    private String line;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}