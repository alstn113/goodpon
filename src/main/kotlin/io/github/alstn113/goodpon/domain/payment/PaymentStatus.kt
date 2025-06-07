package io.github.alstn113.goodpon.domain.payment

enum class PaymentStatus {

    READY,            // 결제 초기 상태
    IN_PROGRESS,      // 결제 인증 완료 상태, 결제 승인 시 결제 완료(DONE)로 변경
    DONE,             // 결제 완료
    CANCELED,         // 전체 취소
    PARTIAL_CANCELED, // 부분 취소
    ABORTED,          // 결제 실패
    EXPIRED           // 결제 만료, 결제 인증 완료 후 일정 시간 내에 결제 승인이 이루어지지 않은 경우
}