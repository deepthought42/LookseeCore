package com.looksee.models.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.looksee.models.Audit;
import com.looksee.models.AuditRecord;
import com.looksee.models.DesignSystem;
import com.looksee.models.DomainAuditRecord;
import com.looksee.models.Label;
import com.looksee.models.PageAuditRecord;
import com.looksee.models.PageState;
import com.looksee.models.UXIssueMessage;

import io.github.resilience4j.retry.annotation.Retry;

/**
 * Repository interface for Spring Data Neo4j to handle interactions with {@link Audit} objects.
 * 
 * <p><b>Class Invariants:</b>
 * <ul>
 *   <li>All audit records must have a unique key</li>
 *   <li>All relationships between nodes must be valid (e.g., AuditRecord-HAS->Audit)</li>
 *   <li>All audit records must be associated with either a PageState or Domain</li>
 *   <li>All audit records must have a valid creation timestamp</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b>
 * This repository is thread-safe as it is managed by Spring's dependency injection container.
 * All operations are atomic at the database level.
 */
@Repository
@Retry(name = "neoforj")
public interface AuditRecordRepository extends Neo4jRepository<AuditRecord, Long> {
	/**
	 * Finds an audit record by its unique key.
	 *
	 * @param key the unique identifier of the audit record
	 * @return the audit record if found, null otherwise
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>key != null</li>
	 *   <li>key is not empty</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>If a record exists with the given key, it is returned</li>
	 *   <li>If no record exists, null is returned</li>
	 * </ul>
	 */
	public AuditRecord findByKey(@Param("key") String key);
	
	/**
	 * Retrieves a specific audit associated with an audit record by their keys.
	 *
	 * @param audit_record_key the key of the audit record
	 * @param audit_key the key of the audit
	 * @return Optional containing the audit if found and associated
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>If the audit exists and is associated with the record, returns Optional containing the audit</li>
	 *   <li>If the audit doesn't exist or isn't associated, returns empty Optional</li>
	 * </ul>
	 */
	@Query("MATCH (:AuditRecord{key:$audit_record_key})-[:HAS]->(a:Audit{key:$audit_key}) RETURN a")
	public Optional<Audit> getAuditForAuditRecord(@Param("audit_record_key") String audit_record_key, @Param("audit_key") String audit_key);

	/**
	 * Retrieves a specific audit associated with an audit record by their IDs.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @param audit_id the ID of the audit
	 * @return Optional containing the audit if found and associated
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>audit_id > 0</li>
	 *   <li>Both IDs must exist in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>If the audit exists and is associated with the record, returns Optional containing the audit</li>
	 *   <li>If the audit doesn't exist or isn't associated, returns empty Optional</li>
	 * </ul>
	 */
	@Query("MATCH (ar:PageAuditRecord)-[:HAS]->(a:Audit) WHERE id(ar)=$audit_record_id AND id(a)=$audit_id RETURN a")
	public Optional<Audit> getAuditForAuditRecord(@Param("audit_record_id") long audit_record_id, @Param("audit_id") long audit_id);

	/**
	 * Adds an audit to an audit record by their keys.
	 *
	 * @param audit_record_key the key of the audit record
	 * @param audit_key the key of the audit to add
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 *   <li>audit_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>A HAS relationship is created between the audit record and audit if it doesn't exist</li>
	 *   <li>The relationship is created only if both nodes exist</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key}) WITH ar MATCH (a:Audit{key:$audit_key}) MERGE (ar)-[h:HAS]->(a) RETURN ar")
	public void addAudit(@Param("audit_record_key") String audit_record_key, @Param("audit_key") String audit_key);
	
	/**
	 * Adds an audit to an audit record by their IDs.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @param audit_id the ID of the audit to add
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>audit_id > 0</li>
	 *   <li>Both IDs must exist in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>A HAS relationship is created between the audit record and audit if it doesn't exist</li>
	 *   <li>The relationship is created only if both nodes exist</li>
	 * </ul>
	 *
	 */
	@Query("MATCH (ar:PageAuditRecord) WITH ar MATCH (a:Audit) WHERE id(ar)=$audit_record_id AND id(a)=$audit_id MERGE (ar)-[h:HAS]->(a) RETURN ar")
	public void addAudit(@Param("audit_record_id") long audit_record_id, @Param("audit_id") long audit_id);
	
	/**
	 * Adds a page audit record to a domain audit record by key.
	 *
	 * @param domain_audit_record_id the ID of the domain audit record
	 * @param page_audit_key the key of the page audit record to add
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domain_audit_record_id > 0</li>
	 *   <li>page_audit_key != null</li>
	 *   <li>domain_audit_record_id exists in the database</li>
	 *   <li>page_audit_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>A HAS relationship is created between the domain audit record and page audit record</li>
	 *   <li>The relationship is created only if both nodes exist</li>
	 * </ul>
	 */
	@Query("MATCH (dar:DomainAuditRecord) WITH dar MATCH (par:PageAuditRecord{key:$page_audit_key}) WHERE id(dar)=$domain_audit_record_id MERGE (dar)-[h:HAS]->(par) RETURN dar")
	public void addPageAuditRecord(@Param("domain_audit_record_id") long domain_audit_record_id, @Param("page_audit_key") String page_audit_key);

	/**
	 * Adds a page audit record to a domain audit record by IDs.
	 *
	 * @param domain_audit_record_id the ID of the domain audit record
	 * @param page_audit_record_id the ID of the page audit record to add
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domain_audit_record_id > 0</li>
	 *   <li>page_audit_record_id > 0</li>
	 *   <li>Both IDs must exist in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>A HAS relationship is created between the domain audit record and page audit record</li>
	 *   <li>The relationship is created only if both nodes exist</li>
	 * </ul>
	 */
	@Query("MATCH (dar:DomainAuditRecord) WITH dar MATCH (par:PageAuditRecord) WHERE id(dar)=$domain_audit_record_id AND id(par)=$page_audit_id MERGE (dar)-[h:HAS]->(par) RETURN dar")
	public void addPageAuditRecord(@Param("domain_audit_record_id") long domain_audit_record_id, @Param("page_audit_id") long page_audit_record_id);
	
	/**
	 * Retrieves all audits associated with an audit record.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @return set of all associated audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>audit_record_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all audits associated with the audit record</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord)-[]->(audit:Audit) WHERE id(ar)=$audit_record_id RETURN audit")
	public Set<Audit> getAllAudits(@Param("audit_record_id") long audit_record_id);

	/**
	 * Finds the most recent domain audit record for a domain.
	 *
	 * @param domain_id the ID of the domain
	 * @return Optional containing the most recent domain audit record
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domain_id > 0</li>
	 *   <li>domain_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>If records exist, returns Optional containing the most recent one</li>
	 *   <li>If no records exist, returns empty Optional</li>
	 *   <li>The returned record must be the most recent based on created_at timestamp</li>
	 * </ul>
	 */
	@Query("MATCH (d:Domain)-[]-(ar:DomainAuditRecord) WHERE id(d)=$domain_id RETURN ar ORDER BY ar.created_at DESC LIMIT 1")
	public Optional<DomainAuditRecord> findMostRecentDomainAuditRecord(@Param("domain_id") long domain_id);

	/**
	 * Retrieves all color management audits for a domain-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all color management audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all color management audits at domain level</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must have category='Color Management' and level='domain'</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{category:'Color Management'}) WHERE audit.level='domain' RETURN audit")
	public Set<Audit> getAllColorManagementAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all visual audits for a domain-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all visual audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all visual audits at domain level</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must have category='Visuals' and level='domain'</li>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{category:'Visuals'}) WHERE audit.level='domain' RETURN audit")
	public Set<Audit> getAllVisualAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all page color palette audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page color palette audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page color palette audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Color Palette'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageColorPaletteAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all page text color contrast audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page text color contrast audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page text color contrast audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Text Background Contrast'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageTextColorContrastAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all page non-text color contrast audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page non-text color contrast audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page non-text color contrast audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Non Text Background Contrast'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageNonTextColorContrastAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all typography audits for a domain-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all typography audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all typography audits at domain level</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must have category='Typography' and level='domain'</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{category:'Typography'}) WHERE audit.level='domain' RETURN audit")
	public Set<Audit> getAllTypographyAudits(@Param("audit_record_key") String key);

	/**
	 * Retrieves all page typeface audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page typeface audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page typeface audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Typefaces'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageTypefaceAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all information architecture audits for a domain-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all information architecture audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all information architecture audits at domain level</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must have category='Information Architecture' and level='domain'</li>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{category:'Information Architecture'}) WHERE audit.level='domain' RETURN audit")
	public Set<Audit> getAllInformationArchitectureAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all page link audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page link audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page link audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must have category='Links' and level='page'</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Links'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageLinkAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all page title and header audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page title and header audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page title and header audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must have category='Titles' and level='page'</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Titles'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageTitleAndHeaderAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all page alt text audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page alt text audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page alt text audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Alt Text'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageAltTextAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all page margin audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page margin audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page margin audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Margin'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageMarginAudits(@Param("audit_record_key") String audit_record_key);
	
	/**
	 * Retrieves all page padding audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page padding audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page padding audits</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Padding'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPagePaddingAudits(@Param("audit_record_key") String audit_record_key);

	/**
	 * Retrieves all page paragraphing audits for a page-level audit record.
	 *
	 * @param audit_record_key the key of the audit record
	 * @return set of all page paragraphing audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_key != null</li>
	 *   <li>audit_record_key exists in the database</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord{key:$audit_record_key})-[]->(audit:Audit{subcategory:'Paragraphing'}) WHERE audit.level='page' RETURN audit")
	public Set<Audit> getAllPageParagraphingAudits(@Param("audit_record_key") String audit_record_key);
	
	/**
	 * Retrieves all audits associated with a domain audit record.
	 *
	 * @param domain_audit_id the ID of the domain audit record
	 * @return set of all associated audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domain_audit_id > 0</li>
	 *   <li>domain_audit_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all audits associated with the domain audit record</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (dar:DomainAuditRecord)-[:HAS]->(audit:PageAuditRecord) WHERE id(dar)=$domain_audit_id RETURN audit")
	public Set<PageAuditRecord> getAllPageAudits(@Param("domain_audit_id") long domain_audit_id);

	/**
	 * Retrieves all audits for a page audit record.
	 *
	 * @param page_audit_id the ID of the page audit record
	 * @return set of all associated audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>page_audit_id > 0</li>
	 *   <li>page_audit_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all audits associated with the page audit record</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 */
	@Query("MATCH (page_audit:PageAuditRecord)-[]->(audit:Audit) OPTIONAL MATCH auditsAndMessages=(audit)-->(:UXIssueMessage) WHERE id(page_audit)=$page_audit_id RETURN auditsAndMessages")
	public Set<Audit> getAllAuditsForPageAuditRecord(@Param("page_audit_id") long page_audit_id);

	/**
	 * Retrieves the most recent page audit record for a given URL.
	 *
	 * @param url the URL to find the audit record for
	 * @return Optional containing the most recent page audit record
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>url != null</li>
	 *   <li>url is a valid URL format</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>If a record exists for the URL, returns Optional containing the most recent one</li>
	 *   <li>If no record exists, returns empty Optional</li>
	 *   <li>The returned record must be the most recent based on created_at timestamp</li>
	 * </ul>
	 */
	@Query("MATCH (page_audit:PageAuditRecord)-[]->(page_state:PageState{url:$url}) RETURN page_audit ORDER BY page_audit.created_at DESC LIMIT 1")
	public Optional<PageAuditRecord> getMostRecentPageAuditRecord(@Param("url") String url);

	/**
	 * Retrieves the page state associated with a page audit record.
	 *
	 * @param page_audit_key the key of the page audit record
	 * @return the associated page state
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>page_audit_key != null</li>
	 *   <li>page_audit_key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns the page state associated with the audit record</li>
	 *   <li>If no page state exists, returns null</li>
	 *   <li>The returned page state must be valid and complete</li>
	 */
	@Query("MATCH (page_audit:PageAuditRecord{key:$page_audit_key})-[]->(page_state:PageState) RETURN page_state LIMIT 1")
	public PageState getPageStateForAuditRecord(@Param("page_audit_key") String page_audit_key);

	/**
	 * Retrieves the page state associated with a page audit record.
	 *
	 * @param page_audit_id the ID of the page audit record
	 * @return the associated page state
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>page_audit_id > 0</li>
	 *   <li>page_audit_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns the page state associated with the audit record</li>
	 *   <li>If no page state exists, returns null</li>
	 *   <li>The returned page state must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (page_audit:PageAuditRecord)-[]->(page_state:PageState) WHERE id(page_audit)=$page_audit_id RETURN page_state LIMIT 1")
	public PageState getPageStateForAuditRecord(@Param("page_audit_id") long page_audit_id);
	
	/**
	 * Retrieves all page states associated with a domain audit record.
	 *
	 * @param domain_audit_id the ID of the domain audit record
	 * @return set of all associated page states
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domain_audit_id > 0</li>
	 *   <li>domain_audit_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all page states associated with the domain audit record</li>
	 *   <li>If no page states exist, returns an empty set</li>
	 *   <li>All returned page states must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (domain_audit:DomainAuditRecord)-[]->(page_state:PageState) WHERE id(domain_audit)=$domain_audit_id RETURN page_state")
	public Set<PageState> getPageStatesForDomainAuditRecord(@Param("domain_audit_id") long domain_audit_id);
	
	/**
	 * Retrieves the most recent audits for a page.
	 *
	 * @param key the key of the page
	 * @return set of all associated audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>key != null</li>
	 *   <li>key exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all audits associated with the page</li>
	 *   <li>If no audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (page_audit:PageAuditRecord)-[]->(page_state:PageState{key:$page_key}) MATCH (page_audit)-[]->(audit:Audit) RETURN audit")
	public Set<Audit> getMostRecentAuditsForPage(@Param("page_key") String key);

	/**
	 * Retrieves all content audits for a domain audit record.
	 *
	 * @param id the ID of the domain audit record
	 * @return set of all associated content audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>id > 0</li>
	 *   <li>id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all content audits associated with the domain audit record</li>
	 *   <li>If no content audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (ar:DomainAuditRecord)-[]->(par:PageAuditRecord) MATCH (par)-[]->(audit:Audit{category:'Content'}) WHERE id(ar)=$id RETURN audit")
	public Set<Audit> getAllContentAuditsForDomainRecord(@Param("id") long id);

	/**
	 * Retrieves all information architecture audits for a domain audit record.
	 *
	 * @param id the ID of the domain audit record
	 * @return set of all associated information architecture audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>id > 0</li>
	 *   <li>id exists in the database</li>
	 */
	@Query("MATCH (ar:DomainAuditRecord)-[]->(par:PageAuditRecord) MATCH (par)-[]->(audit:Audit{category:'Information Architecture'})  WHERE id(ar)=$id RETURN audit")
	public Set<Audit> getAllInformationArchitectureAuditsForDomainRecord(@Param("id") long id);

	/**
	 * Retrieves all aesthetics audits for a domain audit record.
	 *
	 * @param id the ID of the domain audit record
	 * @return set of all associated aesthetics audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>id > 0</li>
	 *   <li>id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all aesthetics audits associated with the domain audit record</li>
	 *   <li>If no aesthetics audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (ar:DomainAuditRecord)-[]->(par:PageAuditRecord) MATCH (par)-[]->(audit:Audit{category:'Aesthetics'}) WHERE id(ar) = $id RETURN audit")
	public Set<Audit> getAllAestheticsAuditsForDomainRecord(@Param("id") long id);

	/**
	 * Retrieves all content audits for an audit record.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @return set of all associated content audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>audit_record_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all content audits associated with the audit record</li>
	 *   <li>If no content audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord)-[]->(audit:Audit{category:'Content'}) WHERE id(ar)=$audit_record_id RETURN audit")
	public Set<Audit> getAllContentAudits(@Param("audit_record_id") long audit_record_id);

	/**
	 * Retrieves all information architecture audits for an audit record.
	 *
	 * @param id the ID of the audit record
	 * @return set of all associated information architecture audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>id > 0</li>
	 *   <li>id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all information architecture audits associated with the audit record</li>
	 *   <li>If no information architecture audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord)-[]->(audit:Audit{category:'Information Architecture'})  WHERE id(ar)=$id RETURN audit")
	public Set<Audit> getAllInformationArchitectureAudits(@Param("id") long id);

	/**
	 * Retrieves all aesthetics audits for an audit record.
	 *
	 * @param id the ID of the audit record
	 * @return set of all associated aesthetics audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>id > 0</li>
	 *   <li>id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all aesthetics audits associated with the audit record</li>
	 *   <li>If no aesthetics audits exist, returns an empty set</li>
	 *   <li>All returned audits must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord)-[]->(audit:Audit{category:'Aesthetics'}) WHERE id(ar)=$id RETURN audit")
	public Set<Audit> getAllAestheticsAudits(@Param("id") long id);

	/**
	 * Retrieves all accessibility audits for a domain audit record.
	 *
	 * @param domain_audit_id the ID of the domain audit record
	 * @return set of all associated accessibility audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domain_audit_id > 0</li>
	 *   <li>domain_audit_id exists in the database</li>
	 */
	@Query("MATCH (dar:DomainAuditRecord)-[]->(par:PageAuditRecord) MATCH (par)-[]->(audit:Audit{is_accessibility:true}) WHERE id(dar)=$domain_audit_id RETURN audit")
	public Set<Audit> getAllAccessibilityAuditsForDomainRecord(@Param("domain_audit_id") long domain_audit_id);

	/**
	 * Retrieves all accessibility audits for a page audit record.
	 *
	 * @param page_audit_id the ID of the page audit record
	 * @return set of all accessibility audits
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>page_audit_id > 0</li>
	 *   <li>page_audit_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all accessibility audits (is_accessibility=true)</li>
	 *   <li>If no accessibility audits exist, returns an empty set</li>
	 *   <li>All returned audits must have is_accessibility=true</li>
	 * </ul>
	 */
	@Query("MATCH (par:PageAuditRecord)-[]->(audit:Audit{is_accessibility:true}) WHERE id(par)=$page_audit_id RETURN audit")
	public Set<Audit> getAllAccessibilityAudits(@Param("page_audit_id") long page_audit_id);

	/**
	 * Retrieves all UX issue messages for a page audit record.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @return set of all associated UX issue messages
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>audit_record_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all UX issue messages associated with the audit record</li>
	 *   <li>If no issues exist, returns an empty set</li>
	 *   <li>All returned messages must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (audit_record:PageAuditRecord)-[]-(audit:Audit)  MATCH (audit)-[:HAS]-(issue:UXIssueMessage) WHERE id(audit_record)=$audit_record_id RETURN issue")
	public Set<UXIssueMessage> getIssues(@Param("audit_record_id") long audit_record_id);

	/**
	 * Adds a page to an audit record.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @param page_state_id the ID of the page state to add
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>page_state_id > 0</li>
	 *   <li>Both IDs must exist in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>A FOR relationship is created between the audit record and page state</li>
	 *   <li>The relationship is created only if both nodes exist</li>
	 * </ul>
	 */
	@Query("MATCH (ar:AuditRecord) WITH ar MATCH (page:PageState) WHERE id(ar)=$audit_record_id AND id(page)=$page_state_id MERGE (ar)-[h:FOR]->(page) RETURN ar")
	public void addPageToAuditRecord(@Param("audit_record_id") long audit_record_id, @Param("page_state_id") long page_state_id);

	/**
	 * Counts issues of a specific severity for a page audit record.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @param severity the severity level to count
	 * @return the count of issues with the specified severity
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>severity != null</li>
	 *   <li>audit_record_id exists in the database</li>
	 *   <li>severity is a valid priority value</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns the count of issues with the specified severity</li>
	 *   <li>If no issues exist, returns 0</li>
	 *   <li>The count must be non-negative</li>
	 * </ul>
	 */
	@Query("MATCH (audit_record:PageAuditRecord)-[]-(audit:Audit) MATCH (audit)-[:HAS]-(issue:UXIssueMessage{priority:$severity}) WHERE id(audit_record)=$audit_record_id RETURN count(issue) as count")
	public long getIssueCountBySeverity(@Param("audit_record_id") long id, @Param("severity") String severity);

	/**
	 * Counts the number of page audit records in a domain audit record.
	 *
	 * @param domain_audit_id the ID of the domain audit record
	 * @return the count of page audit records
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domain_audit_id > 0</li>
	 *   <li>domain_audit_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns the count of page audit records</li>
	 *   <li>If no records exist, returns 0</li>
	 *   <li>The count must be non-negative</li>
	 * </ul>
	 */
	@Query("MATCH (audit_record:DomainAuditRecord)-[]->(page_audit:PageAuditRecord) WHERE id(audit_record)=$audit_record_id RETURN count(page_audit) as count")
	public int getPageAuditRecordCount(@Param("audit_record_id") long domain_audit_id);

	/**
	 * Retrieves the domain audit record associated with a page audit record.
	 *
	 * @param audit_record_id the ID of the page audit record
	 * @return Optional containing the associated domain audit record
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>audit_record_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>If a domain audit record exists, returns Optional containing it</li>
	 *   <li>If no domain audit record exists, returns empty Optional</li>
	 *   <li>The returned record must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (doman_audit:DomainAuditRecord)-[:HAS]->(page_audit:PageAuditRecord) WHERE id(page_audit)=$audit_record_id RETURN doman_audit LIMIT 1")
	public Optional<DomainAuditRecord> getDomainForPageAuditRecord(@Param("audit_record_id") long audit_record_id);

	/**
	 * Retrieves all labels for image elements in an audit record.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @return set of all associated labels
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>audit_record_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>Returns a set of all labels associated with image elements</li>
	 *   <li>If no labels exist, returns an empty set</li>
	 *   <li>All returned labels must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (audit_record:AuditRecord) WITH audit_record WHERE id(audit_record)=$audit_record_id MATCH (audit_record)-[*]->(element:ImageElementState) MATCH (element)-[]->(label:Label) RETURN label")
	public Set<Label> getLabelsForImageElements(@Param("audit_record_id") long id);

	/**
	 * Retrieves the design system detected for an audit record.
	 *
	 * @param audit_record_id the ID of the audit record
	 * @return Optional containing the detected design system
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>audit_record_id exists in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>If a design system exists, returns Optional containing it</li>
	 *   <li>If no design system exists, returns empty Optional</li>
	 *   <li>The returned design system must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (audit_record:AuditRecord) WITH audit_record MATCH (audit_record)-[:DETECTED]->(design_system:DesignSystem) WHERE id(audit_record)=$audit_record_id RETURN design_system")
	public Optional<DesignSystem> getDesignSystem(@Param("audit_record_id") long audit_record_id);

	/**
	 * Adds a journey to a domain audit record.
	 *
	 * @param audit_record_id the ID of the domain audit record
	 * @param journey_id the ID of the journey to add
	 * @return the updated audit record
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>journey_id > 0</li>
	 *   <li>Both IDs must exist in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>A HAS_PATH relationship is created between the domain audit record and journey</li>
	 *   <li>The relationship is created only if both nodes exist</li>
	 *   <li>Returns the updated audit record</li>
	 * </ul>
	 */
	@Query("MATCH (ar:DomainAuditRecord) WITH ar MATCH (journey:Journey) WHERE id(ar)=$audit_record_id AND id(journey)=$journey_id MERGE (ar)-[:HAS_PATH]->(journey) RETURN ar")
	public AuditRecord addJourney(@Param("audit_record_id") long audit_record_id, @Param("journey_id") long journey_id);

	/**
	 * Finds a page with a specific URL in a domain audit record.
	 *
	 * @param audit_record_id the ID of the domain audit record
	 * @param page_url the URL to find
	 * @return the page state if found, null otherwise
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>audit_record_id > 0</li>
	 *   <li>page_url != null</li>
	 *   <li>audit_record_id exists in the database</li>
	 *   <li>page_url is a valid URL format</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>If a page exists with the URL, returns it</li>
	 *   <li>If no page exists, returns null</li>
	 *   <li>The returned page must be valid and complete</li>
	 * </ul>
	 */
	@Query("MATCH (audit_record:DomainAuditRecord) WITH audit_record WHERE id(audit_record)=$audit_record_id MATCH (audit_record)-[:HAS]->(page:PageState) WHERE page.url=$page_url RETURN page")
	public PageState findPageWithUrl(@Param("audit_record_id") long audit_record_id, @Param("page_url") String page_url);

	/**
	 * Adds a domain map to a domain audit record.
	 *
	 * @param domainId the ID of the domain audit record
	 * @param domainMapId the ID of the domain map to add
	 *
	 * <p><b>Preconditions:</b>
	 * <ul>
	 *   <li>domainId > 0</li>
	 *   <li>domainMapId > 0</li>
	 *   <li>Both IDs must exist in the database</li>
	 * </ul>
	 *
	 * <p><b>Postconditions:</b>
	 * <ul>
	 *   <li>A CONTAINS relationship is created between the domain audit record and domain map</li>
	 *   <li>The relationship is created only if both nodes exist</li>
	 * </ul>
	 */
	@Query("MATCH (ar:DomainAuditRecord) WITH ar MATCH (map:DomainMap) WHERE id(ar)=$audit_record_id AND id(map)=$map_id MERGE (ar)-[:CONTAINS]->(map) RETURN ar")
	public void addDomainMap(@Param("audit_record_id") long domainId, @Param("map_id") long domainMapId);
}
