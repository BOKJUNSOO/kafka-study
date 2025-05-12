package com.spring.seoulmoaapi.domain.event.controller;

import com.spring.seoulmoaapi.domain.event.service.CategoryService;
import com.spring.seoulmoaapi.global.common.response.ErrorResponse;
import com.spring.seoulmoaapi.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "Category API", description = "Category 조회 관련 api")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(
            summary = "카테고리 목록 조회",
            description = "카테고리 목록을 조회합니다",
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
                                                       "data": [
                                                         {
                                                           "categoryId": 1,
                                                           "name": "기타"
                                                         },
                                                         {
                                                           "categoryId": 2,
                                                           "name": "교육/체험"
                                                         },
                                                         {
                                                           "categoryId": 3,
                                                           "name": "클래식"
                                                         },
                                                         {
                                                           "categoryId": 4,
                                                           "name": "콘서트"
                                                         },
                                                         {
                                                           "categoryId": 5,
                                                           "name": "영화"
                                                         },
                                                         {
                                                           "categoryId": 6,
                                                           "name": "독주/독창회"
                                                         },
                                                         {
                                                           "categoryId": 7,
                                                           "name": "연극"
                                                         },
                                                         {
                                                           "categoryId": 8,
                                                           "name": "축제"
                                                         },
                                                         {
                                                           "categoryId": 9,
                                                           "name": "뮤지컬/오페라"
                                                         },
                                                         {
                                                       "categoryId": 10,
                                                           "name": "국악"
                                                         },
                                                         {
                                                           "categoryId": 11,
                                                           "name": "전시/미술"
                                                         },
                                                         {
                                                           "categoryId": 12,
                                                           "name": "무용"
                                                         }
                                                       ]
                                                     }
                                                    """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> findAllCategories(){
        return ResponseEntity.ok(SuccessResponse.success(categoryService.findAll()));
    }
}
