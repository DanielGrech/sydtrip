package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Dijkstra {

    final TransportNetworkGraph graph;
    final Node start;
    final Node goal;
    final Map<Integer, Stop> stopMap;

    public Dijkstra(TransportNetworkGraph graph,
                    Node start, Node goal, Map<Integer, Stop> stopMap) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        this.stopMap = stopMap;
    }

    public List<List<Node>> search() {
        final List<List<Node>> nodeLists = new LinkedList<>();

        return nodeLists;
    }
}
