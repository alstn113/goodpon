package com.goodpon.core.domain.coupon.vo

enum class DiscountType {

    FIXED_AMOUNT {
        override fun calculate(orderAmount: Int, discountValue: Int): Int {
            return discountValue
        }
    },
    PERCENTAGE {
        override fun calculate(orderAmount: Int, discountValue: Int): Int {
            return (orderAmount * discountValue / 100.0).toInt()
        }
    };

    abstract fun calculate(orderAmount: Int, discountValue: Int): Int
}