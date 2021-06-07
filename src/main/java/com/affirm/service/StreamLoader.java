package com.affirm.service;

import com.affirm.model.Loan;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StreamLoader {


    public List<Loan> getLoans(File loanFile) throws IOException {

        if (loanFile == null || !loanFile.exists()) {
            throw new NullPointerException("Loan File file doesn't exists ");
        }

        CSVReader reader = new CSVReaderBuilder(new FileReader(loanFile))
                .withSkipLines(1)
                .build();
        String[] line;
        List<Loan> loans = new ArrayList<>();
        while ((line = reader.readNext()) != null) {
            Loan loan = new Loan(
                    new BigDecimal(line[0]),
                    new BigDecimal(line[1]),
                    Long.parseLong(line[2]),
                    new BigDecimal(line[3]),
                    line[4]);
            loans.add(loan);
        }
        reader.close();
        System.out.println("total loan parsed as " + loans.size());
        return loans;
    }


    //create loan to assignment file
    public void createLoanToAssignmentFileFile(File file, Map<Long, Long> loanToFacility, String[] headers) throws IOException {
        CSVWriter writer = new CSVWriter(
                new FileWriter(file),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER
        );
        writer.writeNext(headers);
        for (Long loanId : loanToFacility.keySet()) {
            String[] line = {
                    loanId + "",
                    loanToFacility.get(loanId) == null ? "" : loanToFacility.get(loanId) + ""
            };
            writer.writeNext(line);
        }
        writer.close();
    }

    //create yield file
    public void createExpectedYieldForFacility(File file, Map<Long, BigInteger> expectedYield, String[] headers) throws IOException {
        CSVWriter writer = new CSVWriter(
                new FileWriter(file),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER
        );
        writer.writeNext(headers);

        int totalAssigned = 0;

        for (Long facilityId : expectedYield.keySet()) {
            String[] line = {
                    facilityId + "",
                    expectedYield.get(facilityId) == null ? "" : expectedYield.get(facilityId) + ""
            };
            writer.writeNext(line);
        }
        writer.close();
    }

}
