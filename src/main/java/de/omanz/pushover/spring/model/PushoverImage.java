package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushoverImage {

    private final byte[] rawContent;
    private final String mediaType;
    private final String imageFileName;

}
