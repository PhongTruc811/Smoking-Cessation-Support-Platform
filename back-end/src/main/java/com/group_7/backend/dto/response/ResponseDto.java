package com.group_7.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    String status;
    String message;
    Object data;
    //Về sau thêm token khi dùng JWT

    public ResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
