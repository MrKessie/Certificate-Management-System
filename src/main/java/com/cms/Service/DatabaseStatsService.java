package com.cms.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DatabaseStatsService {

    @Autowired
    private EntityManager entityManager;

    public Map<String, Long> getTableRowCounts() {
        Map<String, Long> rowCounts = new HashMap<>();

        // Get all entity classes
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        for (EntityType<?> entity : entities) {
            String tableName = entity.getName();
            Query query = entityManager.createQuery("SELECT COUNT(e) FROM " + tableName + " e");
            Long count = (Long) query.getSingleResult();
            rowCounts.put(tableName, count);
        }

        return rowCounts;
    }
}
