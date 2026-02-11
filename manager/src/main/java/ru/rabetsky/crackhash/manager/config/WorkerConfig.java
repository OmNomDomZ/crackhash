package ru.rabetsky.crackhash.manager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "worker")
@Data
public class WorkerConfig {
    private List<String> urls;
    private int count;
}
