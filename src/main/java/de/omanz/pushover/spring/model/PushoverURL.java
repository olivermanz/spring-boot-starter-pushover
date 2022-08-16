package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Supplementary URL
 *  <p/>
 * https://pushover.net/api#urls
 */
@Builder
@Getter
public class PushoverURL {

    @NotEmpty
    @Size(max = 512)
    private final String url;

    @Size(max = 100)
    private final String title;

}
