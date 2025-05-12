package com.spring.seoulmoaapi.domain.event.controller;

import com.spring.seoulmoaapi.domain.event.dto.EventSearchDto;
import com.spring.seoulmoaapi.domain.event.service.EventService;
import com.spring.seoulmoaapi.domain.member.entity.Member;
import com.spring.seoulmoaapi.global.common.response.ErrorResponse;
import com.spring.seoulmoaapi.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "Event API", description = "Event(문화행사) 관련 데이터 처리 api")
@Slf4j
public class EventController {

    private final EventService eventService;

    @GetMapping
    @Operation(
            summary = "이벤트 목록 조회",
            description = "검색 조건에 따라 이벤트 목록을 조회합니다.<br/> 조건에 포함하지 않을 요소는 해당 키 밸류를 삭제하시면 됩니다. <br/> 더보기 방식(offset/limit) 사용. offset : 0, limit : 3 인 경우 3개가 반환됩니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(
                                            name = "성공 예시",
                                            value = """
                                                    {
                                                      "status": "SUCCESS",
                                                      "message": "요청이 성공했습니다.",
                                                      "data": {
                                                        "eventList": [
                                                          {
                                                            "eventId": 1,
                                                            "title": "전시회",
                                                            "categoryName": "전시",
                                                            "gu": "강남구",
                                                            "location": "코엑스",
                                                            "startDate": "2024-10-01T00:00:00",
                                                            "endDate": "2024-10-03T00:00:00",
                                                            "isFree": true
                                                          }
                                                        ],
                                                        "totalCount": 1,
                                                        "offset" : 0,
                                                        "limit" : 3
                                                      }
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "요청 파라미터가 유효하지 않습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            name = "잘못된 날짜 형식",
                                            value = """
                                                    {
                                                      "status": "ERROR",
                                                      "message": "날짜 형식이 올바르지 않습니다.",
                                                      "data": null
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    // 검색 조건 (ex : 날짜 , 행사명, 유무료, 행사유치구 , 카테고리, 행사 설명 )
    public ResponseEntity<?> getEvents(@Parameter(description = "검색 조건")@ModelAttribute EventSearchDto dto, @AuthenticationPrincipal Member userDetails){
        if(userDetails == null){
            return ResponseEntity.ok(SuccessResponse.success(eventService.searchEvents(dto,null)));
        }else {
            return ResponseEntity.ok(SuccessResponse.success(eventService.searchEvents(dto,userDetails.getUserId())));
        }
    }

    @GetMapping("/data/{eventId}")
    @Operation(
            summary = "이벤트 상세 조회",
            description = "이벤트 ID를 통해 상세 정보를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(
                                            name = "성공 예시",
                                            value = """
                                                    {
                                                      "status": "SUCCESS",
                                                      "message": "요청이 성공했습니다.",
                                                      "data": {
                                                        "eventId": 1,
                                                        "title": "K-핸드메이드페어 2025",
                                                        "categoryName": "전시/미술",
                                                        "categoryId": {
                                                          "categoryId": 11,
                                                          "name": "전시/미술"
                                                        },
                                                        "gu": "강남구",
                                                        "location": "서울 삼성동 코엑스 1층  B홀",
                                                        "startDate": "2025-12-18T00:00:00",
                                                        "endDate": "2025-12-21T00:00:00",
                                                        "fee": "사전 예매가: 8,000원, 현장 구매가: 10,000원",
                                                        "isFree": false,
                                                        "latitude": 37.5118239121138,
                                                        "longitude": 127.059159043842,
                                                        "homepage": "https://culture.seoul.go.kr/culture/culture/cultureEvent/view.do?cultcode=152033&menuNo=200009",
                                                        "imageUrl": "https://culture.seoul.go.kr/cmmn/file/getImage.do?atchFileId=42afe00583eb4b0983dba37a04a41222&thumb=Y",
                                                        "detailUrl": "https://k-handmade.com/",
                                                        "targetUser": "누구나",
                                                        "eventDescription": "[K-핸드메이드페어 2025 K-HANDMADE FAIR 2025  2025. 12. 18(Thur.)~21(Sun.) 서울 코엑스 B홀, COEX HALL B Opening Hours 11:00-18:00 행복의 페이지",
                                                        "likeCount": 1,
                                                        "isLiked": true,
                                                        "nearStations": [
                                                          {
                                                            "name": "봉은사",
                                                            "distance": 283.0226113,
                                                            "line": "9호선(연장)",
                                                            "latitude": 37.514219,
                                                            "longitude": 127.060245,
                                                            "stationId": "4129"
                                                          },
                                                          {
                                                            "name": "삼성(무역센터)",
                                                            "distance": 484.08129819,
                                                            "line": "2호선",
                                                            "latitude": 37.508844,
                                                            "longitude": 127.06316,
                                                            "stationId": "219"
                                                          },
                                                          {
                                                            "name": "삼성(무역센터)",
                                                            "distance": 484.08129819,
                                                            "line": "2호선",
                                                            "latitude": 37.508844,
                                                            "longitude": 127.06316,
                                                            "stationId": "0219"
                                                          }
                                                        ]
                                                      }
                                                    }
                                                """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 또는 존재하지 않는 이벤트 ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(
                                            name = "존재하지 않는 이벤트",
                                            value = """
                                                {
                                                  "status": "ERROR",
                                                  "message": "해당 이벤트를 찾을 수 없습니다.",
                                                  "data": null
                                                }
                                                """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> getEventById(@PathVariable Long eventId, @AuthenticationPrincipal Member userDetails){
        return ResponseEntity.ok(SuccessResponse.success(eventService.getEventById(eventId,userDetails)));

    }


    @GetMapping("/liked")
    @Operation(
            summary = "유저 좋아요 이벤트 조회",
            description = "로그인된 유저가 좋아요를 등록한 이벤트 목록을 조회합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "요청이 성공했습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponse.class),
                                    examples = @ExampleObject(
                                            name = "성공 예시",
                                            value = """
                                                    {
                                                       "status": "SUCCESS",
                                                       "message": "요청이 성공했습니다.",
                                                       "data": {
                                                         "eventList": [
                                                           {
                                                             "eventId": 2,
                                                             "title": "2025 서울라이트 한강 빛섬축제",
                                                             "categoryName": "축제",
                                                             "categoryId": {
                                                               "categoryId": 8,
                                                               "name": "축제"
                                                             },
                                                             "gu": "광진구",
                                                             "location": "뚝섬 한강공원",
                                                             "startDate": "2025-10-03T00:00:00",
                                                             "endDate": "2025-10-12T00:00:00",
                                                             "fee": "",
                                                             "isFree": true,
                                                             "latitude": 127.073978164616,
                                                             "longtitude": 37.5293645718995,
                                                             "homepage": "https://culture.seoul.go.kr/culture/culture/cultureEvent/view.do?cultcode=153245&menuNo=200010",
                                                             "imageUrl": "https://culture.seoul.go.kr/cmmn/file/getImage.do?atchFileId=b13d2032bec441088ab18e1f0164eaa6&thumb=Y",
                                                             "detailUrl": "https://bitseomfestival.com/",
                                                             "targetUser": "누구나",
                                                             "eventDescription": "2025 서울라이트 한강 빛섬축제",
                                                             "likeCount": 1,
                                                             "isLiked": true
                                                           },
                                                           {
                                                             "eventId": 1,
                                                             "title": "K-핸드메이드페어 2025",
                                                             "categoryName": "전시/미술",
                                                             "categoryId": {
                                                               "categoryId": 11,
                                                               "name": "전시/미술"
                                                             },
                                                             "gu": "강남구",
                                                             "location": "서울 삼성동 코엑스 1층  B홀",
                                                             "startDate": "2025-12-18T00:00:00",
                                                             "endDate": "2025-12-21T00:00:00",
                                                             "fee": "사전 예매가: 8,000원, 현장 구매가: 10,000원",
                                                             "isFree": false,
                                                             "latitude": 127.059159043842,
                                                             "longtitude": 37.5118239121138,
                                                             "homepage": "https://culture.seoul.go.kr/culture/culture/cultureEvent/view.do?cultcode=152033&menuNo=200009",
                                                             "imageUrl": "https://culture.seoul.go.kr/cmmn/file/getImage.do?atchFileId=42afe00583eb4b0983dba37a04a41222&thumb=Y",
                                                             "detailUrl": "https://k-handmade.com/",
                                                             "targetUser": "누구나",
                                                             "eventDescription": "[K-핸드메이드페어 2025 K-HANDMADE FAIR 2025  2025. 12. 18(Thur.)~21(Sun.) 서울 코엑스 B홀, COEX HALL B Opening Hours 11:00-18:00 행복의 페이지",
                                                             "likeCount": 1,
                                                             "isLiked": true
                                                           }
                                                         ],
                                                         "offset": 0,
                                                         "limit": 3,
                                                         "totalCount": 2
                                                       }
                                                     }
                                                """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> getLikedEvents(@AuthenticationPrincipal Member userDetails,@ModelAttribute EventSearchDto dto){
        // Pageable 생성 (offset 기반 → page number로 변환 필요)
        int page = dto.getOffset(); // offset → page 번호
        int limit = dto.getLimit();
        Pageable pageable = PageRequest.of(page,limit);
        return ResponseEntity.ok(SuccessResponse.success(eventService.getLikedEvents(userDetails,pageable)));
    }



    @GetMapping("/recently/reviewed")
    public ResponseEntity<?> getRecentlyReviewedEvents(@AuthenticationPrincipal Member userDetails,@RequestParam Integer offset, @RequestParam Integer limit){
        Pageable pageable = PageRequest.of(offset, limit);
        return ResponseEntity.ok(SuccessResponse.success(eventService.getRecentlyReviewedEvents(userDetails,pageable)));
    }
}
