package com.goodpon.partner.application.coupon.port.out.dto

enum class IssueResult {

    SUCCESS,
    ALREADY_ISSUED,
    ISSUE_LIMIT_EXCEEDED,
}