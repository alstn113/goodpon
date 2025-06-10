package io.github.alstn113.goodpon.support.cleaner

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MysqlDatabaseCleaner(
    @PersistenceContext private val em: EntityManager,
) : DataCleaner {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MysqlDatabaseCleaner::class.java)
    }

    @Transactional
    override fun clear() {
        em.clear()
        truncate()
    }

    private fun truncate() {
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate()
        getTruncateQueries().forEach { query -> em.createNativeQuery(query).executeUpdate() }
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate()

        log.info("[MysqlDatabaseCleaner] All tables are truncated.")
    }

    private fun getTruncateQueries(): List<String> {
        val sql = """
                SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';')
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_SCHEMA = (SELECT DATABASE())
                
                """.trimIndent()

        return em.createNativeQuery(sql).resultList.mapNotNull { it as? String }
    }
}