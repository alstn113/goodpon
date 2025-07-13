package com.goodpon.dashboard.application.merchant.port.out.exception

import com.goodpon.domain.BaseException

class NoMerchantAccessPermissionException(
    cause: Throwable? = null,
) : BaseException("해당 상점에 접근할 수 있는 권한이 없습니다.", cause)
