package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Raptor {

    final TransportNetworkGraph graph;
    final Node start;
    final Node goal;
    final Map<Integer, Stop> stopMap;

    public Raptor(TransportNetworkGraph graph,
                  Node start, Node goal, Map<Integer, Stop> stopMap) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        this.stopMap = stopMap;
    }

    public List<List<Node>> search() {
        final List<List<Node>> nodeLists = new LinkedList<>();

        Map<Node, Integer> nodesUpdated = new HashMap<>();
        nodesUpdated.put(this.start, 0);

        for (int k = 1; k < 3; k++) {
            Set<Integer> tripIds = nodesUpdated.keySet().stream()
                    .flatMap(n -> n.outgoingEdges.stream())
                    .map(e -> e.tripId)
                    .collect(toSet());


        }

        return nodeLists;
    }
}
