package com.looksee.services;

import com.looksee.models.competitiveanalysis.brand.Brand;
import com.looksee.models.repository.BrandRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for {@link Brand}
 */
@NoArgsConstructor
@Service
public class BrandService {

	/**
	 * The repository for {@link Brand}
	 */
	@Autowired
	private BrandRepository brand_repo;
	
	/**
	 * Saves a {@link Brand}
	 *
	 * @param brand the brand to save
	 * @return the saved brand
	 */
	public Brand save(Brand brand){
		return brand_repo.save(brand);
	}
}
