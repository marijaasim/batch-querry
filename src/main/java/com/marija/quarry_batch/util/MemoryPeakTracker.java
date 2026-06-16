package com.marija.quarry_batch.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MemoryPeakTracker {

    private final AtomicLong peakHeapUsed = new AtomicLong(0);

    @Scheduled(fixedRate = 100)
    public void track() {
        long current = ManagementFactory.getMemoryMXBean()
                .getHeapMemoryUsage().getUsed();
        peakHeapUsed.updateAndGet(peak -> Math.max(peak, current));
    }

    public long getPeakHeapUsed() {
        return peakHeapUsed.get();
    }

    public void reset() {
        peakHeapUsed.set(0);
    }
}
