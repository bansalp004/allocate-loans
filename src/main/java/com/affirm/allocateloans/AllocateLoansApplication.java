package com.affirm.allocateloans;

import com.affirm.model.Loan;
import com.affirm.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class AllocateLoansApplication {



    //input files
    private static final String FACILITY_CSV = "src/main/resources/input/facilities.csv";
    private static final String COVENANT_CSV = "src/main/resources/input/covenants.csv";
    private static final String LOANS_CSV = "src/main/resources/input/loans.csv";


    //test input files (uncomment below and comment above to run with sample data)
//    private static final String FACILITY_CSV = "src/main/resources/input/test/facilities.csv";
//    private static final String COVENANT_CSV = "src/main/resources/input/test/covenants.csv";
//    private static final String LOANS_CSV = "src/main/resources/input/test/loans.csv";


    //output files
    private static final String ASSIGNMENT_CSV = "src/main/resources/output/assignments.csv";
    private static final String YIELDS_CSV = "src/main/resources/output/yields.csv";

    private static final String[] CSV_LOAN_FACILITY_HEADERS = {"loan_id", "facility_id"};
    private static final String[] CSV_FACILITY_YIELDS_HEADERS = {"facility_id", "expected_yield"};




    public static void main(String[] args) throws IOException {
        SpringApplication.run(AllocateLoansApplication.class, args);

        StreamLoader streamLoader = new StreamLoader();
        LoanService loanService = new LoanService();


        //read data from stream, assign loan appropriately
        readStreamAndAssignLoan(streamLoader, loanService);


        //create output CSV file
        createOutput(streamLoader, loanService);
    }


    private static void readStreamAndAssignLoan(StreamLoader streamLoader, LoanService loanService) throws IOException {

        FacilityService facilityService = new FacilityService();

        //populate facilities with covenant
        facilityService.populateFacilitiesWithCovenant(new File(FACILITY_CSV), new File(COVENANT_CSV));

        //get loan from given csv file
        List<Loan> loans = streamLoader.getLoans(new File(LOANS_CSV));


        //assign loan to a facility
        for (Loan loan : loans){
            loanService.assignLoanAndCalculateYields(loan, facilityService);
        }

    }

    private static void createOutput(StreamLoader streamLoader, LoanService loanService) throws IOException {
        File assignmentFile = new File(ASSIGNMENT_CSV);
        streamLoader.createLoanToAssignmentFileFile(assignmentFile, loanService.getLoanToFacilityMap(), CSV_LOAN_FACILITY_HEADERS);

        File yieldFile = new File(YIELDS_CSV);
        streamLoader.createExpectedYieldForFacility(yieldFile, loanService.getFacilityIdToExpectedYield(), CSV_FACILITY_YIELDS_HEADERS);
    }


}
