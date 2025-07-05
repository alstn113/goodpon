package com.goodpon.core.domain.coupon.template.vo

enum class CouponDiscountType {

    FIXED_AMOUNT {
        override fun calculate(orderAmount: Int, discountValue: Int, maxDiscountAmount: Int?): Int {
            return minOf(discountValue, orderAmount)
        }
    },
    PERCENTAGE {
        override fun calculate(orderAmount: Int, discountValue: Int, maxDiscountAmount: Int?): Int {
            val calculateDiscount = (orderAmount * discountValue / 100.0).toInt()
            return minOf(calculateDiscount, maxDiscountAmount!!)
        }
    };

    abstract fun calculate(orderAmount: Int, discountValue: Int, maxDiscountAmount: Int?): Int
}