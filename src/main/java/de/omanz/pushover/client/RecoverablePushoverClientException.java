package de.omanz.pushover.client;

import org.springframework.http.HttpStatus;

import java.io.IOException;

public class RecoverablePushoverClientException extends IOException {
    public RecoverablePushoverClientException(HttpStatus statusCode, String statusText) {
        super("Error during request to pushover api. Received status code " + statusCode + " / " + statusText);
    }
}
