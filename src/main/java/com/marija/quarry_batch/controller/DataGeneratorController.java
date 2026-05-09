package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.util.DataGenerator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/generate")
public class DataGeneratorController {

    private final DataGenerator dataGenerator;

    public DataGeneratorController(DataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
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
        dataGenerator.clearInvoices();
        dataGenerator.clearBlocks();
        dataGenerator.clearBuyers();
        return "All data cleared.";
    }

}
