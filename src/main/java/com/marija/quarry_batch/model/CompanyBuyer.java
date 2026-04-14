package com.marija.quarry_batch.model;

import java.util.Objects;

public class CompanyBuyer extends Buyer {

    private String taxId;

    public CompanyBuyer() {
    }

    public CompanyBuyer(Long id, String name, String phoneNumber, String email, String taxId) {
        super(id, name, phoneNumber, email);
        this.taxId = taxId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompanyBuyer that = (CompanyBuyer) o;
        return Objects.equals(taxId, that.taxId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taxId);
    }

    @Override
    public String toString() {
        return name;
    }
}
