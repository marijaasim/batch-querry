package com.marija.quarry_batch.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public BatchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ===== BATCH 1 =====
    public List<Map<String, Object>> findAllInvoicesWithTotal() {
        return jdbcTemplate.queryForList(
                "SELECT id, total_amount FROM invoice"
        );
    }

    public Double calculateInvoiceTotal(Long invoiceId) {
        return jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(price), 0) FROM invoice_item WHERE invoice_id = ?",
                Double.class,
                invoiceId
        );
    }

    public void updateInvoiceTotalAmount(Long invoiceId, double newTotal) {
        jdbcTemplate.update(
                "UPDATE invoice SET total_amount = ? WHERE id = ?",
                newTotal, invoiceId
        );
    }

    // ===== BATCH 2 =====
    public List<Map<String, Object>> findAllBlocksWithDimensions() {
        return jdbcTemplate.queryForList(
                "SELECT id, length, width, height, category FROM block"
        );
    }

    public void updateBlockCategory(Long blockId, String newCategory) {
        jdbcTemplate.update(
                "UPDATE block SET category = ? WHERE id = ?",
                newCategory, blockId
        );
    }

    // ===== BATCH 3 =====
    public void createMonthlyReportTableIfNotExists() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS monthly_report (
                id SERIAL PRIMARY KEY,
                year INTEGER,
                month INTEGER,
                total_revenue DOUBLE PRECISION,
                average_amount DOUBLE PRECISION,
                invoice_count INTEGER,
                top_buyer_id BIGINT,
                generated_at TIMESTAMP DEFAULT NOW()
            )
        """);
    }

    public void clearMonthlyReport() {
        jdbcTemplate.execute("DELETE FROM monthly_report");
    }

    public List<Map<String, Object>> findMonthlyAggregates() {
        return jdbcTemplate.queryForList("""
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
        """);
    }

    public Map<String, Object> findTopBuyerForMonth(int year, int month) {
        return jdbcTemplate.queryForMap("""
            SELECT buyer_id, SUM(total_amount) AS total
            FROM invoice
            WHERE EXTRACT(YEAR FROM date) = ?
              AND EXTRACT(MONTH FROM date) = ?
            GROUP BY buyer_id
            ORDER BY total DESC
            LIMIT 1
        """, year, month);
    }

    public void insertMonthlyReport(int year, int month, double totalRevenue,
                                    double averageAmount, int invoiceCount, Long topBuyerId) {
        double roundedRevenue = Math.round(totalRevenue * 100.0) / 100.0;
        double roundedAverage = Math.round(averageAmount * 100.0) / 100.0;

        jdbcTemplate.update("""
            INSERT INTO monthly_report 
            (year, month, total_revenue, average_amount, invoice_count, top_buyer_id)
            VALUES (?, ?, ?, ?, ?, ?)
        """, year, month, roundedRevenue, roundedAverage, invoiceCount, topBuyerId);
    }

    // ===== BATCH 4 =====
    public void createArchiveTablesIfNotExists() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS invoice_archive (
                id INTEGER PRIMARY KEY,
                date TIMESTAMP,
                total_amount DOUBLE PRECISION,
                user_id BIGINT,
                buyer_id BIGINT,
                archived_at TIMESTAMP DEFAULT NOW()
            )
        """);
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS invoice_item_archive (
                invoice_id BIGINT,
                item_no INTEGER,
                price DOUBLE PRECISION,
                block_id BIGINT,
                archived_at TIMESTAMP DEFAULT NOW()
            )
        """);
    }

    public List<Map<String, Object>> findInvoicesOlderThanTwoYears() {
        return jdbcTemplate.queryForList("""
            SELECT id FROM invoice
            WHERE date < NOW() - INTERVAL '2 years'
        """);
    }

    public void archiveInvoice(Long invoiceId) {
        jdbcTemplate.update("""
            INSERT INTO invoice_archive (id, date, total_amount, user_id, buyer_id)
            SELECT id, date, total_amount, user_id, buyer_id FROM invoice WHERE id = ?
        """, invoiceId);
    }

    public void archiveInvoiceItems(Long invoiceId) {
        jdbcTemplate.update("""
            INSERT INTO invoice_item_archive (invoice_id, item_no, price, block_id)
            SELECT invoice_id, item_no, price, block_id FROM invoice_item WHERE invoice_id = ?
        """, invoiceId);
    }

    public void deleteInvoiceItems(Long invoiceId) {
        jdbcTemplate.update("DELETE FROM invoice_item WHERE invoice_id = ?", invoiceId);
    }

    public void deleteInvoice(Long invoiceId) {
        jdbcTemplate.update("DELETE FROM invoice WHERE id = ?", invoiceId);
    }
}
