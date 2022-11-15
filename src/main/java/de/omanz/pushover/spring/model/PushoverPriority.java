package de.omanz.pushover.spring.model;

/**
 * Priority according to Pushover API documentation:
 * <p />
 * https://pushover.net/api#priority
 * <p />
 * Note: Because of the special process, EMERGENCY is not implemented yet.
 */
public enum PushoverPriority {

    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    EMERGENCY


}
