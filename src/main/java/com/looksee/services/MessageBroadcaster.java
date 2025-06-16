package com.crawlerApi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crawlerApi.dto.DomainDto;
import com.crawlerApi.dto.TestCreatedDto;
import com.crawlerApi.dto.TestDto;
import com.crawlerApi.dto.TestRecordDto;
import com.crawlerApi.models.Account;
import com.crawlerApi.models.DiscoveryRecord;
import com.crawlerApi.models.Domain;
import com.crawlerApi.models.Form;
import com.crawlerApi.models.LookseeObject;
import com.crawlerApi.models.Test;
import com.crawlerApi.models.TestRecord;
import com.crawlerApi.models.audit.Audit;
import com.crawlerApi.models.audit.AuditRecord;
import com.crawlerApi.models.audit.AuditStats;
import com.crawlerApi.models.audit.UXIssueMessage;
import com.crawlerApi.models.enums.TestStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pusher.rest.Pusher;

/**
 * Defines methods for emitting data to subscribed clients
 */
public class MessageBroadcaster {
	private static Logger log = LoggerFactory.getLogger(MessageBroadcaster.class);
	
	@Value("${pusher.appId}")
	private String appId;

	@Value("${pusher.key}")
	private String key;

	@Value("${pusher.secret}")
	private String secret;

	@Value("${pusher.cluster}")
	private String cluster;
	
	private static Pusher pusher = new Pusher(appId, key, secret);
	
	static{
		pusher.setCluster(cluster);
		pusher.setEncrypted(true);
	}
	
	/**
     * Message emitter that sends {@link Test} to all registered clients
     * 
     * @param test {@link Test} to be emitted to clients
     * @throws JsonProcessingException
     */
	public static void broadcastAudit(String host, Audit audit) throws JsonProcessingException {
        //Object to JSON in String
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String audit_json = mapper.writeValueAsString(audit);

		pusher.trigger(host, "audit-update", audit_json);
	}
	
	/**
     * Message emitter that sends {@link Test} to all registered clients
     * 
     * @param test {@link Test} to be emitted to clients
     * @throws JsonProcessingException if there is an error converting the account to a JSON string
     */
	/**
	 * send {@link Account} to the users pusher channel
	 * @param account {@link Account} to be emitted to clients
	 * @throws JsonProcessingException if there is an error converting the account to a JSON string
	 */
	public static void broadcastSubscriptionExceeded(Account account) throws JsonProcessingException {
        //Object to JSON in String
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        int id_start_idx = account.getUserId().indexOf('|');
		String user_id = account.getUserId().substring(id_start_idx+1);
        
		pusher.trigger(user_id, "subscription-exceeded", "");
	}
	
    /**
     * Message emitter that sends {@link Test} to all registered clients
     * 
     * @param test {@link Test} to be emitted to clients
     * @throws JsonProcessingException if there is an error converting the test to a JSON string
     */
	public static void broadcastDiscoveredTest(Test test, String host, String user_id) throws JsonProcessingException {
        //Object to JSON in String
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String test_json = mapper.writeValueAsString(test);

		pusher.trigger(user_id+host, "test-discovered", test_json);
	}

    /**
     * Message emitter that sends {@link Form} to all registered clients
     * 
     * @param test {@link Test} to be emitted to clients
     * @throws JsonProcessingException if there is an error converting the form to a JSON string
     */
	public static void broadcastDiscoveredForm(Form form, long domain_id) throws JsonProcessingException {
		log.info("Broadcasting discovered form !!!");
		
        //Object to JSON in String
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String form_json = mapper.writeValueAsString(form);
        
		pusher.trigger(""+domain_id, "discovered-form", form_json);
		log.info("broadcasted a discovered form");
	}
	
	/**
     * Message emitter that sends {@link Test} to all registered clients
     * 
     * @param test {@link Test} to be emitted to clients
     * @throws JsonProcessingException if there is an error converting the test to a JSON string
     */
	public static void broadcastTest(Test test, String host) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        //Object to JSON in String
        String test_json = mapper.writeValueAsString(test);
        log.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.warn("host :: " + host);
        log.warn("TEST JSON :: " + test_json);
        log.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		
        pusher.trigger(host, "test", test_json);
	}
	
	/**
     * Message emitter that sends {@link Test} to all registered clients
     * 
     * @param test {@link Test} to be emitted to clients
     * @throws JsonProcessingException if there is an error converting the path object to a JSON string
     */
	public static void broadcastPathObject(LookseeObject path_object, String host, String user_id) throws JsonProcessingException {	
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        //Object to JSON in String
        String path_object_json = mapper.writeValueAsString(path_object);
        
		pusher.trigger(user_id+host, "path_object", path_object_json);
	}
	
	/**
     * Message emitter that sends {@link TestStatus to all registered clients
     * 
     * @param test {@link Test} to be emitted to clients
     * @throws JsonProcessingException if there is an error converting the test record to a JSON string
     */
	public static void broadcastTestStatus(String host, TestRecord record, Test test, String user_id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        //Object to JSON in String
        String test_json = mapper.writeValueAsString(new TestRecordDto(record, test.getKey()));
        
		pusher.trigger(user_id+host, "test-run", test_json);
	}
	
	/**
     * Message emitter that sends {@link DiscoveryRecord} to all registered clients
     * 
     * @param record {@link DiscoveryRecord} to be emitted to clients
     * @throws JsonProcessingException if there is an error converting the discovery record to a JSON string
     */
	public static void broadcastDiscoveryStatus(DiscoveryRecord record) throws JsonProcessingException {
		log.info("broadcasting discovery status");
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        //Object to JSON in String
        String discovery_json = mapper.writeValueAsString(record);
        
		pusher.trigger(record.getDomainUrl(), "discovery-status", discovery_json);
	}

	/**
	 * Uses Pusher service to broadcast json representing {@link Test} to browser extension for the user with
	 *   the given username
	 * @param test_dto representation of {@link Test} that complies with format for browser extensions
	 * @param username username of User
	 * @throws JsonProcessingException if there is an error converting the test dto to a JSON string
	 */
	public static void broadcastIdeTest(TestDto test_dto, String username) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        //Object to JSON in String
        String test_json = mapper.writeValueAsString(test_dto);
        
		pusher.trigger(username, "edit-test", test_json);
	}

	public static void broadcastTestCreatedConfirmation(Test test, String username) throws JsonProcessingException {
		TestCreatedDto test_created_dto = new TestCreatedDto(test);
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		String test_confirmation_json = mapper.writeValueAsString(test_created_dto);
		pusher.trigger(username, "test-created", test_confirmation_json);
	}

	public static void sendDomainAdded(String user_id, Domain domain) throws JsonProcessingException {
		/*
		DomainDto domain_dto = new DomainDto( domain.getId(), 
											  domain.getUrl(), 
											  domain.getPages().size(), 
											  0, 
											  0, 
											  0.0, 
											  0, 
											  0.0, 
											  0, 
											  0.0, 
											  0, 
											  0.0, 
											  false, 
											  0.0,
											  "Domain successfully created",
											  ExecutionStatus.COMPLETE);
		*/
		DomainDto domain_dto = new DomainDto( domain.getId(), domain.getUrl(), 0.01);

		
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		String test_confirmation_json = mapper.writeValueAsString(domain_dto);
		pusher.trigger(user_id.replace("|", ""), "domain-added", test_confirmation_json);
	}

	/**
	 * send {@link AuditStats} to the users pusher channel
	 * @param user_id id of the user
	 * @param audit_record {@link AuditStats} to be emitted to clients
	 * @throws JsonProcessingException if there is an error converting the audit record to a JSON string
	 */
	public static void sendAuditStatUpdate(long user_id, AuditStats audit_record) throws JsonProcessingException {
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		String audit_record_json = mapper.writeValueAsString(audit_record);
		pusher.trigger(user_id+"", "audit-stat-update", audit_record_json);
	}

	public static void sendIssueMessage(long page_id, UXIssueMessage issue) {
		ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		try {
			String audit_record_json = mapper.writeValueAsString(issue);
			pusher.trigger(page_id+"", "ux-issue-added", audit_record_json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * send {@link AuditRecord} to the users pusher channel
	 * @param user_id id of the user
	 * @param domain_dto {@link DomainDto} to be emitted to clients
	 * @throws JsonProcessingException if there is an error converting the domain dto to a JSON string
	 */
	public static void sendAuditRecord(String user_id, DomainDto domain_dto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		String domain_dto_json = mapper.writeValueAsString(domain_dto);
		pusher.trigger(user_id, "audit-record", domain_dto_json);
	}
}
