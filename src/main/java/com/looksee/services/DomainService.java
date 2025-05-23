package com.looksee.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.looksee.models.AuditRecord;
import com.looksee.models.DesignSystem;
import com.looksee.models.Domain;
import com.looksee.models.DomainAuditRecord;
import com.looksee.models.Element;
import com.looksee.models.Form;
import com.looksee.models.PageState;
import com.looksee.models.TestUser;
import com.looksee.models.repository.DomainRepository;

import lombok.NoArgsConstructor;

/**
 * Service class for managing domain-related operations
 *
 * This class provides methods to retrieve domains, test users, and related entities
 * from the database. It also includes methods for adding and deleting test users,
 * retrieving test counts, and saving domains.
 *
 * This class collaborates with:
 * - DomainRepository to interact with the database
 * - AuditRecord to manage audit records
 * - DesignSystem to manage design systems
 * - PageState to manage page states
 * - TestUser to manage test users
 * - Form to manage forms
 */
@NoArgsConstructor
@Service
public class DomainService {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DomainRepository domain_repo;

	/**
	 * Retrieves all domains
	 *
	 * @return A set of all domains
	 */
	public Set<Domain> getDomains() {
		return domain_repo.getDomains();
	}
	
	/**
	 * Retrieves all test users for a given domain ID
	 *
	 * @param domain_id The ID of the domain to retrieve test users for
	 * @return A set of all test users for the given domain
	 */
	public Set<TestUser> getTestUsers(long domain_id) {
		return domain_repo.getTestUsers(domain_id);
	}

	/**
	 * Finds a domain by its host for a given user
	 *
	 * @param host The host of the domain to find
	 * @param username The username of the user to find the domain for
	 * @return The domain if found, or null if not found
	 *
	 * precondition: host != null
	 * precondition: !host.isEmpty()
	 * precondition: username != null
	 * precondition: !username.isEmpty()
	 */
	public Domain findByHostForUser(String host, String username) {
		assert host != null;
		assert !host.isEmpty();
		assert username != null;
		assert !username.isEmpty();

		return domain_repo.findByHostForUser(host, username);
	}
	
	/**
	 * Finds a domain by its host
	 *
	 * @param host The host of the domain to find
	 * @return The domain if found, or null if not found
	 *
	 * precondition: host != null
	 * precondition: !host.isEmpty()
	 */
	public Domain findByHost(String host) {
		assert host != null;
		assert !host.isEmpty();

		return domain_repo.findByHost(host);
	}

	/**
	 * Finds a domain by its URL
	 *
	 * @param url The URL of the domain to find
	 * @return The domain if found, or null if not found
	 *
	 * precondition: url != null
	 * precondition: !url.isEmpty()
	 */
	public Domain findByUrl(String url) {
		assert url != null;
		assert !url.isEmpty();

		return domain_repo.findByUrl(url);
	}
	
	/**
	 * Saves a domain
	 *
	 * @param domain The domain to save
	 * @return The saved domain
	 */
	public Domain save(Domain domain) {
		assert domain != null;
		return domain_repo.save(domain);
	}
	
	/**
	 * Retrieves the number of tests for a given account and URL
	 *
	 * @param account_id The ID of the account to retrieve tests for
	 * @param url The URL to retrieve tests for
	 * @return The number of tests for the given account and URL
	 *
	 * precondition: url != null
	 * precondition: !url.isEmpty()
	 */
	public int getTestCount(long account_id, String url) {
		assert url != null;
		assert !url.isEmpty();

		return domain_repo.getTestCount(account_id, url);
	}

	/**
	 * Finds a domain by its ID
	 *
	 * @param domain_id The ID of the domain to find
	 * @return An optional containing the domain if found, or empty if not found
	 */
	public Optional<Domain> findById(long domain_id) {
		return domain_repo.findById(domain_id);
	}

	/**
	 * Deletes a test user from a domain
	 *
	 * @param domain_id The ID of the domain to delete the test user from
	 * @param user_id The ID of the test user to delete
	 * @return true if the test user was deleted successfully, false otherwise
	 */
	public boolean deleteTestUser(long domain_id, long user_id) {
		return domain_repo.deleteTestUser(domain_id, user_id) > 0;
	}

	/**
	 * Retrieves all forms for a given account and URL
	 *
	 * @param account_id The ID of the account to retrieve forms for
	 * @param url The URL to retrieve forms for
	 * @return A set of all forms for the given account and URL
	 *
	 * precondition: url != null
	 * precondition: !url.isEmpty()
	 */
	public Set<Form> getForms(long account_id, String url) {
		assert url != null;
		assert !url.isEmpty();

		return domain_repo.getForms(account_id, url);
	}
	
	/**
	 * Retrieves the number of forms for a given account and URL
	 *
	 * @param account_id The ID of the account to retrieve forms for
	 * @param url The URL to retrieve forms for
	 * @return The number of forms for the given account and URL
	 *
	 * precondition: url != null
	 * precondition: !url.isEmpty()
	 */
	public int getFormCount(long account_id, String url) {
		assert url != null;
		assert !url.isEmpty();
		return domain_repo.getFormCount(account_id, url);
	}

	/**
	 * Retrieves all element states for a given URL and username
	 *
	 * @param url The URL to retrieve element states for
	 * @param username The username to retrieve element states for
	 * @return A set of all element states for the given URL and username
	 *
	 * precondition: url != null
	 * precondition: !url.isEmpty()
	 * precondition: username != null
	 * precondition: !username.isEmpty()
	 */
	public Set<Element> getElementStates(String url, String username) {
		assert url != null;
		assert !url.isEmpty();
		assert username != null;
		assert !username.isEmpty();

		return domain_repo.getElementStates(url, username);
	}

	/**
	 * Retrieves all page states for a given domain ID
	 *
	 * @param domain_id The ID of the domain to retrieve page states for
	 * @return A set of all page states for the given domain
	 */
	public Set<PageState> getPageStates(long domain_id) {
		return domain_repo.getPageStates(domain_id);
	}

	/**
	 * Finds a domain by its key and username
	 *
	 * @param key The key of the domain to find
	 * @param username The username of the domain to find
	 * @return The domain if found, or null if not found
	 *
	 * precondition: key != null
	 * precondition: !key.isEmpty()
	 * precondition: username != null
	 * precondition: !username.isEmpty()
	 */
	public Domain findByKey(String key, String username) {
		assert key != null;
		assert !key.isEmpty();
		assert username != null;
		assert !username.isEmpty();

		return domain_repo.findByKey(key, username);
	}

	/**
	 * Adds a page to a domain
	 *
	 * @param domain_id The ID of the domain to add the page to
	 * @param page_id The ID of the page to add to the domain
	 * 
	 * @return true if the page was added successfully, false otherwise
	 */
	public boolean addPage(long domain_id, long page_id) {
		//check if page already exists. If it does then return true;
		Optional<PageState> page = domain_repo.getPage(domain_id, page_id);
		if(page.isPresent()) {
			return true;
		}
		
		return domain_repo.addPage(domain_id, page_id) != null;
	}

	/**
	 * Retrieves the most recent audit record for a given domain ID
	 *
	 * @param id The ID of the domain to retrieve the most recent audit record for
	 * @return An optional containing the most recent audit record, or empty if no record exists
	 *
	 */
	public Optional<DomainAuditRecord> getMostRecentAuditRecord(long id) {
		return domain_repo.getMostRecentAuditRecord(id);
	}

	/**
	 * Retrieves all page states for a given domain host
	 *
	 * @param domain_host The host of the domain to retrieve page states for
	 * @return A set of all page states for the given domain host
	 */
	public Set<PageState> getPages(String domain_host) {
		return domain_repo.getPages(domain_host);
	}

	/**
	 * Finds a domain by its page state key
	 *
	 * @param page_state_key The key of the page state to find
	 * @return The domain if found, or null if not found
	 */
	public Domain findByPageState(String page_state_key) {
		return domain_repo.findByPageState(page_state_key);
	}

	/**
	 * Adds an audit record to a domain
	 *
	 * @param domain_id The ID of the domain to add the audit record to
	 * @param audit_record_key The key of the audit record to add
	 *
	 * precondition: audit_record_key != null
	 * precondition: !audit_record_key.isEmpty()
	 */
	public void addAuditRecord(long domain_id, String audit_record_key) {
		assert audit_record_key != null;
		assert !audit_record_key.isEmpty();

		domain_repo.addAuditRecord(domain_id, audit_record_key);
	}

	/**
	 * Retrieves all audit records for a given domain key
	 *
	 * @param domain_key The key of the domain to retrieve audit records for
	 * @return A set of all audit records for the given domain key
	 */
	public Set<AuditRecord> getAuditRecords(String domain_key) {
		return domain_repo.getAuditRecords(domain_key);
	}

	/**
	 * Finds a domain by its audit record ID
	 *
	 * @param audit_record_id The ID of the audit record to find
	 * @return The domain if found, or null if not found
	 */
	public Domain findByAuditRecord(long audit_record_id) {
		return domain_repo.findByAuditRecord(audit_record_id);
	}

	/**
	 * Updates the expertise settings for a domain
	 *
	 * @param domain_id The ID of the domain to update the expertise settings for
	 * @param expertise The expertise settings to update the domain with
	 * @return The updated domain
	 */
	public DesignSystem updateExpertiseSettings(long domain_id, String expertise) {
		return domain_repo.updateExpertiseSetting(domain_id, expertise);
	}

	/**
	 * Retrieves the audit record history for a given domain ID
	 *
	 * @param domain_id The ID of the domain to retrieve the audit record history for
	 * @return A list of all audit records for the given domain ID
	 */
	public List<DomainAuditRecord> getAuditRecordHistory(long domain_id) {
		return domain_repo.getAuditRecordHistory(domain_id);
	}

	/**
	 * Retrieves the design system for a given domain ID
	 *
	 * @param domain_id The ID of the domain to retrieve the design system for
	 * @return The design system if found, or null if not found
	 */
	public Optional<DesignSystem> getDesignSystem(long domain_id) {
		return domain_repo.getDesignSystem(domain_id);
	}

	/**
	 * Adds a design system to a domain
	 *
	 * @param domain_id The ID of the domain to add the design system to
	 * @param design_system_id The ID of the design system to add to the domain
	 * @return The added design system
	 */
	public DesignSystem addDesignSystem(long domain_id, long design_system_id) {
		return domain_repo.addDesignSystem(domain_id, design_system_id);
	}

	/**
	 * Updates the WCAG settings for a domain
	 *
	 * @param domain_id The ID of the domain to update the WCAG settings for
	 * @param wcag_level The WCAG level to update the domain with
	 * @return The updated design system
	 */
	public DesignSystem updateWcagSettings(long domain_id, String wcag_level) {
		return domain_repo.updateWcagSettings(domain_id, wcag_level);
	}

	/**
	 * Updates the allowed image characteristics for a domain
	 *
	 * @param domain_id The ID of the domain to update the allowed image characteristics for
	 * @param allowed_image_characteristics The allowed image characteristics to update the domain with
	 * @return The updated design system
	 */
	public DesignSystem updateAllowedImageCharacteristics(long domain_id, List<String> allowed_image_characteristics) {
		return domain_repo.updateAllowedImageCharacteristics(domain_id, allowed_image_characteristics);
	}

	/**
	 * Retrieves all test users for a given domain ID
	 *
	 * @param domain_id The ID of the domain to retrieve test users for
	 * @return A list of all test users for the given domain ID
	 */
	public List<TestUser> findTestUsers(long domain_id) {
		return domain_repo.findTestUsers(domain_id);
	}

	/**
	 * Adds a test user to a domain
	 *
	 * @param domain_id The ID of the domain to add the test user to
	 * @param test_user_id The ID of the test user to add to the domain
	 */
	public void addTestUser(long domain_id, long test_user_id) {
		domain_repo.addTestUser(domain_id, test_user_id);
	}
}
