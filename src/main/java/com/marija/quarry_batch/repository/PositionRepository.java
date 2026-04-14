package com.marija.quarry_batch.repository;

import com.marija.quarry_batch.model.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PositionRepository {

    private final JdbcTemplate jdbcTemplate;

    public PositionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Position position) {
        String sql = "INSERT INTO position (name) VALUES (?)";
        jdbcTemplate.update(sql, position.getName());
    }

    public List<Position> findAll() {
        String sql = "SELECT * FROM position";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Position(
                        rs.getLong("id"),
                        rs.getString("name")
                )
        );
    }

}
