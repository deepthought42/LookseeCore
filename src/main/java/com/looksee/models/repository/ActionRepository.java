package com.looksee.models.repository;

import com.looksee.models.ActionOLD;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 */
@Repository
public interface ActionRepository extends Neo4jRepository<ActionOLD, Long> {
	public	ActionOLD findByKey(@Param("key") String key);

}
