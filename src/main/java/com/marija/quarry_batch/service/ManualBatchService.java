package com.marija.quarry_batch.service;

import com.marija.quarry_batch.model.BatchResult;
import com.marija.quarry_batch.repository.BatchRepository;
import com.marija.quarry_batch.repository.ManualBatchExecutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManualBatchService {

    private final BatchRepository batchRepository;
    private final ManualBatchExecutionRepository executionRepository;

    public ManualBatchService(BatchRepository batchRepository,
                              ManualBatchExecutionRepository executionRepository) {
        this.batchRepository = batchRepository;
        this.executionRepository = executionRepository;
    }

    @Transactional
    public BatchResult reconcileInvoiceAmounts() {
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        long start = System.currentTimeMillis();

        // simulacija 4 HashMap-a za ostale Spring Batch tabele
        Map<String, Object> jobInstance = new HashMap<>();
        Map<String, Object> jobExecution = new HashMap<>();
        Map<String, Object> jobExecutionParams = new HashMap<>();
        Map<String, Object> jobExecutionContext = new HashMap<>();

        jobInstance.put("job_name", "reconcileInvoiceAmounts");
        jobExecution.put("status", "STARTED");
        jobExecution.put("start_time", startTime);
        jobExecutionParams.put("run_id", start);
        jobExecutionContext.put("data", "{}");

        // INSERT u dve tabele
        Long executionId = executionRepository.insertExecution("reconcileInvoiceAmounts", startTime);
        executionRepository.insertContext(executionId);

        int readCount = 0;
        int writeCount = 0;
        int filterCount = 0;
        int commitCount = 0;
        int chunkCounter = 0;
        final int CHUNK_SIZE = 100;

        List<Map<String, Object>> invoices = batchRepository.findAllInvoicesWithTotal();
        readCount = invoices.size();

        for (Map<String, Object> invoice : invoices) {
            Long invoiceId = ((Number) invoice.get("id")).longValue();
            double storedTotal = ((Number) invoice.get("total_amount")).doubleValue();

            Double calculatedTotal = batchRepository.calculateInvoiceTotal(invoiceId);
            if (calculatedTotal == null) calculatedTotal = 0.0;

            if (Math.abs(storedTotal - calculatedTotal) > 0.01) {
                batchRepository.updateInvoiceTotalAmount(invoiceId, calculatedTotal);
                writeCount++;
            } else {
                filterCount++;
            }

            chunkCounter++;
            if (chunkCounter == CHUNK_SIZE) {
                commitCount++;
                chunkCounter = 0;
                // UPDATE posle svakog chunk-a u obe tabele
                executionRepository.updateChunk(executionId, readCount, writeCount, filterCount, commitCount);
            }
        }

        // finalni commit
        commitCount++;

        long end = System.currentTimeMillis();
        Timestamp endTime = new Timestamp(end);

        // finalni UPDATE u obe tabele
        executionRepository.updateFinal(executionId, endTime, end - start,
                readCount, writeCount, filterCount, commitCount);

        // simulacija UPDATE za jobExecution i jobExecutionContext
        jobExecution.put("status", "COMPLETED");
        jobExecution.put("end_time", endTime);
        jobExecutionContext.put("data", "{}");

        //System.out.println("jobInstance: " + jobInstance);
        //System.out.println("jobExecution: " + jobExecution);
        //System.out.println("jobExecutionParams: " + jobExecutionParams);
        //System.out.println("jobExecutionContext: " + jobExecutionContext);

        return new BatchResult(end - start, writeCount);
    }

    @Transactional
    public BatchResult classifyBlocks() {
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        long start = System.currentTimeMillis();

        Map<String, Object> jobInstance = new HashMap<>();
        Map<String, Object> jobExecution = new HashMap<>();
        Map<String, Object> jobExecutionParams = new HashMap<>();
        Map<String, Object> jobExecutionContext = new HashMap<>();

        jobInstance.put("job_name", "classifyBlocks");
        jobExecution.put("status", "STARTED");
        jobExecution.put("start_time", startTime);
        jobExecutionParams.put("run_id", start);
        jobExecutionContext.put("data", "{}");

        Long executionId = executionRepository.insertExecution("classifyBlocks", startTime);
        executionRepository.insertContext(executionId);

        int readCount = 0;
        int writeCount = 0;
        int filterCount = 0;
        int commitCount = 0;
        int chunkCounter = 0;
        final int CHUNK_SIZE = 100;

        List<Map<String, Object>> blocks = batchRepository.findAllBlocksWithDimensions();
        readCount = blocks.size();

        for (Map<String, Object> block : blocks) {
            Long blockId = ((Number) block.get("id")).longValue();
            double length = ((Number) block.get("length")).doubleValue();
            double width  = ((Number) block.get("width")).doubleValue();
            double height = ((Number) block.get("height")).doubleValue();
            String currentCategory = (String) block.get("category");

            double volumeM3 = (length * width * height) / 1_000_000.0;
            String newCategory;

            if (volumeM3 > 6.0) {
                newCategory = "1";
            } else if (volumeM3 >= 3.0) {
                newCategory = "2";
            } else {
                newCategory = "3";
            }

            if (!newCategory.equals(currentCategory)) {
                batchRepository.updateBlockCategory(blockId, newCategory);
                writeCount++;
            } else {
                filterCount++;
            }

            chunkCounter++;
            if (chunkCounter == CHUNK_SIZE) {
                commitCount++;
                chunkCounter = 0;
                executionRepository.updateChunk(executionId, readCount, writeCount, filterCount, commitCount);
            }
        }

        commitCount++;

        long end = System.currentTimeMillis();
        Timestamp endTime = new Timestamp(end);

        executionRepository.updateFinal(executionId, endTime, end - start,
                readCount, writeCount, filterCount, commitCount);

        jobExecution.put("status", "COMPLETED");
        jobExecution.put("end_time", endTime);
        jobExecutionContext.put("data", "{}");

        //System.out.println("jobInstance: " + jobInstance);
        //System.out.println("jobExecution: " + jobExecution);
        //System.out.println("jobExecutionParams: " + jobExecutionParams);
        //System.out.println("jobExecutionContext: " + jobExecutionContext);

        return new BatchResult(end - start, writeCount);
    }

    @Transactional
    public BatchResult generateMonthlyReport() {
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        long start = System.currentTimeMillis();

        Map<String, Object> jobInstance = new HashMap<>();
        Map<String, Object> jobExecution = new HashMap<>();
        Map<String, Object> jobExecutionParams = new HashMap<>();
        Map<String, Object> jobExecutionContext = new HashMap<>();

        jobInstance.put("job_name", "generateMonthlyReport");
        jobExecution.put("status", "STARTED");
        jobExecution.put("start_time", startTime);
        jobExecutionParams.put("run_id", start);
        jobExecutionContext.put("data", "{}");

        Long executionId = executionRepository.insertExecution("generateMonthlyReport", startTime);
        executionRepository.insertContext(executionId);

        batchRepository.createMonthlyReportTableIfNotExists();
        batchRepository.clearMonthlyReport();

        int readCount = 0;
        int writeCount = 0;
        int filterCount = 0;
        int commitCount = 0;
        int chunkCounter = 0;
        final int CHUNK_SIZE = 50;

        List<Map<String, Object>> monthlyData = batchRepository.findMonthlyAggregates();
        readCount = monthlyData.size();

        for (Map<String, Object> row : monthlyData) {
            int year  = ((Number) row.get("year")).intValue();
            int month = ((Number) row.get("month")).intValue();
            double totalRevenue  = ((Number) row.get("total_revenue")).doubleValue();
            double averageAmount = ((Number) row.get("average_amount")).doubleValue();
            int invoiceCount     = ((Number) row.get("invoice_count")).intValue();

            Map<String, Object> topBuyer = batchRepository.findTopBuyerForMonth(year, month);
            Long topBuyerId = ((Number) topBuyer.get("buyer_id")).longValue();

            batchRepository.insertMonthlyReport(year, month, totalRevenue,
                    averageAmount, invoiceCount, topBuyerId);
            writeCount++;

            chunkCounter++;
            if (chunkCounter == CHUNK_SIZE) {
                commitCount++;
                chunkCounter = 0;
                executionRepository.updateChunk(executionId, readCount, writeCount, filterCount, commitCount);
            }
        }

        commitCount++;

        long end = System.currentTimeMillis();
        Timestamp endTime = new Timestamp(end);

        executionRepository.updateFinal(executionId, endTime, end - start,
                readCount, writeCount, filterCount, commitCount);

        jobExecution.put("status", "COMPLETED");
        jobExecution.put("end_time", endTime);
        jobExecutionContext.put("data", "{}");

        //System.out.println("jobInstance: " + jobInstance);
        //System.out.println("jobExecution: " + jobExecution);
        //System.out.println("jobExecutionParams: " + jobExecutionParams);
        //System.out.println("jobExecutionContext: " + jobExecutionContext);

        return new BatchResult(end - start, writeCount);
    }

    @Transactional
    public BatchResult archiveOldInvoices() {
        Timestamp startTime = new Timestamp(System.currentTimeMillis());
        long start = System.currentTimeMillis();

        Map<String, Object> jobInstance = new HashMap<>();
        Map<String, Object> jobExecution = new HashMap<>();
        Map<String, Object> jobExecutionParams = new HashMap<>();
        Map<String, Object> jobExecutionContext = new HashMap<>();

        jobInstance.put("job_name", "archiveOldInvoices");
        jobExecution.put("status", "STARTED");
        jobExecution.put("start_time", startTime);
        jobExecutionParams.put("run_id", start);
        jobExecutionContext.put("data", "{}");

        batchRepository.createArchiveTablesIfNotExists();

        Long executionId = executionRepository.insertExecution("archiveOldInvoices", startTime);
        executionRepository.insertContext(executionId);

        int readCount = 0;
        int writeCount = 0;
        int filterCount = 0;
        int commitCount = 0;
        int chunkCounter = 0;
        final int CHUNK_SIZE = 100;

        List<Map<String, Object>> oldInvoices = batchRepository.findInvoicesOlderThanTwoYears();
        readCount = oldInvoices.size();

        for (Map<String, Object> row : oldInvoices) {
            Long invoiceId = ((Number) row.get("id")).longValue();

            batchRepository.archiveInvoice(invoiceId);
            batchRepository.archiveInvoiceItems(invoiceId);
            batchRepository.deleteInvoiceItems(invoiceId);
            batchRepository.deleteInvoice(invoiceId);
            writeCount++;

            chunkCounter++;
            if (chunkCounter == CHUNK_SIZE) {
                commitCount++;
                chunkCounter = 0;
                executionRepository.updateChunk(executionId, readCount, writeCount, filterCount, commitCount);
            }
        }

        commitCount++;

        long end = System.currentTimeMillis();
        Timestamp endTime = new Timestamp(end);

        executionRepository.updateFinal(executionId, endTime, end - start,
                readCount, writeCount, filterCount, commitCount);

        jobExecution.put("status", "COMPLETED");
        jobExecution.put("end_time", endTime);
        jobExecutionContext.put("data", "{}");

        //System.out.println("jobInstance: " + jobInstance);
        //System.out.println("jobExecution: " + jobExecution);
        //System.out.println("jobExecutionParams: " + jobExecutionParams);
        //System.out.println("jobExecutionContext: " + jobExecutionContext);

        return new BatchResult(end - start, writeCount);
    }
}