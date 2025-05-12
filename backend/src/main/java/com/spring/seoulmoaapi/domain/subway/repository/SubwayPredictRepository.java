package com.spring.seoulmoaapi.domain.subway.repository;

import com.spring.seoulmoaapi.domain.subway.entity.SubwayPredict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubwayPredictRepository extends JpaRepository<SubwayPredict,Integer> {

    List<SubwayPredict> findAllByName(String name);
}
