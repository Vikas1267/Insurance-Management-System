package com.indla.insurance.util;

import com.indla.insurance.factory.FactoryLifeGeneralnsurance;
import com.indla.insurance.service.InsuranceTypeService;
public class FactoryPolicyType {

	public InsuranceTypeService getFactoryObject(String getInsuranceType) {
		return FactoryLifeGeneralnsurance.getUserType(getInsuranceType);
	}
}