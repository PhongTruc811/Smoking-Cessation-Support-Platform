package com.group_7.backend.controller;

import com.group_7.backend.dto.MembershipPackageDto;
import com.group_7.backend.dto.response.ResponseDto;
import com.group_7.backend.service.IMembershipPackageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/membershippackage")
public class MembershipPackageController {

    private final IMembershipPackageService membershipPackageService;

    public MembershipPackageController(IMembershipPackageService membershipPackageService) {
        this.membershipPackageService = membershipPackageService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createMembershipPackage(@Valid @RequestBody MembershipPackageDto membershipPackageDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Membership package created successfully",
                        membershipPackageService.create(membershipPackageDto))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getMembershipPackage(@PathVariable long id) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Membership package fetched successfully",
                        membershipPackageService.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllMembershipPackages() {
        return ResponseEntity.ok(
                new ResponseDto("success", "All membership packages fetched successfully",
                        membershipPackageService.getAll())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateMembershipPackage(@Valid @PathVariable long id, @RequestBody MembershipPackageDto membershipPackageDto) {
        return ResponseEntity.ok(
                new ResponseDto("success", "Membership package updated successfully",
                        membershipPackageService.update(id, membershipPackageDto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteMembershipPackage(@PathVariable long id) {
        membershipPackageService.delete(id);
        return ResponseEntity.ok(
                new ResponseDto("success", "Membership package deleted successfully", null)
        );
    }
}