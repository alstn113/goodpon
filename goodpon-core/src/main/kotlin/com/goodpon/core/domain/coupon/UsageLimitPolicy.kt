package com.goodpon.core.domain.coupon

data class UsageLimitPolicy(
    val limitType: CouponTemplateLimitType,
    val issueLimit: Long? = null,
    val useLimit: Long? = null,
) {

    init {
        when (limitType) {
            CouponTemplateLimitType.ISSUE_COUNT -> {
                if (issueLimit == null || issueLimit <= 0) {
                    throw IllegalArgumentException("발급 제한이 설정된 쿠폰은 발급 제한 수량이 필요합니다.")
                }

                if (useLimit != null) {
                    throw IllegalArgumentException("발급 제한이 설정된 쿠폰은 사용 제한 수량을 설정할 수 없습니다.")
                }
            }

            CouponTemplateLimitType.USE_COUNT -> {
                if (useLimit == null || useLimit <= 0) {
                    throw IllegalArgumentException("사용 제한이 설정된 쿠폰은 사용 제한 수량이 필요합니다.")
                }

                if (issueLimit != null) {
                    throw IllegalArgumentException("사용 제한이 설정된 쿠폰은 발급 제한 수량을 설정할 수 없습니다.")
                }
            }

            CouponTemplateLimitType.NONE -> {
                if (issueLimit != null || useLimit != null) {
                    throw IllegalArgumentException("제한이 없는 쿠폰은 발급 제한과 사용 제한을 설정할 수 없습니다.")
                }
            }
        }
    }

    fun isIssuable(issueCount: Long): Boolean {
        return when (limitType) {
            CouponTemplateLimitType.ISSUE_COUNT -> issueLimit?.let { issueCount < it } ?: true
            else -> true
        }
    }

    fun isUsable(useCount: Long): Boolean {
        return when (limitType) {
            CouponTemplateLimitType.USE_COUNT -> useLimit?.let { useCount < it } ?: true
            else -> true
        }
    }
}
