package com.looksee.models.repository;

import com.looksee.models.audit.AuditSubcategoryStat;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface AuditSubcategoryStatRepository extends Neo4jRepository<AuditSubcategoryStat, Long> {
	
}
