package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import org.junit.Test;

import static com.dgsd.sydtrip.transformer.gtfs.model.staging.BaseStagingModel.parseDateToJulianDay;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseStagingModelTest {

    @Test
    public void should_return_max_value_for_null_input_parse_date_to_julian_day() {
        assertThat(parseDateToJulianDay(null)).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void should_return_max_value_for_empty_input_parse_date_to_julian_day() {
        assertThat(parseDateToJulianDay("")).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void should_return_max_value_for_invalid_input_parse_date_to_julian_day() {
        assertThat(parseDateToJulianDay("this is rubbish")).isEqualTo(Integer.MAX_VALUE);
        assertThat(parseDateToJulianDay("15-09-2014")).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    public void should_parse_julian_day() {
        assertThat(parseDateToJulianDay("20140915")).isEqualTo(2456916);
    }

    @Test
    public void should_parse_time_to_millis() {
        BaseStagingModel.parseTimeToSecondsOfDay("10:10:10");
    }
}
