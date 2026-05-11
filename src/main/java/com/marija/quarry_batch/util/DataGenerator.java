package com.marija.quarry_batch.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class DataGenerator {

    private final JdbcTemplate jdbcTemplate;
    private final Random random = new Random();

    public DataGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void generateBuyers(int count) {
        for (int i = 0; i < count; i++) {
            if (i % 2 == 0) {
                jdbcTemplate.update(
                        "INSERT INTO buyer (name, phone_number, email, buyer_type, personal_id) VALUES (?, ?, ?, 'INDIVIDUAL', ?)",
                        "Buyer " + i,
                        "06" + randomDigits(8),
                        "buyer" + i + "@mail.com",
                        "PID" + randomDigits(9)
                );
            } else {
                jdbcTemplate.update(
                        "INSERT INTO buyer (name, phone_number, email, buyer_type, tax_id) VALUES (?, ?, ?, 'COMPANY', ?)",
                        "Company " + i,
                        "01" + randomDigits(8),
                        "company" + i + "@mail.com",
                        "TID" + randomDigits(9)
                );
            }
        }
    }

    public void generateBlocks(int count) {
        String[] qualities = {"A", "B", "C"};
        String[] categories = {"1", "2", "3"};

        for (int i = 0; i < count; i++) {
            double length = 150 + random.nextDouble() * 350; // 150-500 cm
            double width  = 100 + random.nextDouble() * 250; // 100-350 cm
            double height = 80  + random.nextDouble() * 170; // 80-250 cm

            double volumeM3 = (length * width * height) / 1_000_000.0;
            double mass     = volumeM3 * 2.75;

            jdbcTemplate.update(
                    "INSERT INTO block (length, width, height, mass, quality_class, category) VALUES (?, ?, ?, ?, ?, ?)",
                    round(length),
                    round(width),
                    round(height),
                    round(mass),
                    qualities[random.nextInt(qualities.length)],
                    categories[random.nextInt(categories.length)]
            );
        }
    }

    public void generateInvoices(int count) {
        List<Long> buyerIds = jdbcTemplate.queryForList("SELECT id FROM buyer", Long.class);
        List<Long> blockIds  = jdbcTemplate.queryForList("SELECT id FROM block", Long.class);
        List<Long> userIds   = jdbcTemplate.queryForList("SELECT id FROM users", Long.class);

        if (buyerIds.isEmpty()) throw new RuntimeException("No buyers in database. Call generateBuyers() first.");
        if (blockIds.isEmpty()) throw new RuntimeException("No blocks in database. Call generateBlocks() first.");
        if (userIds.isEmpty())  throw new RuntimeException("No users in database.");

        for (int i = 0; i < count; i++) {
            Long buyerId = buyerIds.get(random.nextInt(buyerIds.size()));
            Long userId  = userIds.get(random.nextInt(userIds.size()));
            Date date    = randomDate();

            Long invoiceId = jdbcTemplate.queryForObject(
                    "INSERT INTO invoice (date, total_amount, user_id, buyer_id) VALUES (?, ?, ?, ?) RETURNING id",
                    Long.class,
                    date, 0.0, userId, buyerId
            );

            int itemCount = 2 + random.nextInt(5);
            double total = 0.0;

            for (int j = 1; j <= itemCount; j++) {
                Long blockId = blockIds.get(random.nextInt(blockIds.size()));
                double price = round(100 + random.nextDouble() * 9900);
                total += price;

                jdbcTemplate.update(
                        "INSERT INTO invoice_item (invoice_id, item_no, price, block_id) VALUES (?, ?, ?, ?)",
                        invoiceId, j, price, blockId
                );
            }

            double savedTotal = (i % 5 == 0) ? round(total * 0.85) : round(total);

            jdbcTemplate.update(
                    "UPDATE invoice SET total_amount = ? WHERE id = ?",
                    savedTotal, invoiceId
            );
        }
    }

    public void clearInvoices() {
        jdbcTemplate.update("DELETE FROM invoice_item");
        jdbcTemplate.update("DELETE FROM invoice");
    }

    public void clearBlocks() {
        jdbcTemplate.update("DELETE FROM block");
    }

    public void clearBuyers() {
        jdbcTemplate.update("DELETE FROM invoice_item");
        jdbcTemplate.update("DELETE FROM invoice");
        jdbcTemplate.update("DELETE FROM buyer");
    }

    public void clearAll() {
        jdbcTemplate.update("DELETE FROM invoice_item_archive");
        jdbcTemplate.update("DELETE FROM invoice_archive");
        jdbcTemplate.update("DELETE FROM monthly_report");
        jdbcTemplate.update("DELETE FROM invoice_item");
        jdbcTemplate.update("DELETE FROM invoice");
        jdbcTemplate.update("DELETE FROM block");
        jdbcTemplate.update("DELETE FROM buyer");

        jdbcTemplate.execute("DROP TABLE IF EXISTS invoice_item_archive");
        jdbcTemplate.execute("DROP TABLE IF EXISTS invoice_archive");
        jdbcTemplate.execute("DROP TABLE IF EXISTS monthly_report");
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private String randomDigits(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(random.nextInt(10));
        return sb.toString();
    }

    private Date randomDate() {
        long now = System.currentTimeMillis();
        long threeYearsMs = 3L * 365 * 24 * 60 * 60 * 1000;
        return new Date(now - (long)(random.nextDouble() * threeYearsMs));
    }
}