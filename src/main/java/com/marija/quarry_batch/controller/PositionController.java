package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.model.Position;
import com.marija.quarry_batch.service.PositionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    public void createPosition(@RequestBody Position position) {
        positionService.createPosition(position);
    }

    @GetMapping
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }
}
