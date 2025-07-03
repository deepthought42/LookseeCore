package com.looksee.models.competitiveanalysis.brand;

import com.looksee.models.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for {@link Brand}
 */
@Service
public class BrandService {

	@Autowired
	private BrandRepository brand_repo;
	
	/**
	 * Saves a {@link Brand}
	 * 
	 * @param brand {@link Brand} to save
	 * @return saved {@link Brand}
	 */
	public Brand save(Brand brand){
		return brand_repo.save(brand);
	}
}
