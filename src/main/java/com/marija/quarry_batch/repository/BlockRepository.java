package com.marija.quarry_batch.repository;

import com.marija.quarry_batch.model.Block;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class BlockRepository {

    private final JdbcTemplate jdbcTemplate;

    public BlockRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Block> getAll() {
        String sql = "SELECT * FROM block";

        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) ->
                new Block(
                        rs.getLong("id"),
                        rs.getDouble("length"),
                        rs.getDouble("width"),
                        rs.getDouble("height"),
                        rs.getDouble("mass"),
                        rs.getString("quality_class"),
                        rs.getString("category")
                )
        );
    }

}
