package com.dgsd.sydtrip.routing;

import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopPair;
import com.dgsd.sydtrip.routing.model.RoutingResult;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RoutingEngineTest {

    public static final int ORIG_STOP_ID = 1;
    public static final int DEST_STOP_ID = 2;

    @Test
    public void shouldFindTripsBetween2StationsWithDirectRoute() {
        final DummyRoutingDataProvider dataProvider = new DummyRoutingDataProvider();
        dataProvider.stopIdToTripIds.put(ORIG_STOP_ID, new int[]{123, 456});
        dataProvider.stopIdToTripIds.put(DEST_STOP_ID, new int[]{456, 789});

        final RoutingResult rr = new RoutingEngine(dataProvider)
                .route(createStopPair(ORIG_STOP_ID, DEST_STOP_ID))
                .toBlocking()
                .single();
//        assertThat(rr.tripIds).containsExactly(456);
    }

    private StopPair createStopPair(int origId, int destId) {
        final Stop orig = new Stop(origId, "O", "Origin Station", 0, 0, null, 0, Stop.StopType.RAIL);
        final Stop dest = new Stop(destId, "D", "Dest Station", 0, 0, null, 0, Stop.StopType.RAIL);

        return new StopPair(orig, dest);
    }

    private class DummyRoutingDataProvider implements RoutingDataProvider {

        public Map<Integer, int[]> stopIdToTripIds = new HashMap<>();

        public Map<Integer, int[]> tripIdToStopIds = new HashMap<>();

        @Override
        public int[] getTripsForStopId(int stopId) {
            final int[] retval = stopIdToTripIds.get(stopId);
            return retval == null ? new int[0] : retval;
        }

        @Override
        public int[] getStopIdsForTrip(int tripId) {
            final int[] retval = tripIdToStopIds.get(tripId);
            return retval == null ? new int[0] : retval;
        }

        @Override
        public int[] getStopIdsAtSameLocation(int stopId) {
            return new int[]{stopId};
        }
    }

}
