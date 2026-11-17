package com.indla.insurance.factory;

import com.indla.insurance.service.InsuranceTypeService;
public class GeneralInsurance implements InsuranceTypeService {

	@Override
	public String getInsuranceType() {
		return "General Insurance";
	}

	

}
