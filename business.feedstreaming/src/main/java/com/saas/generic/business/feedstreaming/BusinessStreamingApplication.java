package com.saas.generic.business.feedstreaming;

import com.saas.generic.business.feedstreaming.service.BusinessStreamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@SpringBootApplication
public class BusinessStreamingApplication {

    public static void main(String[] args) throws Exception {
        log.info("Streaming Application ...");
        BusinessStreamingService businessStreamingService = new BusinessStreamingService();
        businessStreamingService.consumeFromStream();

    }

}
