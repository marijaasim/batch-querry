package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.util.DataGenerator;
import com.marija.quarry_batch.util.MemoryPeakTracker;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/generate")
public class DataGeneratorController {

    private final DataGenerator dataGenerator;
    private final MemoryPeakTracker memoryPeakTracker;

    public DataGeneratorController(DataGenerator dataGenerator, MemoryPeakTracker memoryPeakTracker) {
        this.dataGenerator = dataGenerator;
        this.memoryPeakTracker = memoryPeakTracker;
    }

    @PostMapping("/buyers")
    public String generateBuyers(@RequestParam int count) {
        dataGenerator.generateBuyers(count);
        return "Generated " + count + " buyers.";
    }

    @PostMapping("/blocks")
    public String generateBlocks(@RequestParam int count) {
        dataGenerator.generateBlocks(count);
        return "Generated " + count + " blocks.";
    }

    @PostMapping("/invoices")
    public String generateInvoices(@RequestParam int count) {
        dataGenerator.generateInvoices(count);
        return "Generated " + count + " invoices.";
    }

    @DeleteMapping("/clear")
    public String clearAll() {
        dataGenerator.clearAll();
        return "All data cleared.";
    }

    @GetMapping("/memory-peak")
    public Map<String, Object> getPeak() {
        long peak = memoryPeakTracker.getPeakHeapUsed();
        return Map.of(
                "peakHeapUsedBytes", peak,
                "peakHeapUsedMB", peak / 1_048_576
        );
    }

    @PostMapping("/memory-peak/reset")
    public void reset() {
        memoryPeakTracker.reset();
    }

}
