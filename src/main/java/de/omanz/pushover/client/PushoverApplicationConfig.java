package de.omanz.pushover.client;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushoverApplicationConfig {

    private final String applicationKey;
    private final String pushoverPostUrl;
}
