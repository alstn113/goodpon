package com.goodpon.api.partner.idempotency.exception

import com.goodpon.domain.BaseException

class IdempotencyRequestPayloadMismatchException : BaseException("재시도 된 요청 본문(payload)이 처음 요청과 다릅니다. 새로운 멱등키를 사용해주세요.")