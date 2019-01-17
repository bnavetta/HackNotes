package org.hackatbrown.hacknotes.data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import androidx.room.TypeConverter;

public class Converters {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("UTC"));

    @TypeConverter
    public static String toOffsetDateTime(Instant value)
    {
        if (value == null)
        {
            return null;
        }
        return FORMATTER.format(value);
    }

    @TypeConverter
    public static Instant fromOffsetDateTime(String value)
    {
        if (value == null)
        {
            return null;
        }

        return FORMATTER.parse(value, Instant::from);
    }
}
