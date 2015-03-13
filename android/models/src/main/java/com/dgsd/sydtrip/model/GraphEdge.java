package com.dgsd.sydtrip.model;

public class GraphEdge extends BaseModel {

    private final Stop from;

    private final Stop to;

    private final int cost;

    public GraphEdge(Stop from, Stop to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public Stop getFrom() {
        return from;
    }

    public Stop getTo() {
        return to;
    }

    public int getCost() {
        return cost;
    }
}
