package de.omanz.pushover.spring;

import de.omanz.pushover.client.model.PushoverClientImage;
import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.client.model.PushoverClientResponse;
import de.omanz.pushover.spring.model.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PushoverClientMapper {

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
        builder.group(request.getGroupKey());
        return builder.build();
    }

    private PushoverClientRequest.PushoverClientRequestBuilder mapMessageValues(final PushoverMessage message) {
        return PushoverClientRequest.builder()
                .html(message.getType() == PushoverMessageType.HTML ? 1 : 0)
                .monospace(message.getType() == PushoverMessageType.MONOSPACE ? 1 : 0)
                .sound(message.getSound() != null ? message.getSound().getId() : null)
                .title(message.getMessageTitle())
                .message(message.getMessage())
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

    public PushoverResponse map(final PushoverClientResponse response) {
        List<String> errors = (response.getErrors() != null && !response.getErrors().isEmpty()) ? Collections.unmodifiableList(response.getErrors()) : Collections.emptyList();

        return PushoverResponse.builder()
                .status(response.getStatus() == 1 ? PushoverResponseStatus.SUCCESS : PushoverResponseStatus.ERROR)
                .appLimitRemaining(response.getAppLimitRemaining())
                .appLimitTotal(response.getAppLimitTotal())
                .appLimitReset(response.getAppLimitReset())
                .errors(errors)
                .build();
    }
}
