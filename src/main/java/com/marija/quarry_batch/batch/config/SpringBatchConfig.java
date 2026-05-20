package com.marija.quarry_batch.batch.config;

import com.marija.quarry_batch.batch.job1.InvoiceItemReader;
import com.marija.quarry_batch.batch.job1.InvoiceReconciliationProcessor;
import com.marija.quarry_batch.batch.job1.InvoiceReconciliationWriter;
import com.marija.quarry_batch.batch.job2.BlockClassificationProcessor;
import com.marija.quarry_batch.batch.job2.BlockClassificationWriter;
import com.marija.quarry_batch.batch.job2.BlockItemReader;
import com.marija.quarry_batch.batch.job3.MonthlyReportItem;
import com.marija.quarry_batch.batch.job3.MonthlyReportProcessor;
import com.marija.quarry_batch.batch.job3.MonthlyReportReader;
import com.marija.quarry_batch.batch.job3.MonthlyReportWriter;
import com.marija.quarry_batch.batch.job4.ArchiveInvoice;
import com.marija.quarry_batch.batch.job4.ArchiveInvoiceProcessor;
import com.marija.quarry_batch.batch.job4.ArchiveInvoiceReader;
import com.marija.quarry_batch.batch.job4.ArchiveInvoiceWriter;
import com.marija.quarry_batch.model.Block;
import com.marija.quarry_batch.model.Invoice;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class SpringBatchConfig {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public SpringBatchConfig(DataSource dataSource, JdbcTemplate jdbcTemplate,
                             JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    // ===== JOB 1 — Invoice Reconciliation =====

    @Bean
    public Job invoiceReconciliationJob() {
        return new JobBuilder("invoiceReconciliationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(invoiceReconciliationStep())
                .build();
    }

    @Bean
    public Step invoiceReconciliationStep() {
        return new StepBuilder("invoiceReconciliationStep", jobRepository)
                .<Invoice, Invoice>chunk(100, transactionManager)
                .reader(InvoiceItemReader.reader(dataSource))
                .processor(new InvoiceReconciliationProcessor(jdbcTemplate))
                .writer(new InvoiceReconciliationWriter(jdbcTemplate))
                .build();
    }

    // ===== JOB 2 — Block Classification =====

    @Bean
    public Job blockClassificationJob() {
        return new JobBuilder("blockClassificationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(blockClassificationStep())
                .build();
    }

    @Bean
    public Step blockClassificationStep() {
        return new StepBuilder("blockClassificationStep", jobRepository)
                .<Block, Block>chunk(100, transactionManager)
                .reader(BlockItemReader.reader(dataSource))
                .processor(new BlockClassificationProcessor())
                .writer(new BlockClassificationWriter(jdbcTemplate))
                .build();
    }

    // ===== JOB 3 — Monthly Report =====

    @Bean
    public Job monthlyReportJob() {
        return new JobBuilder("monthlyReportJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(monthlyReportJobListener())
                .start(monthlyReportStep())
                .build();
    }

    @Bean
    public Step monthlyReportStep() {
        return new StepBuilder("monthlyReportStep", jobRepository)
                .<MonthlyReportItem, MonthlyReportItem>chunk(50, transactionManager)
                .reader(MonthlyReportReader.reader(dataSource))
                .processor(new MonthlyReportProcessor(jdbcTemplate))
                .writer(new MonthlyReportWriter(jdbcTemplate))
                .build();
    }

    @Bean
    public JobExecutionListener monthlyReportJobListener() {
        return new JobExecutionListenerSupport() {
            @Override
            public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
                jdbcTemplate.execute("""
                    CREATE TABLE IF NOT EXISTS monthly_report (
                        id SERIAL PRIMARY KEY,
                        year INTEGER,
                        month INTEGER,
                        total_revenue DOUBLE PRECISION,
                        average_amount DOUBLE PRECISION,
                        invoice_count INTEGER,
                        top_buyer_id BIGINT,
                        generated_at TIMESTAMP DEFAULT NOW()
                    )
                """);
                jdbcTemplate.execute("DELETE FROM monthly_report");
            }
        };
    }

    // ===== JOB 4 — Archive Invoices =====

    @Bean
    public Job archiveInvoicesJob() {
        return new JobBuilder("archiveInvoicesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(archiveInvoicesJobListener())
                .start(archiveInvoicesStep())
                .build();
    }

    @Bean
    public Step archiveInvoicesStep() {
        return new StepBuilder("archiveInvoicesStep", jobRepository)
                .<ArchiveInvoice, ArchiveInvoice>chunk(100, transactionManager)
                .reader(ArchiveInvoiceReader.reader(dataSource))
                .processor(new ArchiveInvoiceProcessor())
                .writer(new ArchiveInvoiceWriter(jdbcTemplate))
                .build();
    }

    @Bean
    public JobExecutionListener archiveInvoicesJobListener() {
        return new JobExecutionListenerSupport() {
            @Override
            public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
                jdbcTemplate.execute("""
                    CREATE TABLE IF NOT EXISTS invoice_archive (
                        id INTEGER PRIMARY KEY,
                        date TIMESTAMP,
                        total_amount DOUBLE PRECISION,
                        user_id BIGINT,
                        buyer_id BIGINT,
                        archived_at TIMESTAMP DEFAULT NOW()
                    )
                """);
                jdbcTemplate.execute("""
                    CREATE TABLE IF NOT EXISTS invoice_item_archive (
                        invoice_id BIGINT,
                        item_no INTEGER,
                        price DOUBLE PRECISION,
                        block_id BIGINT,
                        archived_at TIMESTAMP DEFAULT NOW()
                    )
                """);
            }
        };
    }
}
