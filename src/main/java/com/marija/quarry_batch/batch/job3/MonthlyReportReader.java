package com.marija.quarry_batch.batch.job3;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;

import javax.sql.DataSource;

public class MonthlyReportReader {

    public static JdbcCursorItemReader<MonthlyReportItem> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<MonthlyReportItem>()
                .name("monthlyReportReader")
                .dataSource(dataSource)
                .sql("""
                    SELECT
                        EXTRACT(YEAR FROM date) AS year,
                        EXTRACT(MONTH FROM date) AS month,
                        SUM(total_amount) AS total_revenue,
                        AVG(total_amount) AS average_amount,
                        COUNT(*) AS invoice_count
                    FROM invoice
                    WHERE date IS NOT NULL
                    GROUP BY EXTRACT(YEAR FROM date), EXTRACT(MONTH FROM date)
                    ORDER BY year, month
                """)
                .rowMapper((rs, rowNum) -> {
                    MonthlyReportItem item = new MonthlyReportItem();
                    item.setYear(rs.getInt("year"));
                    item.setMonth(rs.getInt("month"));
                    item.setTotalRevenue(rs.getDouble("total_revenue"));
                    item.setAverageAmount(rs.getDouble("average_amount"));
                    item.setInvoiceCount(rs.getInt("invoice_count"));
                    return item;
                })
                .build();
    }
}