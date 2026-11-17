package com.indla.insurance.factory;

import com.indla.insurance.service.InsuranceTypeService;
public class LifeInsurance implements InsuranceTypeService{

	@Override
	public String getInsuranceType() {
		return "Life Insurance";
	}

	

	
}
