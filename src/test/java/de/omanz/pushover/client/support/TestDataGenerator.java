package de.omanz.pushover.client.support;

import de.omanz.pushover.client.model.PushoverClientImage;
import de.omanz.pushover.client.model.PushoverClientRequest;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.http.MediaType;

import java.time.Instant;

public class TestDataGenerator {


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
