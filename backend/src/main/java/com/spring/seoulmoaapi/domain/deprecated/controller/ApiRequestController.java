//package com.spring.seoulmoaapi.domain.component.controller;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.spring.seoulmoaapi.domain.component.dto.ApiResponseDto;
//import com.spring.seoulmoaapi.domain.component.dto.MetroApiResponseDto;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//
//@RestController
//@Slf4j
//@RequestMapping("/getData")
//public class ApiRequestController {
//
//    @Value("${open.api.key}")
//    String API_KEY;
//    @Value("${open.api.url}")
//    String API_URL;
//    @Value("${open.api.metro.key}")
//    String METRO_KEY;
//
//
//    @GetMapping("/event")
//    public ResponseEntity<?> getData(
//            @RequestParam(name = "serviceName", defaultValue = "culturalEventInfo") String serviceName,
//            @RequestParam(name = "startIndex", defaultValue = "1") String startIndex,
//            @RequestParam(name = "endIndex", defaultValue = "10") String endIndex,
//            @RequestParam(name = "format", defaultValue = "json") String format
//    ) {
//        try {
//            StringBuilder urlBuilder = new StringBuilder(API_URL);
//            urlBuilder.append("/").append(URLEncoder.encode(API_KEY, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(format, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(serviceName, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(startIndex, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(endIndex, "UTF-8"));
//
//            URL url = new URL(urlBuilder.toString());
//            log.info("url : {}", url.toString());
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//
//            BufferedReader rd;
//            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//
//            rd.close();
//            conn.disconnect();
//
//            log.info("API Response: {}", sb.toString());
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            ApiResponseDto map = objectMapper.readValue(sb.toString(), ApiResponseDto.class);
//
//            return ResponseEntity.ok(map);
//        } catch (Exception e) {
//            log.error("API 호출 중 에러 발생", e);
//            return ResponseEntity.internalServerError().body("API 호출 중 오류 발생");
//        }
//    }
//
//    @GetMapping("/metro")
//    public ResponseEntity<?> getMetroData(
//            @RequestParam(name = "serviceName", defaultValue = "subwayStationMaster") String serviceName,
//            @RequestParam(name = "startIndex", defaultValue = "1") String startIndex,
//            @RequestParam(name = "endIndex", defaultValue = "10") String endIndex,
//            @RequestParam(name = "format", defaultValue = "json") String format
//    ) {
//        try {
//            StringBuilder urlBuilder = new StringBuilder(API_URL);
//            urlBuilder.append("/").append(URLEncoder.encode(METRO_KEY, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(format, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(serviceName, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(startIndex, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(endIndex, "UTF-8"));
//            urlBuilder.append("/");
//
//
//            URL url = new URL(urlBuilder.toString());
//            log.info("url : {}", url.toString());
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//
//            BufferedReader rd;
//            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//
//            rd.close();
//            conn.disconnect();
//
//            log.info("API Response: {}", sb.toString());
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            MetroApiResponseDto map = objectMapper.readValue(sb.toString(), MetroApiResponseDto.class);
//
//            return ResponseEntity.ok(map);
//
//        } catch (Exception e) {
//            log.error("API 호출 중 에러 발생", e);
//            return ResponseEntity.internalServerError().body("API 호출 중 오류 발생");
//        }
//    }
//
//    @GetMapping("/metro2")
//    public ResponseEntity<?> getMetro2Data(
//            @RequestParam(name = "serviceName", defaultValue = "subwayStationMaster") String serviceName,
//            @RequestParam(name = "startIndex", defaultValue = "1") String startIndex,
//            @RequestParam(name = "endIndex", defaultValue = "10") String endIndex,
//            @RequestParam(name = "format", defaultValue = "json") String format
//    ) {
//        try {
//            StringBuilder urlBuilder = new StringBuilder(API_URL);
//            urlBuilder.append("/").append(URLEncoder.encode(METRO_KEY, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(format, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(serviceName, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(startIndex, "UTF-8"));
//            urlBuilder.append("/").append(URLEncoder.encode(endIndex, "UTF-8"));
//            urlBuilder.append("/");
//
//
//            URL url = new URL(urlBuilder.toString());
//            log.info("url : {}", url.toString());
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//
//            BufferedReader rd;
//            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//            rd.close();
//            conn.disconnect();
//            log.info("API Response: {}", sb.toString());
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            MetroApiResponseDto map = objectMapper.readValue(sb.toString(), MetroApiResponseDto.class);
//
//            return ResponseEntity.ok(map);
//
//        } catch (Exception e) {
//            log.error("API 호출 중 에러 발생", e);
//            return ResponseEntity.internalServerError().body("API 호출 중 오류 발생");
//        }
//    }
//}
