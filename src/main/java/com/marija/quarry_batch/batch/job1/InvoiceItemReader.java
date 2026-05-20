package com.marija.quarry_batch.batch.job1;

import com.marija.quarry_batch.model.Invoice;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.DataClassRowMapper;

import javax.sql.DataSource;

public class InvoiceItemReader {

    public static JdbcCursorItemReader<Invoice> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Invoice>()
                .name("invoiceReader")
                .dataSource(dataSource)
                .sql("SELECT id, total_amount FROM invoice")
                .rowMapper((rs, rowNum) -> {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getLong("id"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    return invoice;
                })
                .build();
    }
}
