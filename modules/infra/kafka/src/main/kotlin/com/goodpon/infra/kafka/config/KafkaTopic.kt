package com.goodpon.infra.kafka.config

enum class KafkaTopic(
    val topicName: String,
) {

    ISSUE_COUPON_REQUESTED("issue-coupon-requested"),
}