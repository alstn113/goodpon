package com.goodpon.dashboard.api.controller.v1.coupon.validation

import com.goodpon.dashboard.api.controller.v1.coupon.dto.CouponHistorySearchRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CouponHistorySearchValidator : ConstraintValidator<CouponHistorySearchValid, CouponHistorySearchRequest> {

    override fun isValid(value: CouponHistorySearchRequest, context: ConstraintValidatorContext): Boolean {
        var isValid = true

        if (value.startDate != null && value.endDate != null && value.endDate < value.startDate) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("startDate는 endDate보다 이후일 수 없습니다.")
                .addPropertyNode("startDate")
                .addConstraintViolation()
            isValid = false
        }

        if (value.size !in 1..100) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("size는 1 이상 100 이하이어야 합니다.")
                .addPropertyNode("size")
                .addConstraintViolation()
            isValid = false
        }

        return isValid
    }
}