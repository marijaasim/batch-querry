package com.marija.quarry_batch.batch.job3;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

public class MonthlyReportProcessor implements ItemProcessor<MonthlyReportItem, MonthlyReportItem> {

    private final JdbcTemplate jdbcTemplate;

    public MonthlyReportProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MonthlyReportItem process(MonthlyReportItem item) {
        Map<String, Object> topBuyer = jdbcTemplate.queryForMap("""
                SELECT buyer_id, SUM(total_amount) AS total
                FROM invoice
                WHERE EXTRACT(YEAR FROM date) = ?
                  AND EXTRACT(MONTH FROM date) = ?
                GROUP BY buyer_id
                ORDER BY total DESC
                LIMIT 1
                """, item.getYear(), item.getMonth());

        item.setTopBuyerId(((Number) topBuyer.get("buyer_id")).longValue());

        item.setTotalRevenue(Math.round(item.getTotalRevenue() * 100.0) / 100.0);
        item.setAverageAmount(Math.round(item.getAverageAmount() * 100.0) / 100.0);

        return item;
    }
}