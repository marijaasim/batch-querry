package com.marija.quarry_batch.batch.job1;

import com.marija.quarry_batch.model.Invoice;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

public class InvoiceReconciliationProcessor implements ItemProcessor<Invoice, Invoice> {

    private final JdbcTemplate jdbcTemplate;

    public InvoiceReconciliationProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Invoice process(Invoice invoice) {
        Double calculatedTotal = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(price), 0) FROM invoice_item WHERE invoice_id = ?",
                Double.class,
                invoice.getId()
        );

        if (calculatedTotal == null) calculatedTotal = 0.0;

        if (Math.abs(invoice.getTotalAmount() - calculatedTotal) > 0.01) {
            invoice.setTotalAmount(calculatedTotal);
            return invoice;
        }

        return null;
    }
}
