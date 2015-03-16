package com.dgsd.sydtrip.transformer;

public class GraphEdge {
    private final int from;
    private final int to;
    private final int tripId;
    private final int departureTime;
    private final int arrivalTime;

    public GraphEdge(
            int from,
            int to,
            int tripId,
            int departureTime,
            int arrivalTime) {
        this.from = from;
        this.to = to;
        this.tripId = tripId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getTripId() {
        return tripId;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphEdge graphEdge = (GraphEdge) o;

        if (from != graphEdge.from) return false;
        if (to != graphEdge.to) return false;
        if (tripId != graphEdge.tripId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        result = 31 * result + tripId;
        return result;
    }
}
