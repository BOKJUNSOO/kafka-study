package com.spring.seoulmoaapi.domain.wheather.repository;

import com.spring.seoulmoaapi.domain.wheather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather,String> {

    List<Weather> findAllByGu(String gu);
}
