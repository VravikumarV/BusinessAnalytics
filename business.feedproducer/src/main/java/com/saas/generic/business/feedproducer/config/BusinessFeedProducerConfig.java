package com.saas.generic.business.feedproducer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "feedproducer")
@Data
public class BusinessFeedProducerConfig {

    private String feedName;
    private String feedLocation;
    private String feedDataTopic;

}
