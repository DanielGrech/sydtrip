package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class DepthFirstSearch2 {

    final TransportNetworkGraph graph;
    final Node start;
    final Node goal;
    final Map<Integer, Stop> stopMap;

    public DepthFirstSearch2(TransportNetworkGraph graph,
                             Node start, Node goal, Map<Integer, Stop> stopMap) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        this.stopMap = stopMap;
    }

    public List<List<Node>> search() {

        final List<List<Node>> nodeLists = new LinkedList<>();

        final Stack<Node> stack = new Stack<>();
        stack.add(start);

        Set<Node> visited = new HashSet<>();
        visited.add(start);

        while (!stack.isEmpty()) {
            final Node node = stack.pop();

            final Collection<Node> neighbours = node.getOutgoingNodes();
            for (Node n : neighbours) {
                if (n.equals(this.goal)) {
                    visited.clear();

                    n.parent = node;
                    List<Node> list = buildPath(n);

                    nodeLists.add(list);

                    list.forEach(o -> {
                        System.out.println(stopMap.get(o.id).getName());
                    });
                } else if (!visited.contains(n)) {
                    n.parent = node;
                    stack.add(n);
                    visited.add(n);
                }
            }
        }

//        nodeLists.forEach(list -> {
//            list.forEach(n -> {
//                System.out.println(stopMap.get(n.id).getName());
//            });
//            System.out.println("----------");
//        });

        return nodeLists;
    }

    private List<Node> buildPath(Node node) {
        final List<Node> path = new LinkedList<>();

        Node n = node;
        while (n != null) {
            path.add(n);
            n = n.parent;
        }
        return path;
    }
}
