package com.affirm.service;

import com.affirm.model.Facility;
import com.affirm.model.Loan;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Loan services , assign loan and calculate facility yields
 */
public class LoanService {

    private static final Map<Long, Long> LOAN_TO_FACILITY_MAP = new HashMap<>();
    private static Map<Long, BigInteger> FACILITY_ID_TO_EXPECTED_YIELD = new HashMap<>();

    //assign a loan by finding appropriate facility according to its covenant
    public void assignLoanAndCalculateYields(Loan loan, FacilityService facilityService) {

        Facility facility = facilityService.findFacilityForLoan(loan);

        if (facility != null && facility.getId() > 0) {
            Long facilityId = facility.getId();

            //loan to facility map
            LOAN_TO_FACILITY_MAP.put(loan.getId(), facilityId);

            //calculate yield
            BigInteger expectYield = calculateYields(loan, facility);


            System.out.println("facilityId " + facilityId +  " expected yield " + expectYield);

            if (FACILITY_ID_TO_EXPECTED_YIELD.get(facilityId) != null) {
                FACILITY_ID_TO_EXPECTED_YIELD.put(facilityId, FACILITY_ID_TO_EXPECTED_YIELD.get(facilityId).add(expectYield));
            } else {
                FACILITY_ID_TO_EXPECTED_YIELD.put(facilityId, expectYield);
            }
        }
    }

    //calculate yields for loan and facility
    private BigInteger calculateYields(Loan loan, Facility facility) {
        BigDecimal expectedInterest = BigDecimal.ONE
                .subtract(loan.getDefaultLikelihood())
                .multiply(loan.getInterest_rate())
                .multiply(loan.getAmount());

        BigDecimal likelihood = loan.getDefaultLikelihood().multiply(loan.getAmount());
        BigDecimal facilityInterest = facility.getInterest_rate().multiply(loan.getAmount());

        return expectedInterest.subtract(likelihood).subtract(facilityInterest).toBigInteger();
    }

    public Map<Long, Long> getLoanToFacilityMap() {
        return LOAN_TO_FACILITY_MAP;
    }

    public Map<Long, BigInteger> getFacilityIdToExpectedYield() {
        return FACILITY_ID_TO_EXPECTED_YIELD;
    }
}
