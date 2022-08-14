package de.omanz.pushover.spring.support;

import de.omanz.pushover.spring.model.*;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

public class SpringTestDataGenerator {

    public static SingleUserPushoverRequest createSingleUser(String user, String... devices) {
        return SingleUserPushoverRequest.builder()
                .userKey(user)
                .devices(List.of(devices))
                .message(createMessage())
                .build();
    }

    public static MultiUserPushoverRequest createMultiUser(String... users) {
        return MultiUserPushoverRequest.builder()
                .userKeys(List.of(users))
                .message(createMessage())
                .build();
    }

    public static GroupPushoverRequest createGroup(String group) {
        return GroupPushoverRequest.builder()
                .groupKey(group)
                .message(createMessage())
                .build();
    }

    public static PushoverMessage createMessage() {
        return createMessage(builder -> {
        });
    }

    public static PushoverMessage createMessage(Consumer<PushoverMessage.PushoverMessageBuilder> modifier) {
        final PushoverMessage.PushoverMessageBuilder builder = PushoverMessage.builder()
                .message(RandomString.make(1024))
                .messageTitle(RandomString.make(250))
                .url(PushoverURL.builder()
                        .url(RandomString.make(512))
                        .title(RandomString.make(100))
                        .build())
                .attachedImage(PushoverImage.builder()
                        .imageFileName("image.jpeg")
                        .mediaType(MediaType.IMAGE_PNG_VALUE)
                        .rawContent(new byte[]{0x0, 0xf})
                        .build())
                .sound(PushoverSound.custom("custom.mp3"))
                .displayedTime(Instant.now())
                .type(PushoverMessageType.TEXT)
                .priority(PushoverPriority.NORMAL);

        modifier.accept(builder);
        return builder.build();
    }
}
