package com.group_7.backend.controller;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.service.IPaymentService;
import com.group_7.backend.service.IUserMembershipService;
import com.group_7.backend.service.impl.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private IUserMembershipService userMembershipService;
    @Autowired
    private IPaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createPayment(@RequestBody UserMembershipDto userMembershipDto, HttpServletRequest request) throws UnsupportedEncodingException {
        String paymentUrl = paymentService.createPayment(userMembershipDto,request);
        return ResponseEntity.ok(
                new ResponseDto("Success", "Payment URL generated successfully!",paymentUrl));
    }

    @GetMapping("/vnpay-return")
    public UserMembershipDto handleVnPayReturn(HttpServletRequest request) {
        // Lấy params từ request
        Map<String, String[]> params = request.getParameterMap();
        // Lấy orderId (userMembershipId) từ vnp_TxnRef
        String[] orderIds = params.get("vnp_TxnRef");
        Long orderId = orderIds != null && orderIds.length > 0 ? Long.parseLong(orderIds[0]) : null;

        boolean isValid = paymentService.verifyCallback(params);

        if(orderId == null) {
            throw new IllegalArgumentException("OrderId is missing!");
        }

        if (isValid) {
            // Thành công: cập nhật membership sang ACTIVE
            return userMembershipService.updateStatus(orderId, MembershipStatusEnum.ACTIVE);
        } else {
            // Thất bại: cập nhật membership sang FAILED
            return userMembershipService.updateStatus(orderId, MembershipStatusEnum.FAILED);
        }
    }

    /**
     * Bước 2: FE redirect user sang VNPay với orderId chính là userMembershipId vừa tạo
     * (Gắn orderId vào vnp_TxnRef hoặc lưu mapping)
     * FE sẽ build URL thanh toán qua một endpoint hoặc trực tiếp ở FE nếu có thể.
     */

    /**
     * Bước 3: Xử lý callback từ VNPay sau khi thanh toán
     * orderId lấy từ vnp_TxnRef hoặc params của callback
     */
}