package com.spring.seoulmoaapi.domain.subway.dto;

import com.spring.seoulmoaapi.domain.subway.entity.SubwayPredict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubwayStationDto {
    private String stationId;
    private String name;
    private String line;
    private Double latitude;
    private Double longitude;
    private Double distance;
    private List<SubwayPredict> stationCrowdByHour;
}