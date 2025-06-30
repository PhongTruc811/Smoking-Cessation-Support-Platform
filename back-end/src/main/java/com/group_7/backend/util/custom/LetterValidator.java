package com.group_7.backend.util.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LetterValidator implements ConstraintValidator<ValidLetter, String> {

    @Override
    public void initialize(ValidLetter constraintAnnotation) {
    }

    @Override
    public boolean isValid(String fullName, ConstraintValidatorContext context) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return true; // @NotBlank sẽ xử lý trường hợp này, nếu fullName là null/empty thì trả về true để không bị lỗi 2 lần
        }
        // Regex để chỉ chấp nhận chữ cái (tiếng Anh và tiếng Việt không dấu, có dấu), và khoảng trắng, không cho phép số, ký tự đặc biệt
        return fullName.matches("^[\\p{L}\\s]+$");
    }
}