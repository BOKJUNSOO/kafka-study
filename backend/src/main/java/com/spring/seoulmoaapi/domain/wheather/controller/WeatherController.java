package com.spring.seoulmoaapi.domain.wheather.controller;

import com.spring.seoulmoaapi.domain.wheather.dto.WeatherDto;
import com.spring.seoulmoaapi.domain.wheather.service.WeatherService;
import com.spring.seoulmoaapi.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
@Tag(name = "Weather API", description = "날씨 api")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<?> getWeather(@RequestParam String gu) {
        List<WeatherDto> result = weatherService.getWeatherByGu(gu);
        return ResponseEntity.ok(SuccessResponse.success(result));
    }
}
