package com.marija.quarry_batch.batch.job2;

import com.marija.quarry_batch.model.Block;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;

import javax.sql.DataSource;

public class BlockItemReader {

    public static JdbcCursorItemReader<Block> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Block>()
                .name("blockReader")
                .dataSource(dataSource)
                .sql("SELECT id, length, width, height, category FROM block")
                .rowMapper((rs, rowNum) -> {
                    Block block = new Block();
                    block.setId(rs.getLong("id"));
                    block.setLength(rs.getDouble("length"));
                    block.setWidth(rs.getDouble("width"));
                    block.setHeight(rs.getDouble("height"));
                    block.setCategory(rs.getString("category"));
                    return block;
                })
                .build();
    }
}