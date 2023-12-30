package com.saas.generic.business.feedconsumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Collections;
import java.util.Properties;

@Slf4j
@RestController
@RequestMapping("/v1/index")
public class KafkaConsumerController {

    @Value("${feedconsumer.feedStatsTopic}")
    private String feedStatsTopic;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/consume")
    private ResponseEntity<String> saveProfession() {
        log.info(" feedconsumer.feedStatsTopic:  "+feedStatsTopic);
        StringBuffer buffer = new StringBuffer();
        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers","localhost:9092");
        properties.put("group.id", "customer-consumer-group-v1");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", LongDeserializer.class.getName());
        // avro part (deserializer)
      /*  properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://localhost:8081");
        properties.setProperty("specific.avro.reader", "true");*/

        KafkaConsumer<String, Long> kafkaConsumer = new KafkaConsumer<>(properties);
        //String topic = "index-by-count-topic";
        kafkaConsumer.subscribe(Collections.singleton(feedStatsTopic));

        log.info("Waiting for data...");
        while (true){
            System.out.println("Polling at " + Calendar.getInstance().getTime().toString());
            ConsumerRecords<String, Long> records = kafkaConsumer.poll(1000);

            for (ConsumerRecord<String, Long> record : records){
                log.info(record.key()+" = "+record.value());
            }
            kafkaConsumer.commitSync();
        }
    }

}
