package com.affirm.service;

import com.affirm.model.Covenant;
import com.affirm.model.Facility;
import com.affirm.model.Loan;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * Solves multiple purposes
 * 1. Parse facilities and covenant
 * 2. Create mapping b/w facility and its covenant (from CSV Data)
 * 3. Find appropriate facility for a given loan
 */
public class FacilityService {

    private static final SortedMap<BigDecimal, List<Facility>> FACILITIES_BY_ASC_INTEREST_RATE = new TreeMap<>();


    //populate each facility from file with its covenants
    public void populateFacilitiesWithCovenant(File facilityFile, File covenantFile) throws IOException {

        if (facilityFile == null || !facilityFile.exists()) {
            throw new NullPointerException("Facility file doesn't exists ");
        }

        if (covenantFile == null || !covenantFile.exists()) {
            throw new NullPointerException("Covenant File file doesn't exists ");
        }

        Map<Long, List<Facility>> facilitiesByBankId = new HashMap<>();
        Map<Long, Facility> facilities = getLongFacilityMap(facilityFile, facilitiesByBankId);
        populateFacilitiesWithCovenants(covenantFile, facilities, facilitiesByBankId);

    }


    Facility findFacilityForLoan(Loan loan) {
        for (BigDecimal interestRate : FACILITIES_BY_ASC_INTEREST_RATE.keySet()) {
            List<Facility> facilities = FACILITIES_BY_ASC_INTEREST_RATE.get(interestRate);
            facilities.sort((m, n) -> (n.getAmount().compareTo(m.getAmount())));
            for (Facility facility : facilities) {
                if (facility.getAmount().compareTo(loan.getAmount()) < 0) {
                    break;
                }
                if (! facility.getBannedStates().contains(loan.getState())
                        && facility.getMaxDefaultLikelihood().compareTo(loan.getDefaultLikelihood()) >= 0){

                    System.out.println("found facility loanId " + loan.getId() +  " facilityId " + facility.getId());

                    //update facility total amount before returning back
                    facility.setAmount(facility.getAmount().subtract(loan.getAmount()));
                    return facility;
                }
            }
        }
        return null;
    }


    private Map<Long, Facility> getLongFacilityMap(File facilityFile, Map<Long, List<Facility>> bankFacilities) throws IOException {
        CSVReader reader = new CSVReaderBuilder(new FileReader(facilityFile))
                .withSkipLines(1)
                .build();
        String[] line;
        Map<Long, Facility> facilities = new HashMap<>();
        while ((line = reader.readNext()) != null) {
            Facility facility = new Facility(
                    Long.parseLong(line[2]),
                    Long.parseLong(line[3]),
                    new BigDecimal(line[1]),
                    new BigDecimal(line[0]));
            facilities.put(facility.getId(), facility);
            if (CollectionUtils.isNotEmpty(bankFacilities.get(facility.getBank_id()))) {
                bankFacilities.get(facility.getBank_id()).add(facility);
            } else {
                List<Facility> bankFacility = new ArrayList<>();
                bankFacility.add(facility);
                bankFacilities.put(facility.getBank_id(), bankFacility);
            }

            if (CollectionUtils.isNotEmpty(FACILITIES_BY_ASC_INTEREST_RATE.get(facility.getInterest_rate()))) {
                FACILITIES_BY_ASC_INTEREST_RATE.get(facility.getInterest_rate()).add(facility);
            } else {
                List<Facility> bankFacility = new ArrayList<>();
                bankFacility.add(facility);
                FACILITIES_BY_ASC_INTEREST_RATE.put(facility.getInterest_rate(), bankFacility);
            }
        }
        reader.close();
        System.out.println("total facilities parsed as " + facilities.size());
        return facilities;
    }

    private void populateFacilitiesWithCovenants(File covenantFile, Map<Long, Facility> facilities,
                                                 Map<Long, List<Facility>> bankFacilities) throws IOException {


        CSVReader reader = new CSVReaderBuilder(new FileReader(covenantFile))
                .withSkipLines(1)
                .build();
        String[] line;
        while ((line = reader.readNext()) != null) {
            Covenant covenant = new Covenant(
                    Long.parseLong(line[0]),
                    StringUtils.isBlank(line[1]) ? null : new BigDecimal(line[1]),
                    Long.parseLong(line[2]),
                    line[3]);

            //set facility max default likelihood
            if (covenant.getMax_default_likelihood() != null) {
                if (covenant.getFacility_id() > 0) {
                    facilities.get(covenant.getFacility_id()).setMaxDefaultLikelihood(covenant.getMax_default_likelihood());
                } else {
                    if (covenant.getBank_id() > 0) {
                        for (Facility facility : bankFacilities.get(covenant.getBank_id())) {
                            facilities.get(facility.getId()).setMaxDefaultLikelihood(covenant.getMax_default_likelihood());
                        }
                    }
                }
            }

            //set facility banned states
            if (StringUtils.isNotBlank(covenant.getBanned_state())) {
                if (covenant.getFacility_id() > 0) {
                    facilities.get(covenant.getFacility_id()).addBannedState(covenant.getBanned_state());
                } else {
                    if (covenant.getBank_id() > 0) {
                        for (Facility facility : bankFacilities.get(covenant.getBank_id())) {
                            facilities.get(facility.getId()).addBannedState(covenant.getBanned_state());
                        }
                    }
                }
            }
        }
        reader.close();
        System.out.println("facilities with covenants are parsed and added");
    }


}
