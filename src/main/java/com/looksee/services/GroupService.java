package com.looksee.services;

import com.looksee.models.Group;
import com.looksee.models.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for {@link Group}s
 */
@Service
public class GroupService {

	@Autowired
	private GroupRepository groupRepo;
	
	/**
	 * Saves a {@link Group}
	 *
	 * @param group the group to save
	 * @return the saved group
	 */
	public Group save(Group group){
		Group groupRecord = findByKey(group.getKey());
		if(groupRecord == null){
			groupRecord = groupRepo.save(group);
		}
		return groupRecord;
	}
	
	/**
	 * Finds a {@link Group} by its key
	 *
	 * @param key the key
	 * @return the group
	 */
	public Group findByKey(String key){
		return groupRepo.findByKey(key);
	}
}
