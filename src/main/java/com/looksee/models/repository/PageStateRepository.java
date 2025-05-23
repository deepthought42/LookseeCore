package com.looksee.models.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.looksee.models.Audit;
import com.looksee.models.PageState;
import com.looksee.models.Screenshot;

import io.github.resilience4j.retry.annotation.Retry;

/**
 * Repository interface for Spring Data Neo4j to handle interactions with
 * {@link PageState} objects
 */
@Repository
@Retry(name = "neoforj")
public interface PageStateRepository extends Neo4jRepository<PageState, Long> {
	/**
	 * Finds a page state by its key and username
	 *
	 * @param user_id the ID of the user
	 * @param key the key of the page state
	 * @return the page state
	 */
	@Query("MATCH (:Account{username:$user_id})-[*]->(p:PageState{key:$key}) RETURN p LIMIT 1")
	public PageState findByKeyAndUsername(@Param("user_id") String user_id, @Param("key") String key);

	/**
	 * Finds a page state by its key
	 *
	 * @param key the key of the page state
	 * @return the page state
	 */
	@Query("MATCH (p:PageState{key:$key}) RETURN p LIMIT 1")
	public PageState findByKey(@Param("key") String key);

	/**
	 * Finds page states by their screenshot checksum and page URL
	 *
	 * @param url the URL of the page
	 * @param checksum the checksum of the screenshot
	 * @return the page states
	 */
	@Query("MATCH (p:PageState{url:$url})-[h:HAS]->() WHERE $screenshot_checksum IN p.screenshot_checksums RETURN a")
	public List<PageState> findByScreenshotChecksumAndPageUrl(@Param("url") String url, @Param("screenshot_checksum") String checksum );
	
	/**
	 * Finds page states by their full page screenshot checksum
	 *
	 * @param checksum the checksum of the full page screenshot
	 * @return the page states
	 */
	@Query("MATCH (p:PageState{full_page_checksum:$screenshot_checksum}) MATCH a=(p)-[h:HAS_CHILD]->() RETURN a")
	public List<PageState> findByFullPageScreenshotChecksum(@Param("screenshot_checksum") String checksum );
	
	/**
	 * Finds screenshots for a page state by its key and username
	 *
	 * @param user_id the ID of the user
	 * @param page_key the key of the page state
	 * @return the screenshots
	 */
	@Query("MATCH (:Account{username:$user_id})-[*]->(p:PageState{key:$page_key}) MATCH (p)-[h:HAS]->(s:Screenshot) RETURN s")
	public List<Screenshot> getScreenshots(@Param("user_id") String user_id, @Param("page_key") String page_key);

	/**
	 * Finds a page state by its animated image checksum and username
	 *
	 * @param user_id the ID of the user
	 * @param screenshot_checksum the checksum of the animated image
	 * @return the page state
	 */
	@Query("MATCH (:Account{username:$user_id})-[*]->(p:PageState{key:$page_key}) WHERE $screenshot_checksum IN p.animated_image_checksums RETURN p LIMIT 1")
	public PageState findByAnimationImageChecksum(@Param("user_id") String user_id, @Param("screenshot_checksum") String screenshot_checksum);

	/**
	 * Finds page states with a form by their account ID, domain URL, and form key
	 *
	 * @param account_id the ID of the account
	 * @param url the URL of the domain
	 * @param form_key the key of the form
	 * @return the page states
	 */
	@Query("MATCH (a:Account)-[]->(d:Domain{url:$url}) MATCH (d)-[]->(p:PageState) MATCH (p)-[:HAS]->(f:Form{key:$form_key}) WHERE id(account)=$account_id RETURN p")
	public List<PageState> findPageStatesWithForm(@Param("account_id") long account_id, @Param("url") String url, @Param("form_key") String form_key);

	/**
	 * Finds page states by their source checksum and domain URL
	 *
	 * @param url the URL of the domain
	 * @param src_checksum the checksum of the source
	 * @return the page states
	 */
	@Query("MATCH (d:Domain{url:$url})-[:HAS]->(ps:PageState{src_checksum:$src_checksum}) MATCH a=(ps)-[h:HAS]->() RETURN a")
	public List<PageState> findBySourceChecksumForDomain(@Param("url") String url, @Param("src_checksum") String src_checksum);
	
	/**
	 * Finds audits for a page state by its key
	 *
	 * @param page_state_key the key of the page state
	 * @return the audits
	 */
	@Query("MATCH (ps:PageState{key:$page_state_key})<-[]-(a:Audit) RETURN a")
	public List<Audit> getAudits(@Param("page_state_key") String page_state_key);

	/**
	 * Finds an audit by its subcategory and page state key
	 *
	 * @param subcategory the subcategory of the audit
	 * @param page_state_key the key of the page state
	 * @return the audit
	 */
	@Query("MATCH (p:PageState{key:$page_state_key})-[*]->(a:Audit{subcategory:$subcategory}) RETURN a")
	public Audit findAuditBySubCategory(@Param("subcategory") String subcategory, @Param("page_state_key") String page_state_key);
	
	/**
	 * Finds the parent page for a page state by its key
	 *
	 * @param page_state_key the key of the page state
	 * @return the parent page
	 */
	@Query("ps:PageState{key:$page_state_key}) return p LIMIT 1")
	public PageState getParentPage(@Param("page_state_key") String page_state_key);

	/**
	 * Finds the most recent page state by its URL
	 *
	 * @param url the URL of the page
	 * @return the page state
	 */
	@Query("MATCH (p:PageState{url:$url}) RETURN p ORDER BY p.created_at DESC LIMIT 1")
	public PageState findByUrl(@Param("url") String url);

	/**
	 * Adds an element to a page state
	 *
	 * @param page_id the ID of the page state
	 * @param element_id the ID of the element
	 * @return the page state
	 */
	@Query("MATCH (p:PageState) WITH p MATCH (element:ElementState) WHERE id(p)=$page_id AND id(element)=$element_id MERGE (p)-[:HAS]->(element) RETURN p LIMIT 1")
	public PageState addElement(@Param("page_id") long page_id, @Param("element_id") long element_id);

	/**
	 * Updates the composite image URL for a page state
	 *
	 * @param id the ID of the page state
	 * @param composite_img_url the URL of the composite image
	 */
	@Query("MATCH (ps:PageState) WHERE id(ps)=$id SET ps.fullPageScreenshotUrlComposite = $composite_img_url RETURN ps")
	public void updateCompositeImageUrl(@Param("id") long id, @Param("composite_img_url") String composite_img_url);

	/**
	 * Adds all elements to a page state
	 *
	 * @param page_state_id the ID of the page state
	 * @param element_id_list the IDs of the elements
	 */
	@Query("MATCH (p:PageState) WITH p MATCH (element:ElementState) WHERE id(p)=$page_state_id AND id(element) IN $element_id_list MERGE (p)-[:HAS]->(element) RETURN p LIMIT 1")
	public void addAllElements(@Param("page_state_id") long page_state_id, @Param("element_id_list") List<Long> element_id_list);
	
	/**
	 * Finds a page state by its audit record ID and key
	 *
	 * @param audit_record_id the ID of the audit record
	 * @param page_key the key of the page state
	 * @return the page state
	 */
	@Query("MATCH (audit_record:AuditRecord)-[:FOR]->(page:PageState) WHERE id(audit_record)=$audit_record_id AND page.key=$page_key RETURN page LIMIT 1")
	public PageState findPageWithKey(@Param("audit_record_id") long audit_record_id, @Param("page_key") String key);

	/**
	 * Finds a page state by its domain audit ID and URL
	 *
	 * @param domain_audit_id the ID of the domain audit
	 * @param url the URL of the page
	 * @return the page state
	 */
	@Query("MATCH (domain_audit:DomainAuditRecord) with domain_audit WHERE id(domain_audit)=$domain_audit_id MATCH (domain_audit)-[:FOR]->(page_state:PageState) WHERE page_state.url=$url MATCH page=(page_state)-[]->(:ElementState) RETURN page LIMIT 1")
	public PageState findByDomainAudit(@Param("domain_audit_id") long domainAuditRecordId, @Param("url") String url);

	/**
	 * Finds the end page for a step
	 *
	 * @param step_id the ID of the step
	 * @return the page state
	 */
	@Query("MATCH (s:Step)-[:ENDS_WITH]->(page:PageState) WHERE id(s)=$step_id RETURN page")
	public PageState getEndPageForStep(@Param("step_id") long id);
}
