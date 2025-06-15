package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.CouponTemplateRepository
import com.goodpon.core.domain.coupon.CouponTemplateStatsCounter
import com.goodpon.core.domain.coupon.IssuedCouponRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponService(
    private val couponTemplateRepository: CouponTemplateRepository,
    private val issuedCouponRepository: IssuedCouponRepository,
    private val couponTemplateStatsCounter: CouponTemplateStatsCounter,
) {

    @Transactional
    fun createCouponTemplate() {
        // 1. 쿠폰 템플릿 생성
        // 2. 쿠폰 템플릿 저장
        // 3. 응답
    }

    @Transactional
    fun issueCoupon() {
        // given
        val couponTemplateId = 1L

        // when
        val couponTemplate = couponTemplateRepository.findById(couponTemplateId)
            ?: throw IllegalArgumentException("쿠폰 템플릿이 존재하지 않습니다.")
        val couponTemplateStats =
        // 1. 쿠폰 템플릿 조회
        // 2. 쿠폰 통계 조회
        // 3. 도메인 서비스 - 정책 검증
        // 4. 쿠폰 발급
        // 5. 쿠폰 발급 이력 저장 - (발급된 쿠폰, 발급 수량)
        // 6. 응답
    }

    @Transactional
    fun useCoupon() {
        // 1. 쿠폰 템플릿 조회
        // 2. 쿠폰 통계 조회
        // 3. 도메인 서비스 - 정책 검증
        // 4. 쿠폰 사용
        // 5. 쿠폰 사용 이력 저장 - (사용된 쿠폰, 사용 수량)
        // 6. 응답
    }
}