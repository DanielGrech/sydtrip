package com.dgsd.sydtrip.transformer.routing.graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

class AStarSearch {

    final TransportNetworkGraph graph;
    final Node start;
    final Node goal;
    private final Heuristic heuristic;

    public AStarSearch(TransportNetworkGraph graph, Node start, Node goal) {
        this(graph, start, goal, new Heuristic() {
            @Override
            public int score(Node start, Node goal, Node toAssess) {
                return 0;
            }
        });
    }

    public AStarSearch(TransportNetworkGraph graph, Node start, Node goal, Heuristic heuristic) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        this.heuristic = heuristic;
    }

    public List<List<Node>> search() {
        final HashSet<Node> closedSet = new HashSet();
        final Queue<Node> openSet = new PriorityQueue(11, new Comparator<Node>() {
            @Override
            public int compare(Node lhs, Node rhs) {
                return Integer.compare(
                        heuristic.score(start, goal, lhs),
                        heuristic.score(start, goal, rhs)
                );
            }
        });
        final HashMap<Node, Node> cameFrom = new HashMap<>();

        openSet.add(this.start);

        final HashMap<Node, Score> scores = new HashMap<>();
        scores.put(this.start, new Score(0, heuristic.score(start, goal, start)));

        List<List<Node>> retval = new LinkedList<>();

        while (!openSet.isEmpty()) {
            final Node current = openSet.poll();

            if (current.equals(goal)) {
                retval.add(reconstructPath(cameFrom, current));
                cameFrom.clear();
            }

            closedSet.add(current);
            final Collection<Edge> edges = current.outgoingEdges;
            for (Edge edge : edges) {
                final Node neighbour = edge.target;
                if (closedSet.contains(neighbour)) {
                   continue;
                }

                final int tentativeScore = scores.get(current).gScore + edge.cost;

                if (!openSet.contains(neighbour) || tentativeScore < scores.get(neighbour).gScore) {
                    cameFrom.put(neighbour, current);
                    scores.put(neighbour, new Score(tentativeScore,
                                    tentativeScore + heuristic.score(start, goal, neighbour)));
                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour);
                    }
                }
            }
        }

        return retval;
    }

    private List<Node> reconstructPath(HashMap<Node, Node> cameFrom, Node current) {
        LinkedList<Node> retval = new LinkedList<>();
        retval.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            retval.add(0, current);
        }
        return retval;
    }

    public interface Heuristic {

        public int score(Node start, Node goal, Node toAssess);
    }

    private class Score {
        int gScore;
        int fScore;

        Score(int gScore, int fScore) {
            this.gScore = gScore;
            this.fScore = fScore;
        }

        public int totalScore() {
            return gScore + fScore;
        }
    }
}
