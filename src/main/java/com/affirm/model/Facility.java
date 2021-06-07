package com.affirm.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Keep facility detail with its Covenants (ex. banned states and max default likelihood)
 *
 */
public class Facility implements Serializable {

    private long id;
    private long bank_id;
    private BigDecimal interest_rate;
    private BigDecimal amount;
    private BigDecimal maxDefaultLikelihood;

    public Set<String> getBannedStates() {
        return bannedStates;
    }

    private final Set<String> bannedStates = new HashSet<>();

    public Facility(long id, long bank_id, BigDecimal interest_rate, BigDecimal amount) {
        this.id = id;
        this.bank_id = bank_id;
        this.interest_rate = interest_rate;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBank_id() {
        return bank_id;
    }

    public void setBank_id(long bank_id) {
        this.bank_id = bank_id;
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


    public void setMaxDefaultLikelihood(BigDecimal maxDefaultLikelihood) {
        this.maxDefaultLikelihood = maxDefaultLikelihood;
    }

    public BigDecimal getMaxDefaultLikelihood() {
        return maxDefaultLikelihood;
    }

    //add banned state to the facility
    public void addBannedState(String state) {
        if (StringUtils.isNotBlank(state)) {
            bannedStates.add(state);
        }
    }
}
