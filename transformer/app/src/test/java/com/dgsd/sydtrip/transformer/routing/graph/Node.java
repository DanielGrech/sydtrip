package com.dgsd.sydtrip.transformer.routing.graph;

import java.util.HashSet;
import java.util.Set;

class Node {
    final int id;
    final Set<Edge> incomingEdges;
    final Set<Edge> outgoingEdges;

    Node(int id) {
        this.id = id;
        this.incomingEdges = new HashSet<>();
        this.outgoingEdges = new HashSet<>();
    }

    public Set<Edge> getOutgoingEdgesDepartingAfter(int secondsInDay) {
        final Set<Edge> retval = new HashSet<>();
        for (Edge e : outgoingEdges) {
            if (e.departureTime > secondsInDay) {
                retval.add(e);
            }
        }
        return retval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", incomingEdges=" + incomingEdges.size() +
                ", outgoingEdges=" + outgoingEdges.size() +
                '}';
    }
}