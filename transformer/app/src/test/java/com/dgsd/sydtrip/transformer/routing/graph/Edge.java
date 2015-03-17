package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.GraphEdge;

import java.util.Comparator;

class Edge {
    final int tripId;
    final int departureTime;
    final int arrivalTime;
    final Node origin;
    final Node target;

    final int cost;
    final boolean isStartingEdge;

    Edge(Node node, int departureTime) {
        this(node, node, -1, departureTime, departureTime);
    }

    Edge(Node origin, Node target, GraphEdge edge) {
        this(origin, target, edge.getTripId(), edge.getDepartureTime(), edge.getArrivalTime());
    }

    Edge(Node origin, Node target, int tripId, int departureTime, int arrivalTime) {
        this.origin = origin;
        this.target = target;
        this.tripId = tripId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;

        this.cost = this.arrivalTime - this.departureTime;

        this.isStartingEdge = origin.equals(target) && tripId < 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (arrivalTime != edge.arrivalTime) return false;
        if (cost != edge.cost) return false;
        if (departureTime != edge.departureTime) return false;
        if (tripId != edge.tripId) return false;
        if (!origin.equals(edge.origin)) return false;
        if (!target.equals(edge.target)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tripId;
        result = 31 * result + departureTime;
        result = 31 * result + arrivalTime;
        result = 31 * result + origin.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + cost;
        return result;
    }

    public static class SortByTrip implements Comparator<Edge> {

        final int targetTripId;

        public SortByTrip(int targetTripId) {
            this.targetTripId = targetTripId;
        }

        @Override
        public int compare(Edge lhs, Edge rhs) {
            if (lhs.tripId == targetTripId && rhs.tripId != targetTripId) {
                return -1;
            } else if (rhs.tripId == targetTripId && lhs.tripId != targetTripId) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    static Comparator<Edge> SORT_BY_EARLIEST_DEPARTURE = new Comparator<Edge>() {
        @Override
        public int compare(Edge lhs, Edge rhs) {
            return Integer.compare(rhs.departureTime, lhs.departureTime);
        }
    };
}