//package com.spring.seoulmoaapi.domain.component.dto;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//
//import java.util.List;
//
//@Data
//public class ApiResponseDto {
//    @JsonProperty("culturalEventInfo")
//    private CulturalEventInfo culturalEventInfo;
//
//    @Data
//    public static class CulturalEventInfo {
//        @JsonProperty("list_total_count")
//        private int listTotalCount;
//
//        @JsonProperty("RESULT")
//        private Result result;
//
//        @JsonProperty("row")
//        private List<CulturalEventDto> row;
//    }
//
//    @Data
//    public static class Result {
//        @JsonProperty("CODE")
//        private String code;
//
//        @JsonProperty("MESSAGE")
//        private String message;
//    }
//
//    @Data
//    public static class CulturalEventDto {
//        @JsonProperty("CODENAME")
//        private String codename;
//
//        @JsonProperty("GUNAME")
//        private String guname;
//
//        @JsonProperty("TITLE")
//        private String title;
//
//        @JsonProperty("DATE")
//        private String date;
//
//        @JsonProperty("PLACE")
//        private String place;
//
//        @JsonProperty("ORG_NAME")
//        private String orgName;
//
//        @JsonProperty("USE_TRGT")
//        private String useTrgt;
//
//        @JsonProperty("USE_FEE")
//        private String useFee;
//
//        @JsonProperty("PLAYER")
//        private String player;
//
//        @JsonProperty("PROGRAM")
//        private String program;
//
//        @JsonProperty("ETC_DESC")
//        private String etcDesc;
//
//        @JsonProperty("ORG_LINK")
//        private String orgLink;
//
//        @JsonProperty("MAIN_IMG")
//        private String mainImg;
//
//        @JsonProperty("RGSTDATE")
//        private String rgstDate;
//
//        @JsonProperty("TICKET")
//        private String ticket;
//
//        @JsonProperty("STRTDATE")
//        private String strtDate;
//
//        @JsonProperty("END_DATE")
//        private String endDate;
//
//        @JsonProperty("THEMECODE")
//        private String themeCode;
//
//        @JsonProperty("LOT")
//        private String lot;
//
//        @JsonProperty("LAT")
//        private String lat;
//
//        @JsonProperty("IS_FREE")
//        private String isFree;
//
//        @JsonProperty("HMPG_ADDR")
//        private String hmpgAddr;
//    }
//}