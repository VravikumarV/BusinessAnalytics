package com.saas.generic.business.feedproducer.config;

import com.saas.generic.business.feeds.schemas.IndexData;
import com.saas.generic.business.feedproducer.batch.BatchDataItemProcessor;
import com.saas.generic.business.feedproducer.batch.IndexDataFieldMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Value("${feedproducer.feedDataTopic}")
    private String feedDataTopic;

    @Value("${feedproducer.feedLocation}")
    private String feedLocation;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private KafkaTemplate<String, IndexData> kafkaTemplate;

    @Autowired
    private BusinessFeedProducerConfig businessFeedProducerConfig;

    public FlatFileItemReader<IndexData> reader() {
        Resource resource = new PathResource(feedLocation);
        return new FlatFileItemReaderBuilder<IndexData>()
                .name("indexItemReader")
                .resource(resource)
                .delimited()
                .names(new String[] {"index","date","open","high","low","close","adjClose","volume"})
                .lineMapper(lineMapper())
                .fieldSetMapper(new BeanWrapperFieldSetMapper<IndexData>() {{
                    setTargetType(IndexData.class);
                }})
                .build();
    }

    @Bean
    public LineMapper<IndexData> lineMapper() {

        final DefaultLineMapper<IndexData> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");

        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"index","date","open","high","low","close","adjClose","volume"});
        final IndexDataFieldMapper fieldSetMapper = new IndexDataFieldMapper();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public BatchDataItemProcessor processor() {
        return new BatchDataItemProcessor();
    }

    @Bean
    public KafkaItemWriter<String, IndexData> writer() {
        KafkaItemWriter<String, IndexData> kafkaItemWriter = new KafkaItemWriter<String, IndexData>();
        kafkaTemplate.setDefaultTopic(feedDataTopic);
        Map<String,Object> props = new HashMap<String,Object>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,"http://localhost:8081");
        kafkaTemplate.getProducerFactory().updateConfigs(props);
        kafkaItemWriter.setKafkaTemplate(kafkaTemplate);
        kafkaItemWriter.setItemKeyMapper(indexData -> indexData.getIndex().toString());
        return kafkaItemWriter;
    }

    @Bean
    public Job indexDataPublisherJob(Step step1) {
        JobParametersValidator jobParametersValidator = jobParameters -> {};
        JobRepository jobRepository;
        JobExecutionListener jobExecutionListener = new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("JobExecutionListener.beforeJob");
            }
            @Override
            public void afterJob(JobExecution jobExecution) {
                System.out.println("JobExecutionListener.afterJob");
            }
        };

        return jobBuilderFactory.get("indexDataPublisherJob")
                .incrementer(new RunIdIncrementer())
                //.validator(jobParametersValidator)
                //.listener(jobExecutionListener)
                //.repository(jobRepository)
                .preventRestart()       // restartable = false;
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(KafkaItemWriter<String, IndexData> writer) {
        SkipPolicy skipPolicy = (throwable, i) -> {boolean shouldSkip = false; return shouldSkip;};
        return stepBuilderFactory.get("step1")
                .<IndexData, IndexData> chunk(10)
                //.transactionManager()
                //.listener(ItemReadListener)
                //.listener(ItemWriteListener)
                //.listener(ItemProcessListener)
                .reader(reader())
                //.chunkOperations(RepeatTemplate)
                //.exceptionHandler(ExceptionHandler)
                .processor(processor())
                .faultTolerant()
                .retryLimit(3)
                .retry(NullPointerException.class)
                //.retryPolicy(RetryPloicy.class)
                .skipLimit(5)
                .skip(NullPointerException.class)
                //.skipPolicy(skipPolicy)
                .writer(writer)
                .build();
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

}

