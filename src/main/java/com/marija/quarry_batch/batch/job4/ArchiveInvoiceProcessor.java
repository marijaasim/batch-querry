package com.marija.quarry_batch.batch.job4;

import org.springframework.batch.item.ItemProcessor;

public class ArchiveInvoiceProcessor implements ItemProcessor<ArchiveInvoice, ArchiveInvoice> {

    @Override
    public ArchiveInvoice process(ArchiveInvoice item) {
        return item;
    }
}