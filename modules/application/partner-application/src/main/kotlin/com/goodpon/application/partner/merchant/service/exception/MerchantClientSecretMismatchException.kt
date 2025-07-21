package com.goodpon.application.partner.merchant.service.exception

import com.goodpon.domain.BaseException

class MerchantClientSecretMismatchException(
    throwable: Throwable? = null,
) : BaseException("Client Id와 Client Secret이 일치하지 않습니다.", throwable)