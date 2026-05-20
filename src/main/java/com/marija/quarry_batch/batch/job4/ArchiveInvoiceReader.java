package com.marija.quarry_batch.batch.job4;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;

import javax.sql.DataSource;

public class ArchiveInvoiceReader {

    public static JdbcCursorItemReader<ArchiveInvoice> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<ArchiveInvoice>()
                .name("archiveInvoiceReader")
                .dataSource(dataSource)
                .sql("SELECT id, date, total_amount, user_id, buyer_id FROM invoice WHERE date < NOW() - INTERVAL '2 years'")
                .rowMapper((rs, rowNum) -> {
                    ArchiveInvoice item = new ArchiveInvoice();
                    item.setId(rs.getLong("id"));
                    item.setDate(rs.getDate("date"));
                    item.setTotalAmount(rs.getDouble("total_amount"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setBuyerId(rs.getLong("buyer_id"));
                    return item;
                })
                .build();
    }
}