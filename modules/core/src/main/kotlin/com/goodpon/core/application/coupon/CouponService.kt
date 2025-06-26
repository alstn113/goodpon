package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.request.CreateCouponTemplateRequest
import com.goodpon.core.application.coupon.request.IssueCouponRequest
import com.goodpon.core.application.coupon.request.UseCouponRequest
import com.goodpon.core.application.coupon.response.CreateCouponTemplateResponse
import com.goodpon.core.domain.coupon.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponService(
    private val couponTemplateRepository: CouponTemplateRepository,
    private val couponTemplateStatsRepository: CouponTemplateStatsRepository,
    private val issuedCouponRepository: IssuedCouponRepository,
) {

    @Transactional
    fun createCouponTemplate(request: CreateCouponTemplateRequest): CreateCouponTemplateResponse {
        val couponTemplate = request.toCouponTemplate()
        val savedCouponTemplate = couponTemplateRepository.save(couponTemplate)

        return CreateCouponTemplateResponse.from(couponTemplate = savedCouponTemplate)
    }

    // 0. merchantId와 CouponTemplate.merchantId가 일치하는지 검증 - 본인 쿠폰 템플릿만 핸들링 가능
    // 1. 쿠폰 템플릿 읽기 락으로 조회 - CouponTemplate
    // 2. 내가 이미 발급한 쿠폰이 있는지 확인 - IssuedCouponRepository.findByUserIdAndCouponTemplateId
    // 3. 쿠폰 통계 쓰기 락으로 조회 - CouponTemplateStats
    // 4. 발급 개수를 이용해서 쿠폼 템플릿의 메서드로 발급 가능 여부 확인 - CouponTemplate.validateIssue
    // 5. 쿠폰 발급 - IssuedCoupon 생성
    // 6. 통계 업데이트 - CouponTemplateStats
    @Transactional
    fun issueCoupon(request: IssueCouponRequest) {

        // given
        val couponTemplateId = 1L
        val userId = 1L

        // when

        // 1. 쿠폰 템플릿 조회
        val template = couponTemplateRepository.findById(couponTemplateId)
            ?: throw IllegalArgumentException("쿠폰 템플릿이 존재하지 않습니다.")
        // 2. 쿠폰 템플릿 상태 검증
        val stats = couponTemplateStatsRepository.getStats(couponTemplateId)

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
        couponTemplateStatsRepository.increaseIssueCount(couponTemplateId)
    }

    @Transactional
    fun useCoupon(request: UseCouponRequest) {
        // given
        val couponTemplateId = 1L
        val userId = 1L

        // when

        // 1. 쿠폰 템플릿 조회
        val template = couponTemplateRepository.findById(couponTemplateId)
            ?: throw IllegalArgumentException("쿠폰 템플릿이 존재하지 않습니다.")
        // 2. 쿠폰 통계 조회
        val stats = couponTemplateStatsRepository.getStats(couponTemplateId)

        // 3. 도메인 서비스 - 정책 검증
        val issuedCoupon = issuedCouponRepository.findByUserIdAndCouponTemplateId(userId, couponTemplateId)
            ?: throw IllegalArgumentException("발급된 쿠폰이 존재하지 않습니다.")
        template.validateUsage(stats.usageCount)
            .onFailure { throw it }

        // 4. 쿠폰 사용
        val usedCoupon = issuedCoupon.use()

        // 5. 쿠폰 사용 이력 저장 - (사용된 쿠폰, 사용 수량)
        issuedCouponRepository.save(usedCoupon)
        couponTemplateStatsRepository.increaseUseCount(couponTemplateId)
    }
}