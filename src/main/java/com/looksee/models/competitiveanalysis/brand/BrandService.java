package com.looksee.models.competitiveanalysis.brand;

import com.looksee.models.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

	@Autowired
	private BrandRepository brand_repo;
	
	public Brand save(Brand brand){
		return brand_repo.save(brand);
	}
}
