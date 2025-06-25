package com.goodpon.core.domain.merchant

enum class MerchantAccountRole {

    OWNER, MEMBER,
}

// account 가 merchant 를 생성하고, 바로 merchantAccount 가 생성됨.
// merchantAccount  = [accountId, merchantId], role = OWNER

// 근데 account 가 활성화된 사용자여야 함. 즉, verifyt
// merchant 생성 -> OWNER merchantAccount 생성 ->