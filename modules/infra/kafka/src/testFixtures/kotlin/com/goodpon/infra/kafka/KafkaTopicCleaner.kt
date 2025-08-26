package com.goodpon.infra.kafka

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.OffsetSpec
import org.apache.kafka.clients.admin.RecordsToDelete
import org.apache.kafka.common.TopicPartition
import org.springframework.stereotype.Component

@Component
class KafkaTopicCleaner {

    /**
     * 모든 Kafka 토픽의 데이터를 삭제합니다.
     */
    fun clear() {
        AdminClient.create(adminConfig()).use { adminClient ->
            val topics = listTopics(adminClient)
            if (topics.isEmpty()) return

            val topicPartitions = listTopicPartitions(adminClient, topics)
            if (topicPartitions.isEmpty()) return

            val endOffsets = fetchEndOffsets(adminClient, topicPartitions)
            deleteRecords(adminClient, endOffsets)
        }
    }

    private fun adminConfig(): Map<String, Any> {
        return mapOf(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to KafkaContainerInitializer.KAFKA_CONTAINER.bootstrapServers
        )
    }

    private fun listTopics(adminClient: AdminClient): Set<String> {
        return adminClient.listTopics().names().get()
    }

    private fun listTopicPartitions(adminClient: AdminClient, topics: Set<String>): List<TopicPartition> {
        return topics.flatMap { topic ->
            adminClient.describeTopics(listOf(topic))
                .allTopicNames()
                .get()[topic]!!.partitions()
                .map { partitionInfo -> TopicPartition(topic, partitionInfo.partition()) }
        }
    }

    private fun fetchEndOffsets(
        adminClient: AdminClient,
        partitions: List<TopicPartition>,
    ): Map<TopicPartition, Long> {
        return adminClient.listOffsets(partitions.associateWith { OffsetSpec.latest() })
            .all().get()
            .mapValues { it.value.offset() }
    }

    private fun deleteRecords(adminClient: AdminClient, offsets: Map<TopicPartition, Long>) {
        val deleteResult = adminClient.deleteRecords(offsets.mapValues { RecordsToDelete.beforeOffset(it.value) })
        deleteResult.all().get() // 삭제 완료 대기
    }
}
