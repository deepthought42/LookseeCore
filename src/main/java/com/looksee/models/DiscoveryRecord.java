package com.looksee.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import com.looksee.models.enums.ExecutionStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Record detailing a "Discovery" ran by an account.
 */
@Node
@Getter
@Setter
@NoArgsConstructor
public class DiscoveryRecord extends LookseeObject {

	@GeneratedValue
    @Id
	private Long id;

	private String key;
	private Date startTime;
	private String browserName;
	private String domainUrl;
	private Date lastPathRanAt;
	private ExecutionStatus status;
	private int totalPathCount;
	private int examinedPathCount;
	private int testCount;
	private List<String> expandedUrls;
	private List<String> expandedPathKeys;

	/**
	 * Creates a new discovery record
	 *
	 * @param started_timestamp the timestamp when the discovery started
	 * @param browser_name the name of the browser used for the discovery
	 * @param domain_url the url of the domain being discovered
	 * @param test_cnt the number of tests to run
	 * @param total_cnt the total number of paths to discover
	 * @param examined_cnt the number of paths that have been examined
	 * @param status the status of the discovery
	 */
	public DiscoveryRecord(Date started_timestamp, String browser_name, String domain_url,
							int test_cnt, int total_cnt, int examined_cnt,
							ExecutionStatus status){
		assert started_timestamp != null;
		assert browser_name != null;
		assert domain_url != null;
		assert test_cnt > -1;
		assert total_cnt > -1;

		setExpandedPathKeys(new ArrayList<String>());
		setExpandedUrls(new ArrayList<String>());
		setStartTime(started_timestamp);
		setBrowserName(browser_name);
		setDomainUrl(domain_url);
		setLastPathRanAt(new Date());
		setTotalPathCount(total_cnt);
		setExaminedPathCount(examined_cnt);
		setTestCount(test_cnt);
		setStatus(status);
		setKey(generateKey());
	}

	public String generateKey() {
		return getDomainUrl()+":"+UUID.randomUUID().toString();
	}

	public boolean addExpandedPathKey(String expanded_path_key) {
		if(!this.expandedPathKeys.contains(expanded_path_key)){
			return this.expandedPathKeys.add(expanded_path_key);
		}
		return false;
	}
}
