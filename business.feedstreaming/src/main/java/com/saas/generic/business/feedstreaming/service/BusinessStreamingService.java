package com.saas.generic.business.feedstreaming.service;

import com.saas.generic.business.feeds.schemas.IndexData;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
@Component
public class BusinessStreamingService {


    @Value("${feedstreming.applicationId}")
    private String applicationId;

    @Value("${feedstreming.feedDataTopic}")
    private String feedDataTopic;

    @Value("${feedstreming.feedStatsTopic}")
    private String feedStatsTopic;

    public void consumeFromStream()  throws Exception {

        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "feed-stats-stream");
        properties.setProperty("bootstrap.servers", "http://localhost:9092");
        properties.setProperty(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        properties.setProperty("schema.registry.url", "http://localhost:8081");

        properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class.getName());

        StreamsBuilder builder = new StreamsBuilder();

        Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", "http://localhost:8081");
        Serde<String> stringSerde = Serdes.String();
        stringSerde.configure(serdeConfig, true);

        Serde<IndexData> valueSpecificAvroSerde = new SpecificAvroSerde<IndexData>();
        valueSpecificAvroSerde.configure(serdeConfig, false);

        KStream<String, IndexData> mainIndexStream = builder.stream("feed-data", Consumed.with(stringSerde, valueSpecificAvroSerde));

        KGroupedStream<String,IndexData> groupedStream = mainIndexStream.groupByKey();
        KTable<String,Long> indexByCount = mainIndexStream.groupByKey().count();

        indexByCount.toStream().to("feed-stats");


/*        KStream<String, IndexData>[] branches = mainIndexStream.branch( (index, indexData) -> Double.parseDouble(indexData.getOpen().toString()) > 531,
                                (index, indexData) -> index.equalsIgnoreCase("SNG"),
                                (index, indexData) -> Double.parseDouble(indexData.getOpen().toString()) > 531 && index.equalsIgnoreCase("SNG"));


        branches[0].to("low-index-stocks-1", Produced.with(stringSerde, valueSpecificAvroSerde));

        branches[1].to("index-topic-1", Produced.with(stringSerde, valueSpecificAvroSerde));

        branches[2].to("low-index-topic-1", Produced.with(stringSerde, valueSpecificAvroSerde));

        //branches[0].merge(branches[1]).merge(branches[2]).to("merge-index-topic", Produced.with(stringSerde, valueSpecificAvroSerde));

        KTable<String, Long> countByIndex = mainIndexStream.groupByKey().count();
        countByIndex.toStream().to("index-count-1");*/

        // map, groupBy, flatmap, join, leftOuterJoin,

        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, properties);
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

}
