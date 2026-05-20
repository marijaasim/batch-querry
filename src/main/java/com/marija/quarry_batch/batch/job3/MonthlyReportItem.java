package com.marija.quarry_batch.batch.job3;

public class MonthlyReportItem {

    private int year;
    private int month;
    private double totalRevenue;
    private double averageAmount;
    private int invoiceCount;
    private Long topBuyerId;

    public MonthlyReportItem() {}

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public double getAverageAmount() { return averageAmount; }
    public void setAverageAmount(double averageAmount) { this.averageAmount = averageAmount; }
    public int getInvoiceCount() { return invoiceCount; }
    public void setInvoiceCount(int invoiceCount) { this.invoiceCount = invoiceCount; }
    public Long getTopBuyerId() { return topBuyerId; }
    public void setTopBuyerId(Long topBuyerId) { this.topBuyerId = topBuyerId; }
}