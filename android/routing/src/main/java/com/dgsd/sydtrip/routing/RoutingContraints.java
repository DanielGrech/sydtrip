package com.dgsd.sydtrip.routing;

import jodd.datetime.JDateTime;

public class RoutingContraints {

    private int julianDay;

    private RoutingContraints() {

    }

    public int getJulianDay() {
        return julianDay;
    }

    public static class Builder {
        private final RoutingContraints constraints;

        public Builder() {
            this.constraints = new RoutingContraints();
        }

        public Builder julianDay(int jd) {
            this.constraints.julianDay = jd;
            return this;
        }

        public Builder julianDay(long millis) {
            return julianDay(new JDateTime(millis).getJulianDayNumber());
        }

        public Builder today() {
            return julianDay(System.currentTimeMillis());
        }

        public RoutingContraints build() {
            return this.constraints;
        }
    }
}
