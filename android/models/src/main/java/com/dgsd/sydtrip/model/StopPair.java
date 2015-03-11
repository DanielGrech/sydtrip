package com.dgsd.sydtrip.model;

public class StopPair extends BaseModel {

    private final Stop from;
    private final Stop to;

    public StopPair(Stop from, Stop to) {
        this.from = from;
        this.to = to;
    }

    public Stop getFrom() {
        return from;
    }

    public Stop getTo() {
        return to;
    }
}
