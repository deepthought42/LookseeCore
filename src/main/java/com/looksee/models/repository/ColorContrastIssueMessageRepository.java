package com.looksee.models.repository;

import com.looksee.models.audit.ColorContrastIssueMessage;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Spring Data Neo4j to handle interactions with 
 * {@link ColorContrastIssueMessage} objects
 */
@Repository
public interface ColorContrastIssueMessageRepository extends Neo4jRepository<ColorContrastIssueMessage, Long>  {
	
}
