package com.looksee.models.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.looksee.models.DesignSystem;

import io.github.resilience4j.retry.annotation.Retry;

/**
 * Repository interface for Spring Data Neo4j to handle interactions with
 * {@link DesignSystem} objects
 */
@Repository
@Retry(name = "neoforj")
public interface DesignSystemRepository extends Neo4jRepository<DesignSystem, Long> {
	
	/**
	 * Updates the audience proficiency of a design system
	 *
	 * @param domain_id the ID of the design system
	 * @param audience_proficiency the new audience proficiency
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domain_id != null</li>
	 *   <li>audience_proficiency != null</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>The audience proficiency of the design system is updated</li>
	 * </ul>
	 */
	@Query("MATCH (setting:DesignSystem) WHERE id(setting)=$id SET setting.audienceProficiency=$audience_proficiency RETURN setting")
	public DesignSystem updateExpertiseSetting(@Param("id") long domain_id, @Param("audience_proficiency") String audience_proficiency);
	
}
