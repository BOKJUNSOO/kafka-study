//package com.spring.seoulmoaapi.domain.component.dto;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//
//import java.util.List;
//
//@Data
//public class MetroApiResponseDto {
//
//    @JsonProperty("subwayStationMaster")
//    private SubwayStationMaster subwayStationMaster;
//
//    @Data
//    public static class SubwayStationMaster {
//        @JsonProperty("list_total_count")
//        private int list_total_count;
//        @JsonProperty("RESULT")
//        private Result RESULT;
//        @JsonProperty("row")
//        private List<SubwayStation> row;
//    }
//
//    @Data
//    public static class Result {
//        @JsonProperty("CODE")
//        private String CODE;
//        @JsonProperty("MESSAGE")
//        private String MESSAGE;
//    }
//
//    @Data
//    public static class SubwayStation {
//        @JsonProperty("BLDN_ID")
//        private String bldnId;
//
//        @JsonProperty("BLDN_NM")
//        private String bldnName;
//
//        @JsonProperty("ROUTE")
//        private String route;
//
//        @JsonProperty("LAT")
//        private String lat;
//
//        @JsonProperty("LOT")
//        private String lot;
//    }
//}