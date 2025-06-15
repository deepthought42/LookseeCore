package com.looksee.models.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.looksee.models.enums.JourneyStatus;
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

	/**
	 * Finds all journeys for a domain audit record
	 *
	 * @param audit_id the ID of the domain audit record
	 * @param status the status of the journey
	 * @return the number of journeys
	 */
	@Query("MATCH (audit:DomainAuditRecord) WHERE id(audit)=$audit_id MATCH (audit)-[*2]->(j:Journey) WHERE j.status=$status RETURN COUNT(j)")
	public int findAllJourneysForDomainAudit(@Param("audit_id") long audit_id, @Param("status") JourneyStatus status);

}
