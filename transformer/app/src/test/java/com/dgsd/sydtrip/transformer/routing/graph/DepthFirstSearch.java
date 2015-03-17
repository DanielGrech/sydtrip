package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

public class DepthFirstSearch {

    final TransportNetworkGraph graph;
    final Node start;
    final Node goal;
    final Map<Integer, Stop> stopMap;

    public DepthFirstSearch(TransportNetworkGraph graph,
                            Node start, Node goal, Map<Integer, Stop> stopMap) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        this.stopMap = stopMap;
    }

    public List<Node> search() {
        final int startTime = 64800;

        final Stack<Edge> connectionPath = new Stack<>();
        final Map<Edge, Edge> parentMap = new HashMap<>();
        final Set<Node> discovered = new HashSet<>();

        connectionPath.push(new Edge(this.start, startTime));

        do {
            final Edge edge = connectionPath.pop();
            if (this.goal.equals(edge.target)) {
                break;
            }

            if (!discovered.contains(edge.target)) {
                discovered.add(edge.target);

                final List<Edge> possibleEdges
                        = edge.target.getOutgoingEdgesDepartingAfter(edge.arrivalTime);
                Collections.sort(possibleEdges, new Edge.SortByTrip(edge.tripId).reversed());

                for (Edge e : possibleEdges) {
                    connectionPath.push(e);

                    if (!discovered.contains(e.target)) {
                        parentMap.put(e, edge);
                    }
                }
            }
        } while ((!connectionPath.isEmpty()));

        Optional<Edge> first = parentMap.keySet()
                .stream()
                .filter(e -> e.target.equals(this.goal))
                .findFirst();

        if (first.isPresent()) {
            Edge edge = first.get();
            while (edge != null) {
                if (edge.isStartingEdge) {
                    System.out.println(String.format("Board at %s at %s aboard trip %s",
                            stopMap.get(edge.origin.id).getName(), edge.departureTime, edge.tripId));
                } else if (edge.target.equals(this.goal)) {
                    System.out.println(String.format("Alight at %s at %s aboard trip %s",
                            stopMap.get(edge.target.id).getName(), edge.arrivalTime, edge.tripId));
                } else {
                    System.out.println(String.format("%s [%s] -> %s [%s] on trip %s",
                            stopMap.get(edge.origin.id).getName(),
                            edge.departureTime,
                            stopMap.get(edge.target.id).getName(),
                            edge.arrivalTime,
                            edge.tripId));
                }

                edge = parentMap.get(edge);
            }
        } else {
            System.out.println("NO PATH FOUND!");
        }

        return null;
    }
}
