package com.marija.quarry_batch.repository;

import com.marija.quarry_batch.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{email, password},
                rs -> {
                    if (rs.next()) {
                        return new User(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password")
                        );
                    }
                    return null;
                }
        );
    }
}
