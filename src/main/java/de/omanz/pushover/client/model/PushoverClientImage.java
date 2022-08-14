package de.omanz.pushover.client.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class PushoverClientImage {

    private final String filename;
    @NotEmpty
    private final String mediaType;
    @NotNull
    private final byte[] rawContent;

}
