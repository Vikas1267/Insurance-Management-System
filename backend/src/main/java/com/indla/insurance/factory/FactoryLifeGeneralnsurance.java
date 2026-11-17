package com.indla.insurance.factory;

import com.indla.insurance.exception.PolicyTypeException;
import com.indla.insurance.service.InsuranceTypeService;

public class FactoryLifeGeneralnsurance {

	public static InsuranceTypeService getUserType(String type) {
		if (type == null) {
			return null;
		} else if (type.equalsIgnoreCase("life")) {
			return new LifeInsurance();
		} else if (type.equalsIgnoreCase("general")) {
			return new GeneralInsurance();
		}
		throw new PolicyTypeException("Invalid input");
	}

}
