package com.marija.quarry_batch.batch.job1;

import com.marija.quarry_batch.model.Invoice;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

public class InvoiceReconciliationWriter implements ItemWriter<Invoice> {

    private final JdbcTemplate jdbcTemplate;

    public InvoiceReconciliationWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(Chunk<? extends Invoice> chunk) {
        for (Invoice invoice : chunk) {
            jdbcTemplate.update(
                    "UPDATE invoice SET total_amount = ? WHERE id = ?",
                    invoice.getTotalAmount(),
                    invoice.getId()
            );
        }
    }
}
