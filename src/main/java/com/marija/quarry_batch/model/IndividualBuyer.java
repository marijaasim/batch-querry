package com.marija.quarry_batch.model;

import java.util.Objects;

public class IndividualBuyer extends Buyer{

    private String personalId;

    public IndividualBuyer() {
    }

    public IndividualBuyer(Long id, String name, String phoneNumber, String email, String personalId) {
        super(id, name, phoneNumber, email);
        this.personalId = personalId;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IndividualBuyer that = (IndividualBuyer) o;
        return Objects.equals(personalId, that.personalId);
    }

    @Override
    public String toString() {
        return name;
    }
}
