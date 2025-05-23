package com.looksee.models.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.looksee.models.journeys.Journey;

/**
 * Repository interface for Spring Data Neo4j to handle interactions with
 * {@link Journey} objects
 */
public interface JourneyRepository extends Neo4jRepository<Journey, Long>  {
	
	/**
	 * Finds a journey by its key
	 *
	 * @param key the key of the journey
	 * @return the journey
	 */
	public Journey findByKey(@Param("key") String key);

	/**
	 * Adds a step to a journey
	 *
	 * @param journey_id the ID of the journey
	 * @param step_id the ID of the step
	 * @return the journey
	 */
	@Query("MATCH (j:Journey) WITH j MATCH (s:Step) WHERE id(s)=$step_id AND id(j)=$journey_id MERGE (j)-[:HAS]->(s) RETURN j")
	public Journey addStep(@Param("journey_id") long journey_id, @Param("step_id") long step_id);

	/**
	 * Finds a journey by its candidate key
	 *
	 * @param candidateKey the candidate key of the journey
	 * @return the journey
	 */
	@Query("MATCH (j:Journey{candidateKey:$candidateKey}) RETURN j")
	public Journey findByCandidateKey(@Param("candidateKey") String candidateKey);
}
