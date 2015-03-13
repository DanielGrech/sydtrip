package com.dgsd.sydtrip.routing.util;

import com.dgsd.sydtrip.model.Stop;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStarSearch {

    final TransportNetworkGraph graph;
    final Stop start;
    final Stop goal;
    private final Heuristic heuristic;

    public AStarSearch(TransportNetworkGraph graph, Stop start, Stop goal) {
        this(graph, start, goal, new Heuristic() {
            @Override
            public int score(Stop start, Stop goal, Stop toAssess) {
                return 0;
            }
        });
    }

    public AStarSearch(TransportNetworkGraph graph, Stop start, Stop goal, Heuristic heuristic) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        this.heuristic = heuristic;
    }

    public List<Stop> search() {
        final HashSet<Stop> closedSet = new HashSet();
        final Queue<Stop> openSet = new PriorityQueue(11, new Comparator<Stop>() {
            @Override
            public int compare(Stop lhs, Stop rhs) {
                return Integer.compare(
                        heuristic.score(start, goal, lhs),
                        heuristic.score(start, goal, rhs)
                );
            }
        });
        final HashMap<Stop, Stop> cameFrom = new HashMap<>();

        openSet.add(this.start);

        final HashMap<Stop, Score> scores = new HashMap<>();
        scores.put(this.start, new Score(0, heuristic.score(start, goal, start)));

        while (!openSet.isEmpty()) {
            final Stop current = openSet.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            closedSet.add(current);
            final TransportNetworkGraph.Node currentNode = graph.nodes.get(current);
            final List<TransportNetworkGraph.Edge> edges = currentNode.edges;
            for (TransportNetworkGraph.Edge edge : edges) {
                final Stop neighbour = edge.target.stop;
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

        return Collections.emptyList();
    }

    private List<Stop> reconstructPath(HashMap<Stop, Stop> cameFrom, Stop current) {
        LinkedList<Stop> retval = new LinkedList<>();
        retval.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            retval.add(0, current);
        }
        return retval;
    }

    public interface Heuristic {

        public int score(Stop start, Stop goal, Stop toAssess);
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
