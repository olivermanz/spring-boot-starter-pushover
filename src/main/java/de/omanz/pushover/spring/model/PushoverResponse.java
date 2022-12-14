package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
public class PushoverResponse {
    /**
     * Is the process was successful or not. See error list if status = ERROR.
     */
    private final PushoverResponseStatus status;
    /**
     * List of errors returned from the Pushover API
     */
    private final List<String> errors;
    /**
     * The total amount of message that you can issue within the given period (normally calendar month).
     */
    private final Integer appLimitTotal;
    /**
     * The amount of messages remaining for the current period.
     */
    private final Integer appLimitRemaining;
    /**
     * The instant in time the amount will be reset to appLimitTotal.
     */
    private final Instant appLimitReset;

}
