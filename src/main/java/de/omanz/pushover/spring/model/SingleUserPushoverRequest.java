package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
public final class SingleUserPushoverRequest {

    @NotEmpty
    private final String userKey;
    private final List<String> devices;

    @NotNull
    @Valid
    private final PushoverMessage message;

}
