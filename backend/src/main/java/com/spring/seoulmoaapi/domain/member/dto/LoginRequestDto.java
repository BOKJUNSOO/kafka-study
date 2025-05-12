package com.spring.seoulmoaapi.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "로그인 요청 dto", example = """
{
  "username": "testUser123",
  "password": "Test@1234"
}
""")
public class LoginRequestDto {
    private String username;
    private String password;
}
