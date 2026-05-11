package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.model.BatchResult;
import com.marija.quarry_batch.service.ManualBatchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch/manual")
public class BatchController {

    private final ManualBatchService manualBatchService;

    public BatchController(ManualBatchService manualBatchService) {
        this.manualBatchService = manualBatchService;
    }

    @PostMapping("/1")
    public BatchResult runBatch1() {
        return manualBatchService.reconcileInvoiceAmounts();
    }

    @PostMapping("/2")
    public BatchResult runBatch2() {
        return manualBatchService.classifyBlocks();
    }

    @PostMapping("/3")
    public BatchResult runBatch3() {
        return manualBatchService.generateMonthlyReport();
    }

    @PostMapping("/4")
    public BatchResult runBatch4() {
        return manualBatchService.archiveOldInvoices();
    }
}
