package com.goodpon.dashboard.api.controller.v1.coupon.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CouponHistorySearchValidator::class])
annotation class CouponHistorySearchValid(
    val message: String = "쿠폰 내역 검색 요청이 유효하지 않습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)