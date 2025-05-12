package com.spring.seoulmoaapi.domain.subway.service;

import com.spring.seoulmoaapi.domain.subway.dto.SubwayStationDto;
import com.spring.seoulmoaapi.domain.subway.entity.SubwayPredict;
import com.spring.seoulmoaapi.domain.subway.repository.SubwayPredictRepository;
import com.spring.seoulmoaapi.domain.subway.repository.SubwayStationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubwayStationService {

    private final SubwayStationRepository subwayStationRepository;
    private final SubwayPredictRepository subwayPredictRepository;

    public SubwayStationDto findNearestStation(double lat, double lng, Integer maxDistance) {
        Double distanceParam = Double.valueOf(maxDistance); // 1000m
        log.info("latitude : {}", lat);
        log.info("longitude : {}", lng);
        Object[] raw = subwayStationRepository.findNearestStation(lat, lng, distanceParam)
                .stream()
                .findFirst()
                .orElse(null);
        if (raw == null) {
            return null;
        }
        String subwayName = (String) raw[1];
        List<SubwayPredict> list =  subwayPredictRepository.findAllByName(subwayName);
        return SubwayStationDto.builder()
                .stationId((String) raw[0])
                .name((String) raw[1])
                .line((String) raw[2])
                .latitude((Double) raw[3])
                .longitude((Double) raw[4])
                .distance((Double) raw[5])
                .stationCrowdByHour(list)
                .build();
//        return subwayStationRepository.findNearestStation(lng, lat, distanceParam);
    }
}
