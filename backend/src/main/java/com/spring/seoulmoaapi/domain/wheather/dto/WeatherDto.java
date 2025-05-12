package com.spring.seoulmoaapi.domain.wheather.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class WeatherDto {
    private LocalDate fcscDate;
    private LocalTime time;
    private String gu;
    private String weatherStatus;
    private Integer temperture;
}
