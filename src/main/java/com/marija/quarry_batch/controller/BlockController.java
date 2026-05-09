package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.model.Block;
import com.marija.quarry_batch.service.BlockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blocks")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @GetMapping
    public List<Block> getAll() {
        return blockService.getAll();
    }

}
