package de.omanz.pushover.spring.config;

import de.omanz.pushover.client.PushoverApplicationConfig;
import de.omanz.pushover.client.PushoverClient;
import de.omanz.pushover.client.PushoverErrorHandler;
import de.omanz.pushover.spring.PushoverClientMapper;
import de.omanz.pushover.spring.service.PushoverService;
import de.omanz.pushover.spring.service.PushoverServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Configuration
@Setter
@ConfigurationProperties(prefix = "spring.pushover")
@ConditionalOnClass(PushoverService.class)
public class PushoverConfig {

    private String schema;
    private String host;
    private String port;
    private String messagepost;
    private String token;

    @Bean
    public PushoverServiceImpl pushoverService(RestTemplate restTemplate,
                                               PushoverClientMapper pushoverClientMapper) {
        PushoverClient client = new PushoverClient(restTemplate, createConfig());
        return new PushoverServiceImpl(client, pushoverClientMapper);
    }

    private PushoverApplicationConfig createConfig() {
        String urlbase = schema + "://" + host + ":" + port + "/";

        return PushoverApplicationConfig.builder()
                .pushoverPostUrl(urlbase + messagepost)
                .applicationKey(token)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    @Qualifier("pushover")
    RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new PushoverErrorHandler())
                .build();
    }

}
