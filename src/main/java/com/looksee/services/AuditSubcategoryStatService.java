package com.looksee.services;

import com.looksee.models.audit.AuditSubcategoryStat;
import com.looksee.models.repository.AuditSubcategoryStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for {@link AuditSubcategoryStat}
 */
@Service
public class AuditSubcategoryStatService {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuditSubcategoryStatRepository audit_stat_repository;
	
	/**
	 * Saves an {@link AuditSubcategoryStat}
	 * 
	 * @param audit_stat {@link AuditSubcategoryStat} to save
	 * @return saved {@link AuditSubcategoryStat}
	 */
	public AuditSubcategoryStat save(AuditSubcategoryStat audit_stat) {
		return audit_stat_repository.save(audit_stat);
	}
}
