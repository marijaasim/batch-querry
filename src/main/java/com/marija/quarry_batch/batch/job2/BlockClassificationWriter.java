package com.marija.quarry_batch.batch.job2;

import com.marija.quarry_batch.model.Block;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

public class BlockClassificationWriter implements ItemWriter<Block> {

    private final JdbcTemplate jdbcTemplate;

    public BlockClassificationWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(Chunk<? extends Block> chunk) {
        for (Block block : chunk) {
            jdbcTemplate.update(
                    "UPDATE block SET category = ? WHERE id = ?",
                    block.getCategory(),
                    block.getId()
            );
        }
    }
}