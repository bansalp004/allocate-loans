package com.affirm.model;

import java.math.BigDecimal;

/**
 * represent a loan received for processing
 */

public class Loan {

    private BigDecimal interest_rate;
    private BigDecimal amount;
    private long id;
    private BigDecimal defaultLikelihood;
    private String state;

    public Loan(BigDecimal interest_rate, BigDecimal amount, long id, BigDecimal defaultLikelihood, String state) {
        this.interest_rate = interest_rate;
        this.amount = amount;
        this.id = id;
        this.defaultLikelihood = defaultLikelihood;
        this.state = state;
    }

    public BigDecimal getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(BigDecimal interest_rate) {
        this.interest_rate = interest_rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getDefaultLikelihood() {
        return defaultLikelihood;
    }

    public void setDefaultLikelihood(BigDecimal defaultLikelihood) {
        this.defaultLikelihood = defaultLikelihood;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
