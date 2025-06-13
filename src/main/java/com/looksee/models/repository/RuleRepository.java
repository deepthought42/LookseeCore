package com.looksee.models.repository;

import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import com.looksee.models.rules.Rule;

/**
 * Repository for {@link Rule}s
 */
public interface RuleRepository extends Neo4jRepository<Rule, Long> {
	public Rule findByKey(@Param("key") String key);

	/**
	 * Finds a rule by type and value
	 *
	 * @param rule_type the type of the rule
	 * @param value the value of the rule
	 * @return the rule
	 */
	@Query("MATCH (r:Rule) WHERE r.type=$type AND r.value=$value RETURN r")
	public Rule findByTypeAndValue(@Param("type") String rule_type, 
									@Param("value") String value);
	
	/**
	 * Finds all rules for an element
	 *
	 * @param user_id the ID of the user
	 * @param element_key the key of the element
	 * @return the rules
	 */
	@Query("MATCH (:Account{user_id:$user_id})-[*]->(e:ElementState{key:$element_key}) MATCH (e)-[hr:HAS]->(r) RETURN r")
	public Set<Rule> getRules(@Param("user_id") String user_id, @Param("element_key") String element_key);

	/**
	 * Adds a rule to a form element
	 *
	 * @param username the username of the user
	 * @param element_key the key of the element
	 * @param rule_key the key of the rule
	 * @return the rule
	 */
	@Query("MATCH (:Account{username:$username})-[*]->(e:ElementState{key:$element_key}),(r:Rule{key:$rule_key}) MERGE element=(e)-[hr:HAS]->(r) RETURN r")
	public Rule addRuleToFormElement(@Param("username") String username, @Param("element_key") String element_key, @Param("rule_key") String rule_key);

	/**
	 * Finds a rule for a form element
	 *
	 * @param username the username of the user
	 * @param element_key the key of the element
	 * @param rule_key the key of the rule
	 * @return the rule
	 */
	@Query("MATCH (:Account{username:$username})-[*]->(e:ElementState{key:$element_key}) MATCH (e)-[:HAS]->(r:Rule{key:$rule_key}) RETURN r LIMIT 1")
	public Rule getElementRule(@Param("username") String username, @Param("element_key") String element_key, @Param("rule_key") String rule_key);

}
