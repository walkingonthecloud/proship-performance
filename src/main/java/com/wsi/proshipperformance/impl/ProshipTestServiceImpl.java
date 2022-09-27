package com.wsi.proshipperformance.impl;

import com.wsi.proshipperformance.api.ProshipTestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ProshipTestServiceImpl implements ProshipTestService, CommandLineRunner {

    private final RestTemplate restTemplate;
    private final DataRandomizerServiceImpl dataRandomizerService;

    private List<Long> averageResponseTimes = new ArrayList<>();

    private final List<String> fieldsToRandomize = Arrays.asList("CCN_CARTON_NUMBER", "CCN_PICKTKT_CTRL", "CCN_ORDER_NUMBER");

    private Integer numberOfCalls;

    @Value("${proship.ship-request.uri}")
    private String proshipUri;

    @Autowired
    public ProshipTestServiceImpl(RestTemplate restTemplate, DataRandomizerServiceImpl dataRandomizerService) {
        this.restTemplate = restTemplate;
        this.dataRandomizerService = dataRandomizerService;
    }

    @Override
    public String shipRequest() throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        String xmlString = IOUtils.toString(Objects.requireNonNull(classLoader.getResourceAsStream("shiprequest.xml")));
        String result=null;
        for (int i = 0; i < numberOfCalls; i++) {
            //String shipRequestXml = dataRandomizerService.randomizeElementData(xmlString, fieldsToRandomize);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            HttpEntity<String> requestEntity = new HttpEntity<>(xmlString, headers);

            Instant before = Instant.now();
            ResponseEntity<String> response = restTemplate.exchange(proshipUri, HttpMethod.POST, requestEntity, String.class);
            Instant after = Instant.now();
            Duration duration = Duration.between(before, after);
            long millis = duration.toMillis();
            log.info("Proship call took {} milli-seconds", millis);
            averageResponseTimes.add(millis);

            result = response.getBody();
            log.info("Response received is: {}", result);
        }

        log.info("Iterations complete.");
        return result;
    }

    @Override
    public void run(String... args) throws Exception {
        numberOfCalls = args.length > 0 ? Integer.parseInt(args[0]) : 10;
        log.info("Starting proship tests for {} iterations", numberOfCalls);
        shipRequest();
        log.info("Response times of calls made:");
        log.info("-----------------------------");
        int i = 1;
        long total = 0;
        for (long millis: averageResponseTimes)
        {
            total = total + millis;
            log.info("Run {} response time in milli-second is: {}", i, millis);
            i++;
        }
        log.info("-----------------------------");
        log.info("Average response time was: {}", total/i);
    }
}
