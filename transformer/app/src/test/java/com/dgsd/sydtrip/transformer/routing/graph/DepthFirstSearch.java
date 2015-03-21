package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
        final Map<Edge, Edge> pathMap = new HashMap<>();
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
                Collections.sort(possibleEdges, new Edge.SortByTrip(edge.tripId));

                for (Edge e : possibleEdges) {
                    connectionPath.push(e);

                    if (!discovered.contains(e.target)) {
                        pathMap.put(e, edge);
                    }
                }
            }
        } while ((!connectionPath.isEmpty()));

        final List<Edge> goals = pathMap.keySet()
                .stream()
                .filter(e -> e.target.equals(this.goal))
                .collect(toList());

        if (goals.isEmpty()) {
            System.out.println("NO PATH FOUND!");
        } else {
            final List<List<Edge>> results = new LinkedList<>();
            for (Edge edge : goals) {
                final List<Edge> edges = new LinkedList<>();
                while (edge != null) {
                    edges.add(edge);
                    edge = pathMap.get(edge);
                }

                Collections.reverse(edges);
                results.add(edges);
            }

            List<List<Edge>> singleTripResults = results.stream()
                    .filter(list -> list.stream()
                                    .filter(e -> !e.isStartingEdge)
                                    .mapToInt(e -> e.tripId)
                                    .distinct()
                                    .count() == 1
                    ).collect(Collectors.toList());

            for (List<Edge> result : singleTripResults) {
                result.forEach(System.out::println);
                System.out.println("\n\n");
            }
        }

        return null;
    }
}
