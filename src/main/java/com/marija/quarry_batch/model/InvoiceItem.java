package com.marija.quarry_batch.model;

import java.util.Objects;

public class InvoiceItem {

    private Long invoiceId;
    private int itemNo;
    private double price;
    private Long blockId;

    public InvoiceItem() {
    }

    public InvoiceItem(Long invoiceId, int itemNo, double price, Long blockId) {
        this.invoiceId = invoiceId;
        this.itemNo = itemNo;
        this.price = price;
        this.blockId = blockId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public int getItemNo() {
        return itemNo;
    }

    public double getPrice() {
        return price;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItem that = (InvoiceItem) o;
        return itemNo == that.itemNo;
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "invoiceId=" + invoiceId +
                ", itemNo=" + itemNo +
                ", price=" + price +
                ", blockId=" + blockId +
                '}';
    }
}
