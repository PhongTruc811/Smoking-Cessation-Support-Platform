package com.group_7.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // Tạo constructor nhận đủ 2 tham số
public class VnPayIpnResponse {

    // Tên thuộc tính phải khớp chính xác với key trong JSON mà VNPAY yêu cầu
    private String RspCode;
    private String Message;

}