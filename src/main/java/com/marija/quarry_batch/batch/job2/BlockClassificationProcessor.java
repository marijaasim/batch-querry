package com.marija.quarry_batch.batch.job2;

import com.marija.quarry_batch.model.Block;
import org.springframework.batch.item.ItemProcessor;

public class BlockClassificationProcessor implements ItemProcessor<Block, Block> {

    @Override
    public Block process(Block block) {
        double volumeM3 = (block.getLength() * block.getWidth() * block.getHeight()) / 1_000_000.0;

        String newCategory;
        if (volumeM3 > 6.0) {
            newCategory = "1";
        } else if (volumeM3 >= 3.0) {
            newCategory = "2";
        } else {
            newCategory = "3";
        }

        if (!newCategory.equals(block.getCategory())) {
            block.setCategory(newCategory);
            return block;
        }

        return null;
    }
}