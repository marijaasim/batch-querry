package com.marija.quarry_batch.service;

import com.marija.quarry_batch.model.Block;
import com.marija.quarry_batch.repository.BlockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {

    private final BlockRepository blockRepository;

    public BlockService(BlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    public List<Block> getAll() {
        return blockRepository.getAll();
    }
}
