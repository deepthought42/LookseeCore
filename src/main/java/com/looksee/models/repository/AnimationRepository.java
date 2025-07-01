package com.looksee.models.repository;

import com.looksee.models.Animation;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

public interface AnimationRepository extends Neo4jRepository<Animation, Long> {
	public Animation findByKey(@Param("key") String key);
}
