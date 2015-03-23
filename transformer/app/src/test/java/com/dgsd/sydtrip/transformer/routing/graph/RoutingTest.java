package com.dgsd.sydtrip.transformer.routing.graph;

import com.dgsd.sydtrip.transformer.GraphEdge;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoutingTest {

    TransportNetworkGraph graph;

    Map<Integer, Stop> stopMap;

    @Before
    public void setup() throws IOException {
        graph = createTransportNetworkGraph();
        stopMap = createStopMap();
    }

    @After
    public void teardown() {
        graph = null;
        stopMap = null;
    }

    private Map<Integer, Stop> createStopMap() throws IOException {
        final String filePath = "/Users/daniel/Desktop/gtfs_output/rail/stops.csv";

        final Map<Integer, Stop> retval = new HashMap<>();
        try (CsvListReader reader
                     = new CsvListReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE)) {

            List<String> nextLine;
            while ((nextLine = reader.read()) != null) {
                final Stop stop = Mockito.mock(Stop.class);

                Mockito.when(stop.getId()).thenReturn(Integer.valueOf(nextLine.get(0)));
                Mockito.when(stop.getName()).thenReturn(nextLine.get(2));
                Mockito.when(stop.getLat()).thenReturn(Float.valueOf(nextLine.get(3)));
                Mockito.when(stop.getLng()).thenReturn(Float.valueOf(nextLine.get(4)));

                retval.put(stop.getId(), stop);
            }
        }
        return retval;
    }

    private TransportNetworkGraph createTransportNetworkGraph() throws IOException {
        final String filePath = "/Users/daniel/Desktop/gtfs_output/rail/network_graph.csv";

        Set<GraphEdge> edges = new HashSet<>();
        try (CsvListReader reader
                     = new CsvListReader(new FileReader(filePath), CsvPreference.STANDARD_PREFERENCE)) {

            List<String> nextLine;
            while ((nextLine = reader.read()) != null) {
                edges.add(new GraphEdge(
                        Integer.valueOf(nextLine.get(0)),
                        Integer.valueOf(nextLine.get(1)),
                        Integer.valueOf(nextLine.get(2)),
                        Integer.valueOf(nextLine.get(3)),
                        Integer.valueOf(nextLine.get(4))
                ));
            }
        }

        TransportNetworkGraph graph = new TransportNetworkGraph();
        graph.addEdges(edges);

        return graph;
    }

//    @Test
//    public void testRoutingDirect() {
//        final int startNode = 485; // Quakers Hill
////        final int endNode = 6945; // Marayong
//        final int endNode = 2978; // Blacktown
//        graph.findPath(startNode, endNode, stopMap).stream().forEach(path -> {
//            path.stream()
//                    .map(n -> n.id)
//                    .map(id -> stopMap.get(id).getName() + "(" + id + ")")
//                    .forEach(System.out::println);
//            System.out.println("==========");
//        });
//    }

//    @Ignore
//    @Test
//    public void testRoutingOneStopover() {
//        final int startNode = 485; // Quakers Hill
////        final int endNode = 3901; // Doonside
//        final int endNode = 2843; // Penrith
////        final int endNode = 2562; // Epping
//
//
//        List<Node> path = graph.findPath(startNode, endNode, stopMap);
//        System.out.println("==========");
//    }


}
