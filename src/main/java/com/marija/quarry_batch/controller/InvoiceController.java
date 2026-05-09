package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.model.Invoice;
import com.marija.quarry_batch.model.InvoiceRequest;
import com.marija.quarry_batch.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> getAll() {
        return invoiceService.getAll();
    }

    @GetMapping("/search")
    public List<Invoice> search(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String buyerName,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) {

        return invoiceService.search(userName, buyerName, dateFrom, dateTo, minAmount, maxAmount);
    }

    @GetMapping("/{id}")
    public Invoice getById(@PathVariable Long id) {
        return invoiceService.getById(id);
    }

    @PostMapping
    public void create(@RequestBody Invoice invoice) {
        invoiceService.createInvoice(invoice);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody InvoiceRequest request) {

        request.getInvoice().setId(id);

        invoiceService.updateInvoice(
                request.getInvoice(),
                request.getItems()
        );
    }

}