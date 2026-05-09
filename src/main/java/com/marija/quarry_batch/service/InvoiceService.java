package com.marija.quarry_batch.service;

import com.marija.quarry_batch.model.Invoice;
import com.marija.quarry_batch.model.InvoiceItem;
import com.marija.quarry_batch.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    public List<Invoice> search(String userName, String buyerName, String dateFrom, String dateTo, Double minAmount, Double maxAmount) {
        return invoiceRepository.search(userName, buyerName, dateFrom, dateTo, minAmount, maxAmount);
    }

    public Invoice getById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Transactional
    public void createInvoice(Invoice invoice) {

        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            throw new RuntimeException("Invoice must have at least one item");
        }

        Long invoiceId = invoiceRepository.insertInvoice(invoice);

        for (InvoiceItem item : invoice.getItems()) {
            item.setInvoiceId(invoiceId);
            invoiceRepository.insertItem(item);
        }
    }

    @Transactional
    public void updateInvoice(Invoice invoice, List<InvoiceItem> items) {

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Invoice must have at least one item");
        }

        invoiceRepository.updateInvoice(invoice);

        invoiceRepository.deleteItemsByInvoiceId(invoice.getId());

        int rb = 1;
        for (InvoiceItem item : items) {
            item.setInvoiceId(invoice.getId());
            item.setItemNo(rb++);
            invoiceRepository.insertItem(item);
        }
    }

}
