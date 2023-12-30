package com.saas.generic.business.feedproducer.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import com.saas.generic.business.feeds.schemas.IndexData;

@Component
public class IndexDataFieldMapper implements FieldSetMapper<IndexData> {

    @Override
    public IndexData mapFieldSet(FieldSet fieldSet) {
        final IndexData indexData = new IndexData();

        indexData.setIndex(fieldSet.readString("index"));
        indexData.setDate(fieldSet.readString("date"));
        indexData.setOpen(fieldSet.readString("open"));
        indexData.setHigh(fieldSet.readString("high"));
        indexData.setLow(fieldSet.readString("low"));
        indexData.setClose(fieldSet.readString("close"));
        indexData.setAdjClose(fieldSet.readString("adjClose"));
        indexData.setVolume(fieldSet.readString("volume"));
        return indexData;

    }
}
