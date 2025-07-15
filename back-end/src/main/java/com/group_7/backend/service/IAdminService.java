package com.group_7.backend.service;

import com.group_7.backend.dto.UserDto;
import java.util.List;

/**
 * Interface định nghĩa các dịch vụ (chức năng) dành riêng cho Admin.
 * Các chức năng này thường liên quan đến việc quản lý tổng thể hệ thống.
 */
public interface IAdminService {

    /**
     * Lấy danh sách tất cả người dùng trong hệ thống.
     * Mỗi UserDto trả về cần được "làm giàu" thêm thông tin về gói thành viên hiện tại.
     *
     * @return Danh sách UserDto đã bao gồm thông tin membership.
     */
    List<UserDto> getAllUsersWithMembership();

    /**
     * Thay đổi trạng thái hoạt động của một người dùng (active <-> inactive).
     *
     * @param userId ID của người dùng cần thay đổi trạng thái.
     * @return UserDto của người dùng sau khi đã được cập nhật.
     */
    UserDto changeUserStatus(Long userId);

    /**
     * Thay đổi vai trò (role) của một người dùng.
     *
     * @param userId ID của người dùng cần thay đổi vai trò.
     * @param newRole Vai trò mới (dưới dạng chuỗi, ví dụ: "MEMBER", "COACH").
     * @return UserDto của người dùng sau khi đã được cập nhật.
     */
    UserDto changeUserRole(Long userId, String newRole);

    /**
     * Lấy các số liệu thống kê tổng quan cho trang Dashboard của Admin.
     * (Chúng ta sẽ cần chức năng này cho trang AdminDashboard)
     *
     * @return Một Map chứa các cặp key-value của các số liệu thống kê.
     *         Ví dụ: { "totalUsers": 100, "totalPosts": 500, ... }
     */
    // Map<String, Object> getDashboardStats(); // Bạn có thể thêm sau khi cần

}