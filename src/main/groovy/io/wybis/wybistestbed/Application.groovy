package io.wybis.wybistestbed

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.xml.internal.ws.developer.SerializationFeature
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import io.wybis.wybistestbed.service.SessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.client.RestTemplate

import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.sql.DataSource

@SpringBootApplication
@Slf4j
class Application implements CommandLineRunner {

    static void main(String[] args) {
        SpringApplication.run(Application, args)
    }

    @Value('${service-id}')
    String serviceId

    @Resource(name = 'eventandgifts')
    SessionService eventAndGiftsService

    @Resource(name = 'wys')
    SessionService wysService

    @Resource(name = 'harmoney')
    SessionService harmoneyService

    SessionService sessionService = null

    @PostConstruct
    void init() {
        if (serviceId == 'eventandgifts') {
            sessionService = eventAndGiftsService
        } else if (serviceId == 'wys') {
            sessionService = wysService
        } else if (serviceId == 'harmoney') {
            sessionService = harmoneyService
        }
    }

    @Override
    void run(String... args) {
        log.info 'started...'

        try {
            if(sessionService.ping()) {
                if(sessionService.login()) {
                    try {
                        sessionService.execute()
                    }
                    catch (Throwable t) {
                        log.error('Execution failed...', t)
                    }
                }
                sessionService.logout()
            }
        }
        catch (Throwable t) {
            log.error('Execution failed...', t)
        }

        log.info 'finished...'
    }

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();

        return dataSource;
    }

    @Bean
    JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource());

        return jdbcTemplate;
    }

    @Bean
    Sql sqlTemplate() {
        Sql sql = new Sql(dataSource())

        return sql
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate()

        return restTemplate
    }

    @Bean
    public ObjectMapper jsonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper;
    }
}
