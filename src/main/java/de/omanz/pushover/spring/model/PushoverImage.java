package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;

@Builder
@Getter
/**
 * Image to be sent with the message. Please note that the documentation refers to this
 * as attachments - but images being the only attachments supported right now.
 * <p />
 * https://pushover.net/api#attachments
 */
public class PushoverImage {

    @Size(max = 2621440)
    private final byte[] rawContent;
    private final String mediaType;
    private final String imageFileName;

}
