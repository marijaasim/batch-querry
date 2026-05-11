package com.marija.quarry_batch.model;

public class BatchResult {

    private long executionTimeMs;
    private int processedRecords;

    public BatchResult(long executionTimeMs, int processedRecords) {
        this.executionTimeMs = executionTimeMs;
        this.processedRecords = processedRecords;
    }

    public long getExecutionTimeMs() { return executionTimeMs; }
    public int getProcessedRecords() { return processedRecords; }

}
