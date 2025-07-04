package com.looksee.services;

import com.looksee.models.Account;
import com.looksee.models.DiscoveryRecord;
import com.looksee.models.repository.DiscoveryRecordRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing discovery records
 */
@Service
public class DiscoveryRecordService {

	@Autowired
	private DiscoveryRecordRepository discovery_repo;
	
	/**
	 * Increments the test count for a discovery record
	 *
	 * @param discovery_key the key of the discovery record
	 * @return the updated discovery record
	 */
	public DiscoveryRecord incrementTestCount(String discovery_key){
		DiscoveryRecord discovery_record = discovery_repo.findByKey(discovery_key);
		discovery_record.setTestCount(discovery_record.getTestCount()+1);
  		return save(discovery_record);
	}
	
	/**
	 * Increments the total path count for a discovery record
	 *
	 * @param discovery_key the key of the discovery record
	 * @return the updated discovery record
	 */
	public DiscoveryRecord incrementTotalPathCount(String discovery_key){
		DiscoveryRecord discovery_record = discovery_repo.findByKey(discovery_key);
		discovery_record.setTotalPathCount(discovery_record.getTotalPathCount()+1);
  		return save(discovery_record);
	}
	
	/**
	 * Increases the total path count for a discovery record
	 *
	 * @param discovery_key the key of the discovery record
	 * @param cnt the number of paths to increase
	 * @return the updated discovery record
	 */
	public DiscoveryRecord increaseTotalPathCount(String discovery_key, int cnt){
		DiscoveryRecord discovery_record = discovery_repo.findByKey(discovery_key);
		discovery_record.setTotalPathCount(discovery_record.getTotalPathCount()+cnt);
  		discovery_record = save(discovery_record);
  		
  		return discovery_record;
	}
	
	/**
	 * Saves a discovery record
	 *
	 * @param discovery the discovery record
	 * @return the updated discovery record
	 */
	public synchronized DiscoveryRecord save(DiscoveryRecord discovery){
		DiscoveryRecord discovery_record = discovery_repo.findByKey(discovery.getKey());
		if(discovery_record == null){
			discovery_record = discovery;
		}
		else{
			discovery_record.setExaminedPathCount(discovery.getExaminedPathCount());
			discovery_record.setExpandedPathKeys(discovery.getExpandedPathKeys());
			discovery_record.setLastPathRanAt(discovery.getLastPathRanAt());
			discovery_record.setTestCount(discovery.getTestCount());
			discovery_record.setTotalPathCount(discovery.getTotalPathCount());
			discovery_record.setExpandedUrls(discovery.getExpandedUrls());
			discovery_record.setStatus(discovery.getStatus());
		}
		
		return discovery_repo.save(discovery_record);
	}
	
	/**
	 * Finds a discovery record by its key
	 *
	 * @param key the key of the discovery record
	 * @return the discovery record
	 */
	public DiscoveryRecord findByKey(String key){
		return discovery_repo.findByKey(key);
	}

	/**
	 * Increases the examined path count for a discovery record
	 *
	 * @param key the key of the discovery record
	 * @param path_cnt the number of paths to increase
	 * @return the updated discovery record
	 */
	public DiscoveryRecord increaseExaminedPathCount(String key, int path_cnt) {
		DiscoveryRecord discovery_record = discovery_repo.findByKey(key);
		discovery_record.setExaminedPathCount(discovery_record.getExaminedPathCount()+path_cnt);
		discovery_record.setLastPathRanAt(new Date());
		return discovery_repo.save(discovery_record);
	}

	/**
	 * Gets the accounts for a discovery record
	 *
	 * @param key the key of the discovery record
	 * @return the accounts for the discovery record
	 */
	public List<Account> getAccounts(String key) {
		return discovery_repo.getAllAccounts(key);
	}
}
