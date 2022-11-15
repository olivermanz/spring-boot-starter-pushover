package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.model.PushoverClientImage;
import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.client.model.PushoverClientResponse;
import de.omanz.pushover.spring.model.*;

import java.util.Collections;
import java.util.List;

/**
 * Mapper to transform between client side api and spring side api.
 * The Pushover API is a little generic - e.g. the user field is used for
 * multiple purposes. The spring sides api tries to abstract from that.
 */
class PushoverClientMapper {

    public PushoverClientRequest map(final SingleUserPushoverRequest request) {
        final PushoverClientRequest.PushoverClientRequestBuilder builder = mapMessageValues(request.getMessage());
        builder.user(request.getUserKey());
        if (request.getDevices() != null && !request.getDevices().isEmpty()) {
            builder.device(String.join(",", request.getDevices()));
        }
        return builder.build();
    }

    public PushoverClientRequest map(final MultiUserPushoverRequest request) {
        final PushoverClientRequest.PushoverClientRequestBuilder builder = mapMessageValues(request.getMessage());
        builder.user(String.join(",", request.getUserKeys()));
        return builder.build();
    }

    public PushoverClientRequest map(final GroupPushoverRequest request) {
        final PushoverClientRequest.PushoverClientRequestBuilder builder = mapMessageValues(request.getMessage());
        builder.user(request.getGroupKey());
        return builder.build();
    }

    private PushoverClientRequest.PushoverClientRequestBuilder mapMessageValues(final PushoverMessage message) {
        return PushoverClientRequest.builder()
                .html(message.getType() == PushoverMessageType.HTML ? 1 : 0)
                .monospace(message.getType() == PushoverMessageType.MONOSPACE ? 1 : 0)
                .sound(message.getSound() != null ? message.getSound().getId() : null)
                .title(message.getMessageTitle())
                .message(message.getMessage())
                .priority(mapPriority(message.getPriority()))
                .image(map(message.getAttachedImage()))
                .time(message.getDisplayedTime() != null ? message.getDisplayedTime().getEpochSecond() : null)
                .url(message.getUrl() != null ? message.getUrl().getUrl() : null)
                .urlTitle(message.getUrl() != null ? message.getUrl().getTitle() : null);
    }


    private PushoverClientImage map(final PushoverImage attachedImage) {
        if (attachedImage == null) {
            return null;
        }

        return PushoverClientImage.builder()
                .rawContent(attachedImage.getRawContent())
                .mediaType(attachedImage.getMediaType())
                .filename(attachedImage.getImageFileName() != null ? attachedImage.getImageFileName() : "image")
                .build();
    }

    public PushoverResponse map(final PushoverClientResponse response, final int priority) {
        List<String> errors = (response.getErrors() != null && !response.getErrors().isEmpty()) ? Collections.unmodifiableList(response.getErrors()) : Collections.emptyList();

        return PushoverResponse.builder()
                .status(response.getStatus() == 1 ? PushoverResponseStatus.SUCCESS : PushoverResponseStatus.ERROR)
                .appLimitRemaining(response.getAppLimitRemaining())
                .appLimitTotal(response.getAppLimitTotal())
                .appLimitReset(response.getAppLimitReset())
                .priority(mapPriority(priority))
                .request(response.getRequest())
                .errors(errors)
                .build();
    }

    private int mapPriority(PushoverPriority priority) {
        switch (priority) {
            case LOWEST:
                return -2;
            case LOW:
                return -1;
            case NORMAL:
                return 0;
            case HIGH:
                return 1;
            case EMERGENCY:
                return 2;
        }
        throw new IllegalArgumentException("Unable to map priority of value " + priority + " to an int value.");
    }

    private PushoverPriority mapPriority(int priority) {
        switch (priority){
            case -2:
                return PushoverPriority.LOWEST;
            case -1:
                return PushoverPriority.LOW;
            case 0:
                return PushoverPriority.NORMAL;
            case 1:
                return PushoverPriority.HIGH;
            case 2:
                return PushoverPriority.EMERGENCY;
        }
        throw new IllegalArgumentException("Unable to map priority of value " + priority + " to a enum value. -2 to 2 are allowed.");
    }
}
