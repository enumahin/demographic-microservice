package com.alienworkspace.cdr.demographic.config;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;


/**
 * Configuration class for application properties.
 */
@Setter
@Getter
@RefreshScope
@AllArgsConstructor
@ConfigurationProperties(prefix = "cdr-application")
public class AppConfig {
    private Map<String, String> contactDetails;
    private String description;
    private String version;
    private List<String> workDays;
    private String email;
}
