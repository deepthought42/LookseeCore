package com.looksee.models.repository;

import com.looksee.models.audit.AuditSubcategoryStat;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * Repository for {@link AuditSubcategoryStat}
 */
public interface AuditSubcategoryStatRepository extends Neo4jRepository<AuditSubcategoryStat, Long> {
	
}
