package com.saas.generic.business.feedproducer.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import com.saas.generic.business.feeds.schemas.IndexData;

@Slf4j
@Service
public class BatchDataItemProcessor implements ItemProcessor<IndexData, IndexData> {

    @Override
    public IndexData process(IndexData indexData) {
        log.info(indexData.toString());
        return indexData;
    }
}
