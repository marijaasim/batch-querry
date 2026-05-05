package com.marija.quarry_batch.model;

import java.util.List;
import java.util.Objects;

public class InvoiceRequest {

    private Invoice invoice;
    private List<InvoiceItem> items;

    public InvoiceRequest() {
    }

    public InvoiceRequest(Invoice invoice, List<InvoiceItem> items) {
        this.invoice = invoice;
        this.items = items;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceRequest that = (InvoiceRequest) o;
        return Objects.equals(invoice, that.invoice) && Objects.equals(items, that.items);
    }

    @Override
    public String toString() {
        return "InvoiceRequest{" +
                "invoice=" + invoice +
                ", items=" + items +
                '}';
    }
}
