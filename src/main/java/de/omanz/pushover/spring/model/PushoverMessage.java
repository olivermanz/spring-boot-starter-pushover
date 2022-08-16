package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;


/**
 * The message of payload. That part that is independent of the recipients.
 * Only the field message is mandatory. There are three types of messages you can send:
 * <ul>
 *     <li>Text (plain) messages.</li>
 *     <li>Monospace font messages.</li>
 *     <li>HTML messages with a reduced HTML syntax. Refer to https://pushover.net/api#html for details.</li>
 * </ul>
 * Choose by selection a PushoverMessageType.
 */
@Builder
@Getter
public final class PushoverMessage {

    @NotNull
    @NotEmpty
    @Size(max = 1024)
    private final String message;
    @Size(max = 250)
    private final String messageTitle;
    @Valid
    private final PushoverURL url;
    private final Instant displayedTime;
    @Valid
    private final PushoverSound sound;
    @Builder.Default
    private final PushoverPriority priority = PushoverPriority.NORMAL;
    @Builder.Default
    private final PushoverMessageType type = PushoverMessageType.TEXT;
    @Valid
    private final PushoverImage attachedImage;

}
