package com.looksee.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.looksee.models.Account;
import com.looksee.models.DiscoveryRecord;
import com.looksee.models.Domain;
import com.looksee.models.Form;
import com.looksee.models.LookseeObject;
import com.looksee.models.Test;
import com.looksee.models.TestRecord;
import com.looksee.models.Audit;
import com.looksee.models.AuditRecord;
import com.looksee.models.UXIssueMessage;
import com.looksee.models.enums.ExecutionStatus;
import com.looksee.models.dto.DomainDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pusher.rest.Pusher;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * Defines methods for emitting data to subscribed clients
 */
@Service
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

	private Pusher pusher;
	
	/**
	 * constructor for the message broadcaster
	 */
	public MessageBroadcaster() {
		pusher = new Pusher(appId, key, secret);
		pusher.setCluster(cluster);
		pusher.setEncrypted(true);
	}
	
	/**
     * Message emitter that sends {@link Test} to all registered clients
     * 
     * @param host the host of the test
     * @param audit {@link Audit} to be emitted to clients
     * @throws JsonProcessingException
     */
	public void broadcastAudit(String host, Audit audit) throws JsonProcessingException {
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
	public void broadcastSubscriptionExceeded(Account account) throws JsonProcessingException {
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
     * @param host the host of the test
     * @param user_id the user id of the test
     * @throws JsonProcessingException if there is an error converting the test to a JSON string
     */
	public void broadcastDiscoveredTest(Test test, String host, String user_id) throws JsonProcessingException {
        //Object to JSON in String
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String test_json = mapper.writeValueAsString(test);

		pusher.trigger(user_id+host, "test-discovered", test_json);
	}

    /**
     * Message emitter that sends {@link Form} to all registered clients
     * 
     * @param form {@link Form} to be emitted to clients
     * @param domain_id the id of the domain
     * @throws JsonProcessingException if there is an error converting the form to a JSON string
     */
	public void broadcastDiscoveredForm(Form form, long domain_id) throws JsonProcessingException {
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
	 * @param host the host of the test
     * @throws JsonProcessingException if there is an error converting the test to a JSON string
     */
	public void broadcastTest(Test test, String host) throws JsonProcessingException {
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
	 * @param path_object {@link LookseeObject} to be emitted to clients
	 * @param host the host of the path object
	 * @param user_id the user id of the path object
     * @throws JsonProcessingException if there is an error converting the path object to a JSON string
     */
	public void broadcastPathObject(LookseeObject path_object, String host, String user_id) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        //Object to JSON in String
        String path_object_json = mapper.writeValueAsString(path_object);
        
		pusher.trigger(user_id+host, "path_object", path_object_json);
	}
	
	/**
     * Message emitter that sends {@link DiscoveryRecord} to all registered clients
     * 
     * @param record {@link DiscoveryRecord} to be emitted to clients
     * @throws JsonProcessingException if there is an error converting the discovery record to a JSON string
     */
	public void broadcastDiscoveryStatus(DiscoveryRecord record) throws JsonProcessingException {
		log.info("broadcasting discovery status");
		
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        //Object to JSON in String
        String discovery_json = mapper.writeValueAsString(record);
        
		pusher.trigger(record.getDomainUrl(), "discovery-status", discovery_json);
	}

	/**
	 * send {@link DomainDto} to the users pusher channel
	 * @param user_id id of the user
	 * @param domain {@link Domain} to be emitted to clients
	 * @throws JsonProcessingException if there is an error converting the domain dto to a JSON string
	 */
	public void sendDomainAdded(String user_id, Domain domain) throws JsonProcessingException {
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
	 * send {@link UXIssueMessage} to the users pusher channel
	 * @param page_id id of the page
	 * @param issue {@link UXIssueMessage} to be emitted to clients
	 */
	public void sendIssueMessage(String page_id, UXIssueMessage issue) {
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
	public void sendAuditRecord(String user_id, DomainDto domain_dto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

		String domain_dto_json = mapper.writeValueAsString(domain_dto);
		pusher.trigger(user_id, "audit-record", domain_dto_json);
	}

	/**
	 * Sends {@linkplain AuditUpdateDto} to user via Pusher
	 * @param channel_id the channel id to send the audit update to
	 * @param audit_update the audit update to send
	 * @throws JsonProcessingException if there is an error converting the audit update to a JSON string
	 *
	 * precondition: channel_id != null
	 * precondition: channel_id is not empty
	 * precondition: audit_update != null
	 */
	public void sendAuditUpdate(String channel_id, AuditUpdateDto audit_update) throws JsonProcessingException {
		assert channel_id != null;
		assert !channel_id.isEmpty();
		assert audit_update != null;
		
		ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String audit_record_json = mapper.writeValueAsString(audit_update);
		pusher.trigger(channel_id, "audit-progress", audit_record_json);
	}
}
