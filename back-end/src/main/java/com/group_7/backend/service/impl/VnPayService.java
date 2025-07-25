package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.UserMembership;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.exception.ResourceNotFoundException;
import com.group_7.backend.repository.UserMembershipRepository;
import com.group_7.backend.service.IPaymentService;
import com.group_7.backend.service.IUserMembershipService; // Import service interface
import com.group_7.backend.util.VnPayTools;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnPayService implements IPaymentService {
    private static final Logger log = LoggerFactory.getLogger(VnPayService.class);


    @Value("${vnpay.tmnCode}")
    private String vnpTmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnpHashSecret;

    @Value("${vnpay.payUrl}")
    private String vnpPayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnpReturnUrl;

    // --- SỬA LỖI INJECT DEPENDENCY TẠI ĐÂY ---
    private final IUserMembershipService userMembershipService;
    private final UserMembershipRepository userMembershipRepository;

    // Sử dụng Constructor Injection
    public VnPayService(IUserMembershipService userMembershipService,
                        UserMembershipRepository userMembershipRepository) {
        this.userMembershipService = userMembershipService;
        this.userMembershipRepository = userMembershipRepository;
    }



    @Override
    public String createPayment(Long userMembershipId, HttpServletRequest request) throws UnsupportedEncodingException {
        // 1. Tìm bản ghi UserMembership từ ID được cung cấp
        UserMembership userMembership = userMembershipRepository.findById(userMembershipId)
                .orElseThrow(() -> new ResourceNotFoundException("UserMembership not found with id: " + userMembershipId));

        // 2. Lấy thông tin giá tiền từ gói membership liên quan
        long amount = userMembership.getMembershipPackage().getPrice().multiply(BigDecimal.valueOf(100)).longValue();

        // 3. Tạo một chuỗi ngẫu nhiên ngắn
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);

        // 4. Tạo vnp_TxnRef độc nhất bằng cách nối ID và chuỗi ngẫu nhiên
        String vnp_TxnRef = userMembershipId + "-" + randomSuffix;


        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String vnp_IpAddr = getClientIp(request);
        String vnp_TmnCode = vnpTmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef); // <-- Dùng ID của UserMembership
        vnp_Params.put("vnp_OrderInfo", "Thanh toan goi Premium cho User ID:" + userMembership.getUser().getUserId());
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = request.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnpReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // --- Sắp xếp và tạo chuỗi hash ---
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        // **QUAN TRỌNG**: Sử dụng vnpHashSecret được inject từ file properties, không dùng VnPayTools.secretKey
        String vnp_SecureHash = VnPayTools.hmacSHA512(this.vnpHashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnpPayUrl + "?" + queryUrl;
    }

    @Override
    public boolean verifyCallback(Map<String, String[]> params) {
        String vnp_SecureHash = "";
        if (params.containsKey("vnp_SecureHash")) {
            vnp_SecureHash = params.get("vnp_SecureHash")[0];
        }

        // Dùng một TreeMap để tự động sắp xếp các key theo thứ tự alphabet
        Map<String, String> fields = new TreeMap<>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue()[0];
            // Bỏ qua các trường hash và type
            if (!fieldName.equals("vnp_SecureHash") && !fieldName.equals("vnp_SecureHashType")) {
                fields.put(fieldName, fieldValue);
            }
        }

        // Tạo chuỗi hashData
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (hashData.length() > 0) {
                hashData.append('&');
            }
            // GIỮ NGUYÊN GIÁ TRỊ GỐC MÀ VNPAY GỬI, KHÔNG ENCODE LẠI
            hashData.append(entry.getKey()).append('=').append(entry.getValue());
        }

        String checkHash = hmacSHA512(this.vnpHashSecret, hashData.toString());

        // Ghi log để debug
         log.info("Received vnp_SecureHash: {}", vnp_SecureHash);
         log.info("Generated checkHash: {}", checkHash);
         log.info("HashData string: {}", hashData.toString());

        return checkHash.equalsIgnoreCase(vnp_SecureHash);
    }

    @Override
    public UserMembershipDto handleVnPayReturn(Map<String, String[]> params, Long orderId) {
        boolean isValid = verifyCallback(params); // kiểm tra chữ ký hash
        if (isValid) {
            return userMembershipService.updateStatus(orderId, MembershipStatusEnum.ACTIVE);
        } else {
            return userMembershipService.updateStatus(orderId, MembershipStatusEnum.FAILED);
        }
    }

    //Hàm để hash data theo SHA512
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error while hashing HmacSHA512", ex);
        }
    }

    /**
     * Phương thức lấy địa chỉ IP của client một cách an toàn.
     * Nó xử lý các trường hợp request đi qua proxy và môi trường dev (localhost).
     * @param request HttpServletRequest từ controller.
     * @return Chuỗi địa chỉ IP.
     */
    private String getClientIp(HttpServletRequest request) {
        // Thử lấy IP từ header 'X-Forwarded-For' (nếu app chạy sau một proxy/load balancer)
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
                ipAddress = "127.0.0.1";
            }
        }
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }
}