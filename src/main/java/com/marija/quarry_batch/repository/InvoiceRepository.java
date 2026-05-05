package com.marija.quarry_batch.repository;

import com.marija.quarry_batch.model.Invoice;
import com.marija.quarry_batch.model.InvoiceItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvoiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public InvoiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Invoice> findAll() {

        String sql = """
            SELECT 
                i.id,
                i.date,
                i.total_amount,
                i.user_id,
                i.buyer_id,
                u.name AS user_name,
                b.name AS buyer_name
            FROM invoice i
            JOIN users u ON i.user_id = u.id
            JOIN buyer b ON i.buyer_id = b.id
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Invoice i = new Invoice();
            i.setId(rs.getLong("id"));
            i.setDate(rs.getDate("date"));
            i.setTotalAmount(rs.getDouble("total_amount"));
            i.setUserId(rs.getLong("user_id"));
            i.setBuyerId(rs.getLong("buyer_id"));
            return i;
        });
    }

    public Invoice findById(Long id) {

        String invoiceSql = "SELECT * FROM invoice WHERE id = ?";

        Invoice invoice = jdbcTemplate.queryForObject(
                invoiceSql,
                new Object[]{id},
                (rs, rowNum) -> {
                    Invoice i = new Invoice();
                    i.setId(rs.getLong("id"));
                    i.setDate(rs.getDate("date"));
                    i.setTotalAmount(rs.getDouble("total_amount"));
                    i.setUserId(rs.getLong("user_id"));
                    i.setBuyerId(rs.getLong("buyer_id"));
                    return i;
                }
        );

        String itemsSql = "SELECT * FROM invoice_item WHERE invoice_id = ?";

        List<InvoiceItem> items = jdbcTemplate.query(
                itemsSql,
                new Object[]{id},
                (rs, rowNum) -> {
                    InvoiceItem item = new InvoiceItem();
                    item.setInvoiceId(rs.getLong("invoice_id"));
                    item.setItemNo(rs.getInt("item_no"));
                    item.setPrice(rs.getDouble("price"));
                    item.setBlockId(rs.getLong("block_id"));
                    return item;
                }
        );

        invoice.setItems(items);

        return invoice;
    }

    public Long insertInvoice(Invoice invoice) {

        String sql = """
        INSERT INTO invoice (date, total_amount, user_id, buyer_id)
        VALUES (?, ?, ?, ?)
        RETURNING id
    """;

        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{
                        invoice.getDate(),
                        invoice.getTotalAmount(),
                        invoice.getUserId(),
                        invoice.getBuyerId()
                },
                Long.class
        );
    }

    public void insertItem(InvoiceItem item) {

        String sql = """
        INSERT INTO invoice_item (invoice_id, item_no, price, block_id)
        VALUES (?, ?, ?, ?)
    """;

        jdbcTemplate.update(
                sql,
                item.getInvoiceId(),
                item.getItemNo(),
                item.getPrice(),
                item.getBlockId()
        );
    }

    public void updateInvoice(Invoice invoice) {
        String sql = "UPDATE invoice SET date = ?, total_amount = ?, user_id = ?, buyer_id = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                new java.sql.Date(invoice.getDate().getTime()),
                invoice.getTotalAmount(),
                invoice.getUserId(),
                invoice.getBuyerId(),
                invoice.getId()
        );
    }

    public void deleteItemsByInvoiceId(Long invoiceId) {
        String sql = "DELETE FROM invoice_item WHERE invoice_id = ?";
        jdbcTemplate.update(sql, invoiceId);
    }



}
