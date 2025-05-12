package com.spring.seoulmoaapi.domain.wheather.entity;

import com.spring.seoulmoaapi.domain.wheather.dto.WeatherDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@Table(name = "weather")
@NoArgsConstructor
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fcst_date")
    private LocalDateTime fcstDate;

    @Column(name = "time")
    private String time;

    @Column(name = "gu")
    private String gu;

    @Column(name = "weather_status")
    private String weatherStatus;

    private Integer temperture;

    public WeatherDto toDto() {
        WeatherDto dto = new WeatherDto();

        if (this.fcstDate != null && this.time != null) {
            dto.setFcscDate(this.fcstDate.toLocalDate());
            dto.setTime(LocalTime.parse(this.time, DateTimeFormatter.ofPattern("HHmm")));
        }
        dto.setGu(this.gu);
        dto.setWeatherStatus(this.weatherStatus);
        dto.setTemperture(this.temperture);
        return dto;
    }
}
