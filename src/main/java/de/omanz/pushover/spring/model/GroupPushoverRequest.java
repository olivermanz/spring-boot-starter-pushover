package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public final class GroupPushoverRequest {

    @NotEmpty
    private final String groupKey;
    @NotNull
    @Valid
    private final PushoverMessage message;

}
