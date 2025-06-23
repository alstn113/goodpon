package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.CouponTemplateRepository
import com.goodpon.core.domain.coupon.CouponTemplateStatsCounter
import com.goodpon.core.domain.coupon.IssuedCoupon
import com.goodpon.core.domain.coupon.IssuedCouponRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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
        val userId = 1L

        // when

        // 1. 쿠폰 템플릿 조회
        val template = couponTemplateRepository.findById(couponTemplateId)
            ?: throw IllegalArgumentException("쿠폰 템플릿이 존재하지 않습니다.")
        // 2. 쿠폰 템플릿 상태 검증
        val stats = couponTemplateStatsCounter.getStats(couponTemplateId)

        val now = LocalDateTime.now()
        // 3. 도메인 서비스 - 정책 검증
        template.validateIssue(stats.issueCount, now)
            .onFailure { throw it }
        val expiresAt = template.calculateFinalUsageEndAt(now.toLocalDate())

        // 4. 쿠폰 발급
        val issuedCoupon = IssuedCoupon.issue(
            userId = userId,
            couponTemplateId = couponTemplateId,
            expiresAt = expiresAt,
            now = now
        )

        // 5. 쿠폰 발급 이력 저장
        issuedCouponRepository.save(issuedCoupon)
        // 6. 쿠폰 발급 통계 증가
        couponTemplateStatsCounter.increaseIssueCount(couponTemplateId)
    }

    @Transactional
    fun useCoupon() {
        // given
        val couponTemplateId = 1L
        val userId = 1L

        // when

        // 1. 쿠폰 템플릿 조회
        val template = couponTemplateRepository.findById(couponTemplateId)
            ?: throw IllegalArgumentException("쿠폰 템플릿이 존재하지 않습니다.")
        // 2. 쿠폰 통계 조회
        val stats = couponTemplateStatsCounter.getStats(couponTemplateId)

        // 3. 도메인 서비스 - 정책 검증
        val issuedCoupon = issuedCouponRepository.findByUserIdAndCouponTemplateId(userId, couponTemplateId)
            ?: throw IllegalArgumentException("발급된 쿠폰이 존재하지 않습니다.")
        template.validateUsage(stats.usageCount)
            .onFailure { throw it }

        // 4. 쿠폰 사용
        val usedCoupon = issuedCoupon.markAsUsed()

        // 5. 쿠폰 사용 이력 저장 - (사용된 쿠폰, 사용 수량)
        issuedCouponRepository.save(usedCoupon)
        couponTemplateStatsCounter.increaseUseCount(couponTemplateId)
    }
}