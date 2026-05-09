package com.marija.quarry_batch.repository;

import com.marija.quarry_batch.model.Buyer;
import com.marija.quarry_batch.model.CompanyBuyer;
import com.marija.quarry_batch.model.IndividualBuyer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BuyerRepository {

    private final JdbcTemplate jdbcTemplate;

    public BuyerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveIndividual(IndividualBuyer buyer) {
        String sql = "INSERT INTO buyer (name, phone_number, email, buyer_type, personal_id) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                buyer.getName(),
                buyer.getPhoneNumber(),
                buyer.getEmail(),
                "INDIVIDUAL",
                buyer.getPersonalId()
        );
    }

    public void saveCompany(CompanyBuyer buyer) {
        String sql = "INSERT INTO buyer (name, phone_number, email, buyer_type, tax_id) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                buyer.getName(),
                buyer.getPhoneNumber(),
                buyer.getEmail(),
                "COMPANY",
                buyer.getTaxId()
        );
    }

    private Buyer mapBuyer(ResultSet rs) throws SQLException {

        String type = rs.getString("buyer_type");

        if ("INDIVIDUAL".equalsIgnoreCase(type)) {
            IndividualBuyer b = new IndividualBuyer();
            b.setId(rs.getLong("id"));
            b.setName(rs.getString("name"));
            b.setPhoneNumber(rs.getString("phone_number"));
            b.setEmail(rs.getString("email"));
            b.setPersonalId(rs.getString("personal_id"));
            b.setBuyerType("INDIVIDUAL");  // dodaj ovo
            return b;
        } else if ("COMPANY".equalsIgnoreCase(type)) {
            CompanyBuyer b = new CompanyBuyer();
            b.setId(rs.getLong("id"));
            b.setName(rs.getString("name"));
            b.setPhoneNumber(rs.getString("phone_number"));
            b.setEmail(rs.getString("email"));
            b.setTaxId(rs.getString("tax_id"));
            b.setBuyerType("COMPANY");  // dodaj ovo
            return b;
        } else {
            throw new RuntimeException("Unknown buyer type: " + type);
        }
    }

    public List<Buyer> findAll() {
        String sql = "SELECT * FROM buyer";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapBuyer(rs));
    }

    public List<Buyer> search(String name, String email, String type) {

        StringBuilder sql = new StringBuilder("SELECT * FROM buyer WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + name + "%");
        }

        if (email != null && !email.isBlank()) {
            sql.append(" AND LOWER(email) LIKE LOWER(?)");
            params.add("%" + email + "%");
        }

        if (type != null && !type.isBlank()) {
            sql.append(" AND buyer_type = ?");
            params.add(type);
        }

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> mapBuyer(rs)
        );
    }

    public void update(Buyer buyer) {

        if (buyer instanceof IndividualBuyer) {
            String sql = """
            UPDATE buyer 
            SET name = ?, phone_number = ?, email = ?, personal_id = ?, tax_id = NULL, buyer_type = 'INDIVIDUAL'
            WHERE id = ?
        """;

            jdbcTemplate.update(
                    sql,
                    buyer.getName(),
                    buyer.getPhoneNumber(),
                    buyer.getEmail(),
                    ((IndividualBuyer) buyer).getPersonalId(),
                    buyer.getId()
            );

        } else if (buyer instanceof CompanyBuyer) {
            String sql = """
            UPDATE buyer 
            SET name = ?, phone_number = ?, email = ?, tax_id = ?, personal_id = NULL, buyer_type = 'COMPANY'
            WHERE id = ?
        """;

            jdbcTemplate.update(
                    sql,
                    buyer.getName(),
                    buyer.getPhoneNumber(),
                    buyer.getEmail(),
                    ((CompanyBuyer) buyer).getTaxId(),
                    buyer.getId()
            );
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM buyer WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
