package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.MembershipPackage;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.repository.MembershipPackageRepository;
import com.group_7.backend.service.IPaymentService;
import com.group_7.backend.util.VnPayTools;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Value("${vnpay.tmnCode}")
    private String vnpTmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnpHashSecret;

    @Value("${vnpay.payUrl}")
    private String vnpPayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnpReturnUrl;

    @Autowired
    private UserMembershipServiceImp userMembershipService;

    @Autowired
    private MembershipPackageRepository membershipPackageRepository;

    @Override
    public String createPayment(UserMembershipDto userMembershipDto, HttpServletRequest request) throws UnsupportedEncodingException {
        // --- SỬA LỖI BẮT ĐẦU TỪ ĐÂY ---

        // 1. Lấy membershipPackageId từ DTO mà frontend gửi lên
        Long packageId = userMembershipDto.getMembershipPackageId();
        if (packageId == null) {
            throw new IllegalArgumentException("MembershipPackageId is required.");
        }

        // 2. Dùng repository để tìm thông tin gói trong database
        // Lưu ý: .findById trả về Optional, nên dùng .orElseThrow để xử lý nếu không tìm thấy
        MembershipPackage membershipPackage = membershipPackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Membership package not found with id: " + packageId));

        // 3. Lấy giá tiền từ đối tượng đã tìm được và nhân với 100 cho VNPAY
        long amount = membershipPackage.getPrice().multiply(BigDecimal.valueOf(100)).longValue();

        // --- KẾT THÚC PHẦN SỬA LỖI ---
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
//        long amount = Integer.parseInt(request.getParameter("amount"))*100;
//        String bankCode = request.getParameter("bankCode");

        String vnp_TxnRef = VnPayTools.getRandomNumber(8);
        // Lấy địa chỉ IP của client một cách an toàn
        String vnp_IpAddr = getClientIp(request); // <-- SỬ DỤNG PHƯƠNG THỨC MỚI

        String vnp_TmnCode = vnpTmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
//        if (bankCode != null && !bankCode.isEmpty()) {
//            vnp_Params.put("vnp_BankCode", bankCode);
//        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef); //TODO: xóa
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

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
//        String vnp_SecureHash = VnPayTools.hmacSHA512(VnPayTools.secretKey, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        return vnpPayUrl + "?" + queryUrl;

        // **QUAN TRỌNG**: Sử dụng vnpHashSecret được inject từ file properties, không dùng VnPayTools.secretKey
        String vnp_SecureHash = VnPayTools.hmacSHA512(this.vnpHashSecret, hashData.toString());

        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return vnpPayUrl + "?" + queryUrl;
    }

    @Override
    public boolean verifyCallback(Map<String, String[]> params) {
        Map<String, String> fields = new HashMap<>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            fields.put(entry.getKey(), entry.getValue()[0]);
        }
        String vnpSecureHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String value = fields.get(fieldName);
            if (value != null && value.length() > 0) {
                hashData.append(fieldName).append('=').append(value);
                if (i < fieldNames.size() - 1) {
                    hashData.append('&');
                }
            }
        }
        String checkHash = hmacSHA512(vnpHashSecret, hashData.toString());
        return checkHash.equalsIgnoreCase(vnpSecureHash);
    }

    @Override
    public UserMembershipDto handleVnPayReturn(Map<String, String[]> params, Long orderId) {
        boolean isValid = verifyCallback(params); // kiểm tra chữ ký hash
        if (isValid) {
            return userMembershipService.updateStatus(orderId,MembershipStatusEnum.ACTIVE);
        } else {
            return userMembershipService.updateStatus(orderId,MembershipStatusEnum.FAILED);
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
            throw new RuntimeException("Lỗi khi mã hóa HmacSHA512", ex);
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

        if (Objects.isNull(ipAddress) || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (Objects.isNull(ipAddress) || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (Objects.isNull(ipAddress) || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            // Xử lý trường hợp đặc biệt của localhost IPv6
            if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
                ipAddress = "127.0.0.1";
            }
        }

        // Nếu IP có thể chứa nhiều địa chỉ (do đi qua nhiều proxy), lấy địa chỉ đầu tiên
        if (!Objects.isNull(ipAddress) && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        // Nếu sau tất cả các bước vẫn là null, gán một giá trị mặc định cho môi trường dev
        if (Objects.isNull(ipAddress)) {
            ipAddress = "127.0.0.1"; // Fallback IP for local development
        }

        return ipAddress;
    }
}