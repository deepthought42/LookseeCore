package com.looksee.services;


import com.looksee.models.PageLoadAnimation;
import com.looksee.models.repository.PageLoadAnimationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for page load animations
 */
@Service
public class PageLoadAnimationService {
	/**
	 * Repository for page load animations
	 */
	@Autowired
	private PageLoadAnimationRepository animation_repo;

	/**
	 * Find a page load animation by key
	 * @param key the key of the page load animation
	 * @return the page load animation
	 */
	public PageLoadAnimation findByKey(String key){
		return animation_repo.findByKey(key);
	}

	/**
	 * Save a page load animation
	 * @param animation the page load animation to save
	 * @return the saved page load animation
	 */
	public PageLoadAnimation save(PageLoadAnimation animation){
		PageLoadAnimation record = findByKey(animation.getKey());
		if(record == null){
			record = animation_repo.save(animation);
		}
		return record;
	}
}
