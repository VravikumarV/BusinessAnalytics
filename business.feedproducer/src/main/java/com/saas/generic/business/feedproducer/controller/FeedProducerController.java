package com.saas.generic.business.feedproducer.controller;

import com.saas.generic.business.feedproducer.config.SpringBatchConfig;
import com.saas.generic.business.feeds.schemas.IndexData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/analytics/")
public class FeedProducerController {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    SpringBatchConfig springBatchConfig;

    @Autowired
    Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/publishFeedFile")
    public String helloIndex() throws Exception {
        log.info(" Going to piublish feed to Kafkka ... ");
        //jobLauncher.run(springBatchConfig.indexDataPublisherJob(), new JobParameters());
        return "Hello helloFromFeed;  !";
    }


    @GetMapping("/feedIndex")
    public String feedIndex(@RequestBody String indexFeed){
        log.info(" In feedIndex mapping ... ");
        kafkaTemplate.send("index-feed",indexFeed.toString());

        KStream<String, IndexData> streamByIndex;

        return "Hello helloFromFeed!";
    }

}
