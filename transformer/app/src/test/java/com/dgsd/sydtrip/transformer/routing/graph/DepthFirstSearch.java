package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        final Stack<Node> connectionPath = new Stack<>();
        connectionPath.push(this.start);

        final Map<Node, Node> parentMap = new HashMap<>();
        final Set<Node> discovered = new HashSet<>();
        do {
            final Node node = connectionPath.pop();
            if (this.goal.equals(node)) {
                break;
            }

            if (!discovered.contains(node)) {
                discovered.add(node);

                for (Edge edge : node.outgoingEdges) {
                    connectionPath.push(edge.target);

                    if (!discovered.contains(edge.target)) {
                        parentMap.put(edge.target, node);
                    }
                }
            }
        } while ((!connectionPath.isEmpty()));

        Node node = this.goal;
        while (node != null) {
            System.out.println(stopMap.get(node.id).getName());
            node = parentMap.get(node);
        }

        return null;
    }
}
