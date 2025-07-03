package com.looksee.models;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the usage of an account
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountUsage {
	private int discoveryLimit;
	private int discoveriesUsed;
	private int testLimit;
	private int testsUsed;
	private int totalDiscoveredTestCount;
	
	private int domainDiscoveries;
	private int domainTotalTestsRun;
	private int domainTotalDiscoveredTestCount;
	
	private Date currentDiscoveryStart;
	private long discoveryRunTime;
	private Date timestampOfLastDiscoveredTest;
	
	public AccountUsage(int discoveryLimit, int discoveriesUsed, int testLimit, int testsUsed){
		this.setDiscoveryLimit(discoveryLimit);
		this.setDiscoveriesUsed(discoveriesUsed);
		this.setTestLimit(testLimit);
		this.setTestsUsed(testsUsed);
	}

	public AccountUsage(int discoveryLimit, int discoveriesUsed, int testLimit, int testsUsed, int totalDiscoveredTestCount, int domainDiscoveries, int domainTotalDiscoveredTestCount, int domainTotalTestsRun, Date currentDiscoveryStart, long discoveryRunTime, Date timestampOfLastDiscoveredTest){
		this.setDiscoveryLimit(discoveryLimit);
		this.setDiscoveriesUsed(discoveriesUsed);
		this.setTestLimit(testLimit);
		this.setTestsUsed(testsUsed);
		this.setTotalDiscoveredTestCount(totalDiscoveredTestCount);
		this.setDomainDiscoveries(domainDiscoveries);
		this.setDomainTotalDiscoveredTestCount(domainTotalDiscoveredTestCount);
		this.setDomainTotalTestsRun(domainTotalTestsRun);
		this.setCurrentDiscoveryStart(currentDiscoveryStart);
		this.setDiscoveryRunTime(discoveryRunTime);
		this.setTimestampOfLastDiscoveredTest(timestampOfLastDiscoveredTest);
	}
}
