package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

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
