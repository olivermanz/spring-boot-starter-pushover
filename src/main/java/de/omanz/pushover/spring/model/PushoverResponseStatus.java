package de.omanz.pushover.spring.model;

/**
 * Will be mapped according to https://pushover.net/api#response
 * with a return code of 1 leading to SUCCESS and all other codes leading to ERROR.
 */
public enum PushoverResponseStatus {
    SUCCESS, ERROR
}
