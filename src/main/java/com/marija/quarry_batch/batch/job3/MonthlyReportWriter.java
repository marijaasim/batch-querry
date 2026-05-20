package com.marija.quarry_batch.batch.job3;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

public class MonthlyReportWriter implements ItemWriter<MonthlyReportItem> {

    private final JdbcTemplate jdbcTemplate;

    public MonthlyReportWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(Chunk<? extends MonthlyReportItem> chunk) {
        for (MonthlyReportItem item : chunk) {
            jdbcTemplate.update("""
                    INSERT INTO monthly_report
                    (year, month, total_revenue, average_amount, invoice_count, top_buyer_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """,
                    item.getYear(),
                    item.getMonth(),
                    item.getTotalRevenue(),
                    item.getAverageAmount(),
                    item.getInvoiceCount(),
                    item.getTopBuyerId()
            );
        }
    }
}