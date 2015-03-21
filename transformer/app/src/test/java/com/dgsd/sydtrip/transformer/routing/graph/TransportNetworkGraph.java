package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.GraphEdge;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportNetworkGraph {

    private final Map<Integer, Node> nodes;

    public TransportNetworkGraph() {
        this.nodes = new HashMap<>();
    }

    public void addEdges(Iterable<GraphEdge> edges) {
        for (GraphEdge edge : edges) {
            addEdge(edge);
        }
    }

    public void addEdge(GraphEdge graphEdge) {
        final Node fromNode = addNode(graphEdge.getFrom());
        final Node toNode = addNode(graphEdge.getTo());

        final Edge edge = new Edge(fromNode, toNode, graphEdge);

        fromNode.outgoingEdges.add(edge);
        toNode.incomingEdges.add(edge);
    }

    public List<List<Node>> findPath(int startNode, int endNode, Map<Integer, Stop> stopMap) {
        final Node start = nodes.get(startNode);
        final Node end = nodes.get(endNode);

//        return new DepthFirstSearch(this, start, end, stopMap).search();
        return new DepthFirstSearch2(this, start, end, stopMap).search();
    }

    public Node addNode(int nodeId) {
        final Node existingNode = nodes.get(nodeId);

        if (existingNode == null) {
            final Node node = new Node(nodeId);
            nodes.put(nodeId, node);
            return node;
        } else {
            return existingNode;
        }
    }
}
