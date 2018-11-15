package io.alpere.common.crudfop.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

@UtilityClass
public class Constants {
    public static final ZonedDateTime NOW = Instant.now().atZone(UTC);

    public static final UUID ID = UUID.randomUUID();

    public static final String NULL = "NULL";
}
