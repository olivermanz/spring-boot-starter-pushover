package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
public final class MultiUserPushoverRequest {

    @Size(min = 1, max = 50)
    @Builder.Default
    private final List<String> userKeys = Collections.emptyList();

    @NotNull
    @Valid
    private final PushoverMessage message;

}
