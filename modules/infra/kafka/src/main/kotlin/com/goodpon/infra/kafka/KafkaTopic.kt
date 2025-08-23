package com.goodpon.infra.kafka

enum class KafkaTopic(
    val topicName: String,
) {

    ISSUE_COUPON_REQUESTED("issue-coupon-requested"),
}