package com.group_7.backend.controller;

import com.group_7.backend.dto.UserMembershipDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IUserMembershipService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-memberships")
public class UserMembershipController {

    private final IUserMembershipService userMembershipService;

    public UserMembershipController(IUserMembershipService userMembershipService) {
        this.userMembershipService = userMembershipService;
    }

//    @PostMapping("/purchase")
//    public ResponseEntity<ResponseDto> purchaseMembership(@Valid @RequestBody UserMembershipDto dto) {
//        UserMembershipDto purchased = userMembershipService.create(dto);
//        return ResponseEntity.ok(new ResponseDto("success", "Membership purchased successfully", purchased));
//    }

    @GetMapping("/user/{userId}/memberships")
    public ResponseEntity<ResponseDto> getMembershipsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ResponseDto("success", "User memberships fetched successfully", userMembershipService.getByUserId(userId))
        );
    }

    @GetMapping("/user/{userId}/currentmembership")
    public ResponseEntity<ResponseDto> getCurrentMembership(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Current membership fetched successfully", userMembershipService.getCurrentMembership(userId))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getUserMembership(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "User membership fetched successfully", userMembershipService.getById(id))
        );
    }

    @PostMapping("/initiate-purchase")
    public ResponseEntity<ResponseDto> initiatePurchase(@Valid @RequestBody UserMembershipDto dto) {
        // Service sẽ tạo một bản ghi UserMembership với status PENDING
        // và trả về bản ghi đó (quan trọng là phải có ID)
        UserMembershipDto pendingMembership = userMembershipService.findOrCreatePendingMembership(dto);
        return ResponseEntity.ok(new ResponseDto("success", "Membership purchase initiated or retrieved", pendingMembership));
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllUserMemberships() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All user memberships fetched successfully", userMembershipService.getAll())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateUserMembership(
            @PathVariable Long id,
            @Valid @RequestBody UserMembershipDto dto) {
        UserMembershipDto updated = userMembershipService.update(id, dto);
        return ResponseEntity.ok(
                new ResponseDto("success", "User membership updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteUserMembership(@PathVariable Long id) {
        userMembershipService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "User membership deleted successfully", null)
        );
    }
}