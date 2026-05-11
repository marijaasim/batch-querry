package com.marija.quarry_batch.service;

import com.marija.quarry_batch.model.BatchResult;
import com.marija.quarry_batch.repository.BatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ManualBatchService {

    private final BatchRepository batchRepository;

    public ManualBatchService(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Transactional
    public BatchResult reconcileInvoiceAmounts() {
        long start = System.currentTimeMillis();
        int processed = 0;

        List<Map<String, Object>> invoices = batchRepository.findAllInvoicesWithTotal();

        for (Map<String, Object> invoice : invoices) {
            Long invoiceId = ((Number) invoice.get("id")).longValue();
            double storedTotal = ((Number) invoice.get("total_amount")).doubleValue();

            Double calculatedTotal = batchRepository.calculateInvoiceTotal(invoiceId);
            if (calculatedTotal == null) calculatedTotal = 0.0;

            if (Math.abs(storedTotal - calculatedTotal) > 0.01) {
                batchRepository.updateInvoiceTotalAmount(invoiceId, calculatedTotal);
                processed++;
            }
        }

        long end = System.currentTimeMillis();
        return new BatchResult(end - start, processed);
    }

    @Transactional
    public BatchResult classifyBlocks() {
        long start = System.currentTimeMillis();
        int processed = 0;

        List<Map<String, Object>> blocks = batchRepository.findAllBlocksWithDimensions();

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
                processed++;
            }
        }

        long end = System.currentTimeMillis();
        return new BatchResult(end - start, processed);
    }

    @Transactional
    public BatchResult generateMonthlyReport() {
        long start = System.currentTimeMillis();
        int processed = 0;

        batchRepository.createMonthlyReportTableIfNotExists();
        batchRepository.clearMonthlyReport();

        List<Map<String, Object>> monthlyData = batchRepository.findMonthlyAggregates();

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
            processed++;
        }

        long end = System.currentTimeMillis();
        return new BatchResult(end - start, processed);
    }

    @Transactional
    public BatchResult archiveOldInvoices() {
        long start = System.currentTimeMillis();
        int processed = 0;

        batchRepository.createArchiveTablesIfNotExists();

        List<Map<String, Object>> oldInvoices = batchRepository.findInvoicesOlderThanTwoYears();

        for (Map<String, Object> row : oldInvoices) {
            Long invoiceId = ((Number) row.get("id")).longValue();

            batchRepository.archiveInvoice(invoiceId);
            batchRepository.archiveInvoiceItems(invoiceId);
            batchRepository.deleteInvoiceItems(invoiceId);
            batchRepository.deleteInvoice(invoiceId);

            processed++;
        }

        long end = System.currentTimeMillis();
        return new BatchResult(end - start, processed);
    }
}
