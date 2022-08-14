package de.omanz.pushover.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
/**
 * Error handler for Pushover API according to documentation.
 *
 * @see https://pushover.net/api#friendly
 *
 * Return of 400 means the
 */
public class PushoverErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is4xxClientError()
                && !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new RecoverablePushoverClientException(response.getStatusCode(), response.getStatusText());
    }
}
