package com.goodpon.domain.support

enum class DomainErrorMessage(
    val code: String,
    val statusCode: Int,
    val message: String,
) {
    // Account
    ACCOUNT_ALREADY_VERIFIED("A001", 409, "이미 인증된 계정입니다."),
    ACCOUNT_EMAIL_INVALID_FORMAT("A002", 400, "이메일 형식이 올바르지 않습니다."),
    ACCOUNT_NAME_INVALID_LENGTH("A003", 400, "이름 길이가 올바르지 않습니다."),
    ACCOUNT_PASSWORD_INVALID_LENGTH("A004", 400, "비밀번호 길이가 올바르지 않습니다."),
    ACCOUNT_NAME_BLANK("A005", 400, "이름은 필수 입력값입니다."),

    // Coupon - Discount Policy
    COUPON_DISCOUNT_FIXED_MAX_INVALID("C001", 400, "할인 정책의 최대 고정값이 올바르지 않습니다."),
    COUPON_DISCOUNT_FIXED_VALUE_INVALID("C002", 400, "할인 정책의 고정값이 올바르지 않습니다."),
    COUPON_DISCOUNT_PERCENT_MAX_INVALID("C003", 400, "할인 정책의 최대 퍼센트값이 올바르지 않습니다."),
    COUPON_DISCOUNT_PERCENT_VALUE_INVALID("C004", 400, "할인 정책의 퍼센트값이 올바르지 않습니다."),

    // Coupon - Limit Policy
    COUPON_LIMIT_ISSUE_VALUE_INVALID("C005", 400, "쿠폰 발급 한도 값이 올바르지 않습니다."),
    COUPON_LIMIT_REDEEM_VALUE_INVALID("C006", 400, "쿠폰 사용 한도 값이 올바르지 않습니다."),
    COUPON_LIMIT_ISSUE_REDEEM_CONFLICT("C007", 400, "쿠폰 발급 한도와 사용 한도가 충돌합니다."),
    COUPON_LIMIT_NONE_CONFLICT("C008", 400, "쿠폰 한도 정책이 충돌합니다."),
    COUPON_LIMIT_REDEEM_ISSUE_CONFLICT("C009", 400, "쿠폰 사용 한도와 발급 한도가 충돌합니다."),

    // Coupon - Period
    COUPON_PERIOD_EXPIRE_BEFORE_ISSUE_END_INVALID("C010", 400, "만료일이 발급 종료일보다 앞설 수 없습니다."),
    COUPON_PERIOD_EXPIRE_BEFORE_START_INVALID("C011", 400, "만료일이 시작일보다 앞설 수 없습니다."),
    COUPON_PERIOD_EXPIRE_WITHOUT_ISSUE_END_INVALID("C012", 400, "발급 종료일 없는 만료일은 허용되지 않습니다."),
    COUPON_PERIOD_ISSUE_END_BEFORE_START_INVALID("C013", 400, "발급 종료일이 시작일보다 앞설 수 없습니다."),
    COUPON_PERIOD_VALIDITY_DAYS_INVALID("C014", 400, "유효기간 일수가 올바르지 않습니다."),

    // Coupon - Redemption Condition
    COUPON_REDEMPTION_MIN_ORDER_AMOUNT_INVALID("C015", 400, "최소 주문 금액 조건이 올바르지 않습니다."),

    // Coupon - Template
    COUPON_TEMPLATE_EXPIRATION_NOT_ALLOWED("C016", 400, "쿠폰 만료가 허용되지 않습니다."),
    COUPON_TEMPLATE_ISSUANCE_LIMIT_EXCEEDED("C017", 400, "쿠폰 발급 한도를 초과했습니다."),
    COUPON_TEMPLATE_ISSUANCE_PERIOD_INVALID("C018", 400, "쿠폰 발급 기간이 올바르지 않습니다."),
    COUPON_TEMPLATE_PUBLISH_NOT_ALLOWED("C019", 400, "쿠폰 발행이 허용되지 않습니다."),
    COUPON_TEMPLATE_REDEMPTION_CONDITION_NOT_SATISFIED("C020", 400, "쿠폰 사용 조건이 충족되지 않습니다."),
    COUPON_TEMPLATE_REDEMPTION_LIMIT_EXCEEDED("C021", 400, "쿠폰 사용 한도를 초과했습니다."),
    COUPON_TEMPLATE_STATUS_NOT_ISSUABLE("C022", 400, "쿠폰 상태가 발급 가능하지 않습니다."),
    COUPON_TEMPLATE_STATUS_NOT_REDEEMABLE("C023", 400, "쿠폰 상태가 사용 가능하지 않습니다."),

    // UserCoupon
    USER_COUPON_CANCEL_NOT_ALLOWED("U001", 400, "쿠폰 취소가 허용되지 않습니다."),
    USER_COUPON_EXPIRED("U002", 400, "쿠폰이 만료되었습니다."),
    USER_COUPON_EXPIRE_NOT_ALLOWED("U003", 400, "쿠폰 만료가 허용되지 않습니다."),
    USER_COUPON_REDEEM_NOT_ALLOWED("U004", 400, "쿠폰 사용이 허용되지 않습니다."),
    ;
}