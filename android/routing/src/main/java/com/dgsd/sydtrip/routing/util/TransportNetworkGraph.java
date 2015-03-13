package com.dgsd.sydtrip.routing.util;

import com.dgsd.sydtrip.model.GraphEdge;
import com.dgsd.sydtrip.model.Stop;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jodd.util.collection.SortedArrayList;

public class TransportNetworkGraph {

    final HashMap<Stop, Node> nodes;

    public TransportNetworkGraph() {
        this(null);
    }

    public TransportNetworkGraph(List<GraphEdge> edges) {
        nodes = new HashMap<>();

        if (edges != null) {
            for (GraphEdge edge : edges) {
                addEdge(edge);
            }
        }
    }

    private Node addNode(Stop stop) {
        Node node = nodes.get(stop);
        if (node == null) {
            node = new Node(stop);
            nodes.put(stop, node);
        }

        return node;
    }

    public void addEdge(GraphEdge edge) {
        addEdge(edge.getFrom(), edge.getTo(), edge.getCost());
    }

    public void addEdge(Stop fromNode, Stop toNode, int cost) {
        final Node from = addNode(fromNode);
        final Node to = addNode(toNode);

        from.edges.add(new Edge(to, cost));
    }

    public List<GraphEdge> findBestPath(Stop from, Stop to) {
        System.out.println("BEST PATH: " + new AStarSearch(this, from, to).search().toString());
        return null;
    }

    class Node {
        final List<Edge> edges;
        final Stop stop;

        private Node(Stop stop) {
            this.stop = stop;
            this.edges = new SortedArrayList<>(new Comparator<Edge>() {
                @Override
                public int compare(Edge lhs, Edge rhs) {
                    return Integer.compare(lhs.cost, rhs.cost);
                }
            });
        }

        public boolean isInterchange() {
            Set<Stop> stops = new HashSet<>();
            for (Edge e : edges) {
                stops.add(e.target.stop);
            }
            return stops.size() > 2; // 1 station before, 1 after
        }

        @Override
        public boolean equals(Object other) {
            return this.stop.equals(((Node) other).stop);
        }

        @Override
        public int hashCode() {
            return stop.getId();
        }
    }

    class Edge {
        final Node target;
        final int cost;

        private Edge(Node target, int cost) {
            this.target = target;
            this.cost = cost;
        }
    }
}
