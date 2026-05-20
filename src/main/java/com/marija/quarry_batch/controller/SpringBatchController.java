package com.marija.quarry_batch.controller;

import com.marija.quarry_batch.model.BatchResult;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch/spring")
public class SpringBatchController {

    private final JobLauncher jobLauncher;
    private final Job invoiceReconciliationJob;
    private final Job blockClassificationJob;
    private final Job monthlyReportJob;
    private final Job archiveInvoicesJob;

    public SpringBatchController(
            JobLauncher jobLauncher,
            @Qualifier("invoiceReconciliationJob") Job invoiceReconciliationJob,
            @Qualifier("blockClassificationJob") Job blockClassificationJob,
            @Qualifier("monthlyReportJob") Job monthlyReportJob,
            @Qualifier("archiveInvoicesJob") Job archiveInvoicesJob) {
        this.jobLauncher = jobLauncher;
        this.invoiceReconciliationJob = invoiceReconciliationJob;
        this.blockClassificationJob = blockClassificationJob;
        this.monthlyReportJob = monthlyReportJob;
        this.archiveInvoicesJob = archiveInvoicesJob;
    }

    @PostMapping("/1")
    public BatchResult runJob1() throws Exception {
        return runJob(invoiceReconciliationJob);
    }

    @PostMapping("/2")
    public BatchResult runJob2() throws Exception {
        return runJob(blockClassificationJob);
    }

    @PostMapping("/3")
    public BatchResult runJob3() throws Exception {
        return runJob(monthlyReportJob);
    }

    @PostMapping("/4")
    public BatchResult runJob4() throws Exception {
        return runJob(archiveInvoicesJob);
    }

    private BatchResult runJob(Job job) throws Exception {
        long start = System.currentTimeMillis();

        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, params);

        long end = System.currentTimeMillis();

        int processedRecords = execution.getStepExecutions().stream()
                .mapToInt(step -> (int) step.getWriteCount())
                .sum();

        return new BatchResult(end - start, processedRecords);
    }
}
