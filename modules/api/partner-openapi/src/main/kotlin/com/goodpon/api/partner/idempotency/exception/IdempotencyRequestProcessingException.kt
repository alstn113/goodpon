package com.goodpon.api.partner.idempotency.exception

import com.goodpon.domain.BaseException

class IdempotencyRequestProcessingException : BaseException("이전 멱등 요청이 처리 중입니다.")