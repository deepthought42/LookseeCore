package com.looksee.models.repository;

import com.looksee.models.audit.performance.AuditDetail;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * Repository interface for Spring Data Neo4j to handle interactions with {@link AuditDetail} objects
 */
public interface AuditDetailRepository extends Neo4jRepository<AuditDetail, Long> {
	
}
