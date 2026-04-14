package com.marija.quarry_batch.model;

import java.util.Date;
import java.util.Objects;

public class Invoice {

    private Long id;
    private Date date;
    private double totalAmount;
    private Long userId;
    private Long buyerId;

    public Invoice() {
    }

    public Invoice(Long id, Date date, double totalAmount, Long userId, Long buyerId) {
        this.id = id;
        this.date = date;
        this.totalAmount = totalAmount;
        this.userId = userId;
        this.buyerId = buyerId;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", date=" + date +
                ", totalAmount=" + totalAmount +
                ", userId=" + userId +
                ", buyerId=" + buyerId +
                '}';
    }
}
