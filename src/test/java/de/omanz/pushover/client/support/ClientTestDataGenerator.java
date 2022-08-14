package de.omanz.pushover.client.support;

import de.omanz.pushover.client.model.PushoverClientImage;
import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.client.model.PushoverClientResponse;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ClientTestDataGenerator {

    public static PushoverClientResponse createResponse(int status, String... errors) {
        return PushoverClientResponse.builder()
                .request(UUID.randomUUID().toString())
                .errors(List.of(errors))
                .status(status)
                .httpStatusCode(status == 1 ? 200 : 400)
                .appLimitReset(Instant.now())
                .appLimitTotal(10_000)
                .appLimitRemaining(1_000)
                .build();
    }

    public static PushoverClientRequest createMessage() {
        return PushoverClientRequest.builder()
                .message(RandomString.make(1024))
                .title(RandomString.make(250))
                .time(Instant.now().getEpochSecond())
                .url("http://localhost/something")
                .urlTitle("Something cool")
                .image(PushoverClientImage.builder()
                        .filename("image.jpeg")
                        .mediaType(MediaType.IMAGE_PNG_VALUE)
                        .rawContent(new byte[]{0x0, 0xf})
                        .build())
                .sound("mySound")
                .html(0)
                .monospace(0)
                .user("user")
                .priority(0)
                .build();
    }
}
