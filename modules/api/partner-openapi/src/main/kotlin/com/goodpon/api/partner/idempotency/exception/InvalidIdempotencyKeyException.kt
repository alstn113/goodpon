package com.goodpon.api.partner.idempotency.exception

import com.goodpon.domain.BaseException

class InvalidIdempotencyKeyException : BaseException("멱등성 키의 길이는 300자 이하여야 합니다.")