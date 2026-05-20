package com.marija.quarry_batch.batch.job4;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

public class ArchiveInvoiceWriter implements ItemWriter<ArchiveInvoice> {

    private final JdbcTemplate jdbcTemplate;

    public ArchiveInvoiceWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(Chunk<? extends ArchiveInvoice> chunk) {
        for (ArchiveInvoice item : chunk) {
            jdbcTemplate.update("""
                    INSERT INTO invoice_archive (id, date, total_amount, user_id, buyer_id)
                    VALUES (?, ?, ?, ?, ?)
                    """,
                    item.getId(),
                    item.getDate(),
                    item.getTotalAmount(),
                    item.getUserId(),
                    item.getBuyerId()
            );

            jdbcTemplate.update("""
                    INSERT INTO invoice_item_archive (invoice_id, item_no, price, block_id)
                    SELECT invoice_id, item_no, price, block_id
                    FROM invoice_item WHERE invoice_id = ?
                    """, item.getId());

            jdbcTemplate.update("DELETE FROM invoice_item WHERE invoice_id = ?", item.getId());
            jdbcTemplate.update("DELETE FROM invoice WHERE id = ?", item.getId());
        }
    }
}