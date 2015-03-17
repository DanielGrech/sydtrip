package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.GraphEdge;

class Edge {
    final int tripId;
    final int departureTime;
    final int arrivalTime;
    final Node origin;
    final Node target;

    final int cost;

    Edge(Node origin, Node target, GraphEdge edge) {
        this.origin = origin;
        this.target = target;
        this.tripId = edge.getTripId();
        this.departureTime = edge.getDepartureTime();
        this.arrivalTime = edge.getArrivalTime();

        this.cost = this.arrivalTime - this.departureTime;
    }
}