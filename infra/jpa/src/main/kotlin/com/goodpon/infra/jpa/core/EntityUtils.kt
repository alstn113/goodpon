package com.goodpon.infra.jpa.core

object EntityUtils {

    fun <ID, Domain, Entity> synchronizeEntityList(
        isNew: Boolean,
        findExistingEntities: () -> List<Entity>,
        newDomains: List<Domain>,
        idSelector: (Domain) -> ID?,
        entityIdSelector: (Entity) -> ID?,
        createEntity: (Domain) -> Entity,
        updateEntity: (Entity, Domain) -> Unit,
        saveAll: (List<Entity>) -> List<Entity>,
        deleteAll: (List<Entity>) -> Unit,
    ): List<Entity> {
        if (isNew) {
            return saveAll(newDomains.map(createEntity))
        }

        val newDomainById = newDomains.associateBy(idSelector)
        val newIds = newDomainById.keys.filterNotNull().toSet()
        val existingEntities = findExistingEntities()

        val toDelete = findExistingEntities().filter { entityIdSelector(it) !in newIds }
        if (toDelete.isNotEmpty()) deleteAll(toDelete)

        val toInsert = newDomains.filter { idSelector(it) == null || idSelector(it) == 0L }
            .map(createEntity)

        val toUpdate = existingEntities.mapNotNull { existing ->
            val id = entityIdSelector(existing) ?: return@mapNotNull null
            val domain = newDomainById[id] ?: return@mapNotNull null
            updateEntity(existing, domain)
            existing
        }

        return saveAll(toInsert + toUpdate)
    }
}