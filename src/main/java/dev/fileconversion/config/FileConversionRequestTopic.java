package dev.fileconversion.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class FileConversionRequestTopic {

    @Bean
    public NewTopic resultTopic() {

        return TopicBuilder.name("file-conversion-requests")
                .partitions(1)
                .replicas(1)
                .build();

    }
}
