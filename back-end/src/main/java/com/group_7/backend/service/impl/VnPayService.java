package com.group_7.backend.service.impl;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.entity.MembershipPackage;
import com.group_7.backend.entity.User;
import com.group_7.backend.entity.UserMembership;
import com.group_7.backend.entity.enums.MembershipStatusEnum;
import com.group_7.backend.repository.MembershipPackageRepository;
import com.group_7.backend.repository.UserRepository;
import com.group_7.backend.service.IPaymentService;
import com.group_7.backend.service.IUserMembershipService;
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
import java.time.LocalDate;
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

    @Autowired
    private UserRepository userRepository;



    @Override
    public String createPayment(UserMembershipDto userMembershipDto, HttpServletRequest request) throws UnsupportedEncodingException {

        // 1. Giao toàn bộ việc tạo bản ghi PENDING cho UserMembershipService
        UserMembership savedMembership = userMembershipService.createPendingMembership(userMembershipDto);

        // 2. Lấy ID từ kết quả trả về để làm mã giao dịch
        String vnp_TxnRef = String.valueOf(savedMembership.getId());

        // 3. Lấy giá tiền từ đối tượng MembershipPackage bên trong bản ghi vừa tạo
        MembershipPackage membershipPackage = savedMembership.getMembershipPackage();
        long amount = membershipPackage.getPrice().multiply(BigDecimal.valueOf(100)).longValue();

        // 4. Lấy IP của client
        String vnp_IpAddr = getClientIp(request);

        // --- Bắt đầu xây dựng các tham số cho VNPAY ---
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnpTmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan goi " + membershipPackage.getPackageName() + " cho user " + savedMembership.getUser().getUsername());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // !!! ĐẶT DEBUGGER HOẶC IN RA TẠI ĐÂY !!!
        System.out.println("--- VNPAY PARAMS ---");
        System.out.println(vnp_Params);
        System.out.println("----------------------");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // --- Tạo chữ ký và URL cuối cùng ---
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
        String vnp_SecureHash = VnPayTools.hmacSHA512(this.vnpHashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return vnpPayUrl + "?" + queryUrl;

    }

    @Override
    public boolean verifyCallback(Map<String, String[]> params) {
        return true;
    }

    @Override
    public UserMembershipDto handleVnPayReturn(Map<String, String[]> params, Long orderId) {
        System.out.println("🔁 handleVnPayReturn CALLED with orderId: " + orderId);

        boolean isValid = verifyCallback(params);
        if (isValid) {
            return userMembershipService.updateStatus(orderId, MembershipStatusEnum.ACTIVE);
        } else {
            return userMembershipService.updateStatus(orderId, MembershipStatusEnum.FAILED);
        }
    }

//    @Override
//    public boolean verifyCallback(Map<String, String[]> params) {
//        Map<String, String> fields = new HashMap<>();
//        for (Map.Entry<String, String[]> entry : params.entrySet()) {
//            fields.put(entry.getKey(), entry.getValue()[0]);
//        }
//        String vnpSecureHash = fields.remove("vnp_SecureHash");
//        fields.remove("vnp_SecureHashType");
//
//        List<String> fieldNames = new ArrayList<>(fields.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        for (int i = 0; i < fieldNames.size(); i++) {
//            String fieldName = fieldNames.get(i);
//            String value = fields.get(fieldName);
//            if (value != null && value.length() > 0) {
//                hashData.append(fieldName).append('=').append(value);
//                if (i < fieldNames.size() - 1) {
//                    hashData.append('&');
//                }
//            }
//        }
//        String checkHash = hmacSHA512(vnpHashSecret, hashData.toString());
//        return checkHash.equalsIgnoreCase(vnpSecureHash);
//    }

//    @Override
//    public UserMembershipDto handleVnPayReturn(Map<String, String[]> params, Long orderId) {
//        boolean isValid = verifyCallback(params); // kiểm tra chữ ký hash
//        if (isValid) {
//            return userMembershipService.updateStatus(orderId,MembershipStatusEnum.ACTIVE);
//        } else {
//            return userMembershipService.updateStatus(orderId,MembershipStatusEnum.FAILED);
//        }
//    }

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

    public boolean handleIpn(HttpServletRequest request) {
        try {
            Map<String, String> fields = new HashMap<>();
            // Đọc tất cả các tham số từ request VNPAY gửi về
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = fields.remove("vnp_SecureHash");

            // Tạo lại một Map mới không chứa vnp_SecureHashType (nếu có) để kiểm tra chữ ký
            Map<String, String> fieldsToCheck = new HashMap<>(fields);
            fieldsToCheck.remove("vnp_SecureHashType");

            if (!verifySignature(fieldsToCheck, vnp_SecureHash)) {
                return false; // Chữ ký không hợp lệ
            }

            // Lấy mã giao dịch (ID của UserMembership)
            Long orderId = Long.parseLong(fields.get("vnp_TxnRef"));
            // Lấy mã kết quả thanh toán
            String responseCode = fields.get("vnp_ResponseCode");

            // Kiểm tra xem đơn hàng có tồn tại và đang ở trạng thái PENDING không
            UserMembership membership = userMembershipService.findEntityById(orderId); // Cần tạo hàm này


            System.out.println("🔍 Membership status BEFORE: " + membership.getStatus());


            if (membership.getStatus() != MembershipStatusEnum.PENDING) {
                // Đơn hàng này đã được xử lý rồi, trả về true để VNPAY không gửi lại
                return true;
            }

            if ("00".equals(responseCode)) {
                // Giao dịch thành công -> Cập nhật trạng thái thành ACTIVE
                userMembershipService.updateStatus(orderId, MembershipStatusEnum.ACTIVE);
            } else {
                // Giao dịch thất bại -> Cập nhật trạng thái thành FAILED
                userMembershipService.updateStatus(orderId, MembershipStatusEnum.FAILED);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private boolean verifySignature(Map<String, String> fields, String vnpSecureHash) {
        // Sắp xếp các trường theo thứ tự alphabet
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        // Tạo chuỗi hashData
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    // Encode giá trị fieldValue
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    // Xử lý lỗi nếu cần
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        // Băm chuỗi hashData bằng HmacSHA512
        String checkSum = hmacSHA512(this.vnpHashSecret, hashData.toString());

        // So sánh chữ ký
        return checkSum.equalsIgnoreCase(vnpSecureHash);
    }
}