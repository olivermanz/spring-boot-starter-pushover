package de.omanz.pushover.spring.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PushoverSound {

    public static final PushoverSound PUSHOVER = new PushoverSound("pushover");
    public static final PushoverSound BIKE = new PushoverSound("bike");
    public static final PushoverSound BUGLE = new PushoverSound("bugle");
    public static final PushoverSound CASH_REGISTER = new PushoverSound("cashregister");
    public static final PushoverSound CLASSICAL = new PushoverSound("classical");
    public static final PushoverSound COSMIC = new PushoverSound("cosmic");
    public static final PushoverSound FALLING = new PushoverSound("falling");
    public static final PushoverSound GAMELAN = new PushoverSound("gamelan");
    public static final PushoverSound INCOMING = new PushoverSound("incoming");
    public static final PushoverSound INTERMISSION = new PushoverSound("intermission");
    public static final PushoverSound MAGIC = new PushoverSound("magic");
    public static final PushoverSound MECHANICAL = new PushoverSound("mechanical");
    public static final PushoverSound PIANO = new PushoverSound("pianobar");
    public static final PushoverSound SIREN = new PushoverSound("siren");
    public static final PushoverSound SPACE_ALARM = new PushoverSound("spacealarm");
    public static final PushoverSound TUG = new PushoverSound("tugboat");
    public static final PushoverSound ALIEN = new PushoverSound("alien");
    public static final PushoverSound CLIMB = new PushoverSound("climb");
    public static final PushoverSound PERSISTENT = new PushoverSound("persistent");
    public static final PushoverSound PUSHOVER_ECHO = new PushoverSound("echo");
    public static final PushoverSound UP_DOWN = new PushoverSound("updown");
    public static final PushoverSound VIBRATE = new PushoverSound("vibrate");
    public static final PushoverSound NONE = new PushoverSound("none");

    public static PushoverSound custom(String id) {
        return new PushoverSound(id);
    }

    @NotEmpty
    private final String id;


}
