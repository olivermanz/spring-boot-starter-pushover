package de.omanz.pushover.client.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
public class PushoverClientRequest {

    @NotEmpty
    private final String user;
    private final String device;
    private final String group;

    private final String message;
    private final String title;
    private final String url;
    private final String urlTitle;
    private final Long time;
    private final String sound;
    private final int priority;
    private final int html;
    private final int monospace;
    private final PushoverClientImage image;

}
