package com.spring.seoulmoaapi.domain.subway.repository;

import com.spring.seoulmoaapi.domain.subway.entity.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubwayStationRepository extends JpaRepository<SubwayStation, String> {
    @Query(value = """
                    SELECT 
                ss.station_id AS stationId,
                ss.name AS name,
                ss.line AS line,
                ss.latitude AS latitude,
                ss.longitude AS longitude,
                ss.distance AS distance
            FROM (
                SELECT 
                    station_id,
                    name,
                    line,
                    latitude,
                    longitude,
                    public.ST_DistanceSphere(
                        public.ST_MakePoint(:lng, :lat),
                        public.ST_MakePoint(longitude, latitude)
                    ) AS distance
                FROM datawarehouse.subway_station
                ORDER BY distance
                LIMIT 1
            ) AS ss
            WHERE ss.distance <= :maxDistance
            """, nativeQuery = true)
    List<Object[]> findNearestStation(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("maxDistance") double maxDistance
    );
}