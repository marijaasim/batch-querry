package com.marija.quarry_batch.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class ManualBatchExecutionRepository {

    private final JdbcTemplate jdbcTemplate;

    public ManualBatchExecutionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insertExecution(String jobName, Timestamp startTime) {
        return jdbcTemplate.queryForObject("""
            INSERT INTO manual_batch_execution 
            (job_name, start_time, read_count, write_count, filter_count, commit_count, status)
            VALUES (?, ?, 0, 0, 0, 0, 'STARTED')
            RETURNING id
            """,
                Long.class,
                jobName, startTime
        );
    }

    public void insertContext(Long executionId) {
        jdbcTemplate.update("""
            INSERT INTO manual_batch_execution_context (id, execution_id, last_chunk_index)
            VALUES (?, ?, 0)
            """,
                executionId, executionId
        );
    }

    public void updateChunk(Long executionId, int readCount, int writeCount,
                            int filterCount, int commitCount) {
        jdbcTemplate.update("""
            UPDATE manual_batch_execution
            SET read_count = ?, write_count = ?, filter_count = ?, commit_count = ?
            WHERE id = ?
            """,
                readCount, writeCount, filterCount, commitCount, executionId
        );
        jdbcTemplate.update("""
            UPDATE manual_batch_execution_context
            SET last_chunk_index = ?
            WHERE execution_id = ?
            """,
                commitCount, executionId
        );
    }

    public void updateFinal(Long executionId, Timestamp endTime,
                            long executionTimeMs, int readCount, int writeCount,
                            int filterCount, int commitCount) {
        jdbcTemplate.update("""
            UPDATE manual_batch_execution
            SET end_time = ?, execution_time_ms = ?, read_count = ?,
                write_count = ?, filter_count = ?, commit_count = ?, status = 'COMPLETED'
            WHERE id = ?
            """,
                endTime, executionTimeMs, readCount, writeCount,
                filterCount, commitCount, executionId
        );
        jdbcTemplate.update("""
            UPDATE manual_batch_execution_context
            SET last_chunk_index = ?
            WHERE execution_id = ?
            """,
                commitCount, executionId
        );
    }
}
