package com.alienworkspace.cdr.demographic.controller;

import static com.alienworkspace.cdr.demographic.helpers.Constants.CONFIG_BASE_URL;

import com.alienworkspace.cdr.demographic.config.AppConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConfigController is a REST controller that handles requests related to metadata.
 * It is responsible for exposing endpoints under the base URL "/api/demographic/configs".
 *
 * <p>
 * This class defines entry points to interact with metadata operations.
 */
@Tag(name = "Metadata", description = "Metadata Operations")
@RestController
@RequestMapping(CONFIG_BASE_URL)
public class ConfigController {

    private final AppConfig appConfig;

    /**
     * Constructor for ConfigController.
     *
     * @param appConfig The application configuration.
     */
    public ConfigController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping()
    public ResponseEntity<AppConfig> getAppConfig() {
        return ResponseEntity.ok(new AppConfig(appConfig.getContactDetails(),
                appConfig.getDescription(), appConfig.getVersion(), appConfig.getWorkDays(), appConfig.getEmail()));
    }
}
