package com.indla.insurance.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ActuarialCalculatorService {

    public Map<String, Object> calculatePremium(int age, double sumAssured, int termYears, String coverageType, boolean isSmoker) {
        Map<String, Object> result = new HashMap<>();

        double baseMortalityPerThousand;
        String riskCategory;
        if (age < 25) {
            baseMortalityPerThousand = 1.15;
            riskCategory = "MINIMAL RISK (YOUNG ADULT)";
        } else if (age <= 35) {
            baseMortalityPerThousand = 1.65;
            riskCategory = "STANDARD PREFERRED";
        } else if (age <= 45) {
            baseMortalityPerThousand = 3.20;
            riskCategory = "STANDARD UNDERWRITING";
        } else if (age <= 55) {
            baseMortalityPerThousand = 6.80;
            riskCategory = "MODERATE RISK";
        } else {
            baseMortalityPerThousand = 13.50;
            riskCategory = "ELEVATED RISK BRACKET";
        }

        double planMultiplier;
        String planDescription;
        if ("WHOLE_LIFE".equalsIgnoreCase(coverageType)) {
            planMultiplier = 1.75;
            planDescription = "Whole Life Comprehensive Protection";
        } else if ("ENDOWMENT".equalsIgnoreCase(coverageType)) {
            planMultiplier = 2.20;
            planDescription = "Endowment with Guaranteed Savings";
        } else if ("CRITICAL_ILLNESS".equalsIgnoreCase(coverageType)) {
            planMultiplier = 1.40;
            planDescription = "Term Life with Critical Illness Rider";
        } else {
            planMultiplier = 1.00;
            planDescription = "Pure Term Life Assurance";
        }

        double smokerLoading = isSmoker ? 1.35 : 1.0;
        double termDiscount = 1.0 - (Math.min(termYears, 35) * 0.005);

        double netAnnualPremium = (sumAssured / 1000.0) * baseMortalityPerThousand * planMultiplier * smokerLoading * termDiscount;
        netAnnualPremium += 650.0;

        double netAnnual = Math.round(netAnnualPremium * 100.0) / 100.0;
        double gstTax = Math.round((netAnnual * 0.18) * 100.0) / 100.0;
        double grossAnnual = Math.round((netAnnual + gstTax) * 100.0) / 100.0;

        double monthly = Math.round((grossAnnual / 12.0) * 100.0) / 100.0;
        double quarterly = Math.round((grossAnnual / 4.0) * 100.0) / 100.0;
        double semiAnnual = Math.round((grossAnnual / 2.0) * 100.0) / 100.0;

        result.put("age", age);
        result.put("sumAssured", sumAssured);
        result.put("termYears", termYears);
        result.put("coverageType", coverageType);
        result.put("planDescription", planDescription);
        result.put("isSmoker", isSmoker);
        result.put("riskCategory", riskCategory);
        result.put("baseMortalityRatePerThousand", baseMortalityPerThousand);
        result.put("netAnnualPremium", netAnnual);
        result.put("gst18Percent", gstTax);
        result.put("grossAnnualPremium", grossAnnual);
        result.put("monthlyPremium", monthly);
        result.put("quarterlyPremium", quarterly);
        result.put("semiAnnualPremium", semiAnnual);
        result.put("maturityBenefitEstimate", Math.round(sumAssured * (1.0 + (termYears * 0.035))));

        return result;
    }
}
