package com.goodpon.infra.jpa

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MySQLDataCleaner(
    @PersistenceContext
    private val em: EntityManager,
) {

    @Transactional
    fun clear() {
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0;").executeUpdate()
        truncateAllTables()
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1;").executeUpdate()

        em.clear()
    }

    private fun truncateAllTables() {
        // 전체 추상 테스트 클래스에서 testcontainers 공유로 인해 flyway_schema_history 은 유지한다.
        val tableNames = getTableNames()
            .filter { it != "flyway_schema_history" }
        tableNames.forEach { tableName ->
            em.createNativeQuery("TRUNCATE TABLE $tableName;").executeUpdate() // ``는 예약어 충돌 방지
        }
    }

    private fun getTableNames(): List<String> {
        /**
         * 1. information_schema.tables 에서 모든 테이블 메타 데이터를 조회
         * 2. 현재 접속 중인 데이터베이스에 속한 테이블만 조회 (table_schema = DATABASE())
         * 3. 테이블 타입이 뷰(View)가 아닌 일반 테이블(Base Table)만 조회
         */
        val sql = """
            SELECT table_name 
            FROM information_schema.tables
            WHERE table_schema = DATABASE()
            AND table_type = 'BASE TABLE';
        """.trimIndent()

        return em.createNativeQuery(sql)
            .resultList
            .mapNotNull { it as? String }
    }
}