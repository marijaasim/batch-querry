package com.marija.quarry_batch.batch.job4;

import java.util.Date;

public class ArchiveInvoice {

    private Long id;
    private Date date;
    private double totalAmount;
    private Long userId;
    private Long buyerId;

    public ArchiveInvoice() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBuyerId() { return buyerId; }
    public void setBuyerId(Long buyerId) { this.buyerId = buyerId; }
}
