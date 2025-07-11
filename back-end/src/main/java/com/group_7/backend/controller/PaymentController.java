package com.group_7.backend.controller;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.dto.response.VnPayIpnResponse;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.service.IPaymentService;
import com.group_7.backend.service.IUserMembershipService;
import com.group_7.backend.service.impl.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);


    @Autowired
    private IUserMembershipService userMembershipService;
    @Autowired
    private IPaymentService paymentService;

    @PostMapping("/create")
    // Body giờ chỉ cần chứa userMembershipId
    public ResponseEntity<ResponseDto> createPayment(@RequestBody Map<String, Long> payload, HttpServletRequest request) throws UnsupportedEncodingException {
        Long userMembershipId = payload.get("userMembershipId");
        String paymentUrl = paymentService.createPayment(userMembershipId, request);
        return ResponseEntity.ok(new ResponseDto("Success", "Payment URL generated", paymentUrl));
    }

    @GetMapping("/vnpay-ipn")
    public ResponseEntity<VnPayIpnResponse> handleVnPayIpn(HttpServletRequest request) {
        try {
            Map<String, String[]> params = request.getParameterMap();
            boolean isValid = paymentService.verifyCallback(params);

            String[] txnRefs = params.get("vnp_TxnRef");
            String[] responseCodes = params.get("vnp_ResponseCode");

            if (txnRefs == null || txnRefs.length == 0 || responseCodes == null || responseCodes.length == 0) {
                log.warn("IPN Call Warning: Missing vnp_TxnRef or vnp_ResponseCode.");
                return ResponseEntity.ok(new VnPayIpnResponse("01", "Order not found"));
            }

            String fullTxnRef = txnRefs[0]; // Ví dụ: "4-aB1xY2zP"
            String responseCode = responseCodes[0];

            // Tách chuỗi để lấy ra userMembershipId gốc
            String[] parts = fullTxnRef.split("-");
            if (parts.length == 0) {
                log.warn("IPN Call Warning: Invalid vnp_TxnRef format {}", fullTxnRef);
                return ResponseEntity.ok(new VnPayIpnResponse("01", "Order not found"));
            }

            Long orderId = Long.parseLong(parts[0]); // Lấy phần đầu tiên: "4"

            String[] orderIds = params.get("vnp_TxnRef");


            if (orderIds == null || orderIds.length == 0 || responseCodes == null || responseCodes.length == 0) {
                log.warn("IPN Call Warning: Missing vnp_TxnRef or vnp_ResponseCode.");
                return ResponseEntity.ok(new VnPayIpnResponse("01", "Order not found"));
            }

//            Long orderId = Long.parseLong(orderIds[0]);
//            String responseCode = responseCodes[0];

            if (isValid) {
                if ("00".equals(responseCode)) {
                    log.info("IPN Success: Updating orderId {} to ACTIVE.", orderId);
                    // <<< GỌI PHƯƠNG THỨC SERVICE KHÔNG CÓ BẢO MẬT >>>
                    userMembershipService.updateStatusForIpn(orderId, MembershipStatusEnum.ACTIVE);
                    return ResponseEntity.ok(new VnPayIpnResponse("00", "Confirm Success"));
                } else {
                    log.info("IPN Failed: Updating orderId {} to FAILED.", orderId);
                    userMembershipService.updateStatusForIpn(orderId, MembershipStatusEnum.FAILED);
                    return ResponseEntity.ok(new VnPayIpnResponse("00", "Confirm Success"));
                }
            } else {
                log.warn("IPN Failed: Checksum invalid for orderId {}", orderId);
                return ResponseEntity.ok(new VnPayIpnResponse("97", "Invalid Checksum"));
            }
        } catch (Exception e) {
            log.error("IPN Exception: An unexpected error occurred.", e);
            return ResponseEntity.ok(new VnPayIpnResponse("99", "Unknown error"));
        }
    }

    @GetMapping("/vnpay-return")
    public RedirectView handleVnPayReturn(HttpServletRequest request) {
        String returnUrl = "http://localhost:5173/payment-return"; // URL của Frontend
        String queryString = request.getQueryString();
        return new RedirectView(returnUrl + (queryString != null ? "?" + queryString : ""));
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
