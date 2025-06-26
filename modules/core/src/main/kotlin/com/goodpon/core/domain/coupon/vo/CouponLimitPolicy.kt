package com.goodpon.core.domain.coupon.vo

data class CouponLimitPolicy(
    val limitType: CouponLimitType,
    val maxIssueLimit: Long? = null,
    val maxUsageLimit: Long? = null,
) {

    init {
        when (limitType) {
            CouponLimitType.ISSUE_COUNT -> {
                if (maxIssueLimit == null || maxIssueLimit <= 0) {
                    throw IllegalArgumentException("발급 제한이 설정된 쿠폰은 발급 제한 수량이 필요합니다.")
                }

                if (maxUsageLimit != null) {
                    throw IllegalArgumentException("발급 제한이 설정된 쿠폰은 사용 제한 수량을 설정할 수 없습니다.")
                }
            }

            CouponLimitType.REDEEM_COUNT -> {
                if (maxUsageLimit == null || maxUsageLimit <= 0) {
                    throw IllegalArgumentException("사용 제한이 설정된 쿠폰은 사용 제한 수량이 필요합니다.")
                }

                if (maxIssueLimit != null) {
                    throw IllegalArgumentException("사용 제한이 설정된 쿠폰은 발급 제한 수량을 설정할 수 없습니다.")
                }
            }

            CouponLimitType.NONE -> {
                if (maxIssueLimit != null || maxUsageLimit != null) {
                    throw IllegalArgumentException("제한이 없는 쿠폰은 발급 제한과 사용 제한을 설정할 수 없습니다.")
                }
            }
        }
    }

    fun canIssue(issueCount: Long): Boolean {
        return when (limitType) {
            CouponLimitType.ISSUE_COUNT -> maxIssueLimit?.let { issueCount < it } ?: true
            else -> true
        }
    }

    fun canRedeem(redeemCount: Long): Boolean {
        return when (limitType) {
            CouponLimitType.REDEEM_COUNT -> maxUsageLimit?.let { redeemCount < it } ?: true
            else -> true
        }
    }
}
