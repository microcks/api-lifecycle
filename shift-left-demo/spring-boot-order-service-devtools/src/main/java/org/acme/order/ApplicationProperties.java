package org.acme.order;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "application")
@Validated
public record ApplicationProperties(@NotEmpty String pastriesBaseUrl,
                                    @NotEmpty String orderEventsCreatedTopic,
                                    @NotEmpty String orderEventsReviewedTopic) {}
