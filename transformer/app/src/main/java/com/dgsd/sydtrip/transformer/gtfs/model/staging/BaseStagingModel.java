package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.JulianFields;
import java.util.concurrent.TimeUnit;

public abstract class BaseStagingModel<T extends BaseGtfsModel> {

    private final T gtfsObject;

    public BaseStagingModel(T gtfsObject) {
        this.gtfsObject = gtfsObject;
    }

    protected BaseGtfsModel getGtfsObject() {
        return gtfsObject;
    }

    protected static int parseDateToJulianDay(String input) {
        try {
            final LocalDate date = LocalDate.parse(input, DateTimeFormatter.BASIC_ISO_DATE);
            return (int) date.getLong(JulianFields.JULIAN_DAY);
        } catch (Exception e) {
            return -1;
        }
    }

    protected static int parseTimeToSecondsOfDay(String input) {
        try {
            if (input != null && input.length() > 2) {
                final int hour = safeParseInt(input.substring(0, 2), -1);
                if (hour > LocalTime.MAX.getHour()) {
                    final int newHour = hour - LocalTime.MAX.getHour() - 1;
                    input = String.format("%02d", newHour) + input.substring(2);
                }
            }
            return LocalTime.parse(input, DateTimeFormatter.ISO_LOCAL_TIME).toSecondOfDay();
        } catch (Exception e) {
            return -1;
        }
    }

    protected static int safeParseInt(String input, int defaultValue) {
        return safeParseInt(input, 10, defaultValue);
    }

    protected static int safeParseInt(String input, int base, int defaultValue) {
        if (input == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(input, base);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    protected static float safeParseFloat(String input, float defaultValue) {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException | NullPointerException ex) {
            return defaultValue;
        }
    }
}
