package com.group_7.backend.service;

import com.group_7.backend.dto.UserMembershipDto;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IPaymentService {
    String createPayment(UserMembershipDto userMembershipDto, HttpServletRequest request) throws UnsupportedEncodingException;
    boolean verifyCallback(Map<String, String[]> params);
    UserMembershipDto handleVnPayReturn(Map<String, String[]> params, Long orderId);
    boolean handleIpn(HttpServletRequest request);

}
