package com.spring.seoulmoaapi.domain.wheather.service;

import com.spring.seoulmoaapi.domain.wheather.dto.WeatherDto;
import com.spring.seoulmoaapi.domain.wheather.entity.Weather;
import com.spring.seoulmoaapi.domain.wheather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public List<WeatherDto> getWeatherByGu(String gu){
        List<Weather> weathers = weatherRepository.findAllByGu(gu);
        return weathers.stream().map(Weather::toDto).toList();
    }
}
