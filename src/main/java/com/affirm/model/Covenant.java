package com.affirm.model;


import java.math.BigDecimal;

/**
 * holds bank and facility Covenant details
 */
public class Covenant {

    private long facility_id;
    private BigDecimal max_default_likelihood;
    private long bank_id;
    private String banned_state;


    public Covenant(long facility_id, BigDecimal max_default_likelihood, long bank_id, String banned_state) {
        this.facility_id = facility_id;
        this.max_default_likelihood = max_default_likelihood;
        this.bank_id = bank_id;
        this.banned_state = banned_state;
    }

    public long getFacility_id() {
        return facility_id;
    }

    public void setFacility_id(long facility_id) {
        this.facility_id = facility_id;
    }

    public BigDecimal getMax_default_likelihood() {
        return max_default_likelihood;
    }

    public void setMax_default_likelihood(BigDecimal max_default_likelihood) {
        this.max_default_likelihood = max_default_likelihood;
    }

    public long getBank_id() {
        return bank_id;
    }

    public void setBank_id(long bank_id) {
        this.bank_id = bank_id;
    }

    public String getBanned_state() {
        return banned_state;
    }

    public void setBanned_state(String banned_state) {
        this.banned_state = banned_state;
    }
}
