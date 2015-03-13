package com.dgsd.android.data.db;

import android.content.Context;

import com.dgsd.android.data.model.DbCalendarInfo;
import com.dgsd.android.data.model.DbCalendarInfoEx;
import com.dgsd.android.data.model.DbGraphEdge;
import com.dgsd.android.data.model.DbRoute;
import com.dgsd.android.data.model.DbStop;
import com.dgsd.android.data.model.DbStopTime;
import com.dgsd.android.data.model.DbTrip;

import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RealmDatabaseBackend implements DatabaseBackend {

    private static RealmDatabaseBackend instance;

    private final Context context;

    public static RealmDatabaseBackend getInstance(Context context) {
        if (instance == null) {
            instance = new RealmDatabaseBackend(context.getApplicationContext());
        }
        return instance;
    }

    private RealmDatabaseBackend(Context context) {
        this.context = context;
    }

    @Override
    public int[] getTripIdsForStop(final int stopId) {
        final RealmResults<DbStopTime> results = getRealm().where(DbStopTime.class)
                .equalTo(Contract.StopTimes.COL_STOP_ID, stopId)
                .findAll();

        if (results == null || results.isEmpty()) {
            return new int[0];
        }

        final int[] stopTimeIds = new int[results.size()];
        for (int i = 0, len = results.size(); i < len; i++) {
            stopTimeIds[i] = results.get(i).getStopTimeId();
        }

        RealmQuery<DbTrip> query = getRealm().where(DbTrip.class);
        for (int i = 0, len = stopTimeIds.length; i < len; i++) {
            if (i != 0) {
                query = query.or();
            }
            query = query.equalTo(Contract.Trips.COL_STOP_TIME_ID, stopTimeIds[i]);
        }

        final RealmResults<DbTrip> dbTripResults = query.findAll();
        final int[] retval = new int[dbTripResults.size()];
        for (int i = 0, len = retval.length; i < len; i++) {
            retval[i] = dbTripResults.get(i).getId();
        }

        return retval;
    }

    @Override
    public int[] getStopIdsForTrip(int tripId) {
        final List<DbStopTime> results = getStopTimes(tripId);

        if (results == null || results.isEmpty()) {
            return new int[0];
        }

        final int[] retval = new int[results.size()];
        for (int i = 0, len = results.size(); i < len; i++) {
            retval[i] = results.get(i).getStopId();
        }

        return retval;
    }

    @Override
    public int[] getStopIdsAtSameLocation(final int stopId) {
        final RealmResults<DbStop> results = getRealm().where(DbStop.class)
                .equalTo(Contract.Stops.COL_ID, stopId)
                .or()
                .equalTo(Contract.Stops.COL_PARENT_ID, stopId)
                .findAll();

        if (results == null || results.isEmpty()) {
            return new int[]{stopId};
        }

        final int[] retval = new int[results.size()];
        for (int i = 0, len = results.size(); i < len; i++) {
            retval[i] = results.get(i).getId();
        }

        return retval;
    }

    @Override
    public List<DbGraphEdge> getNetwork() {
        return getRealm().where(DbGraphEdge.class).findAll();
    }

    @Override
    public List<DbStop> getStops() {
        return getRealm().where(DbStop.class).findAll();
    }

    @Override
    public List<DbStopTime> getStopTimes(final int tripId) {
        final Realm realm = getRealm();


        final DbTrip trip = getTrip(tripId);
        if (trip == null) {
            return Collections.emptyList();
        } else {
            return realm.where(DbStopTime.class)
                    .equalTo(Contract.StopTimes.COL_STOP_TIME_ID, trip.getStopTimeId())
                    .findAllSorted(Contract.StopTimes.COL_SECONDS_SINCE_MIDNIGHT);
        }
    }

    @Override
    public DbTrip getTrip(final int tripId) {
        return getRealm().where(DbTrip.class).equalTo(Contract.Trips.COL_ID, tripId).findFirst();
    }

    @Override
    public DbRoute getRoute(final int routeId) {
        return getRealm().where(DbRoute.class).equalTo(Contract.Routes.COL_ID, routeId).findFirst();
    }

    @Override
    public DbStop getStop(final int stopId) {
        return getRealm().where(DbStop.class).equalTo(Contract.Stops.COL_ID, stopId).findFirst();
    }

    @Override
    public DbCalendarInfo getCalendarInfo(int tripId, int julianDay) {
        return getRealm().where(DbCalendarInfo.class)
                .equalTo(Contract.CalendarInfo.COL_TRIP_ID, tripId)
                .lessThanOrEqualTo(Contract.CalendarInfo.COL_START_DAY, julianDay)
                .greaterThanOrEqualTo(Contract.CalendarInfo.COL_END_DAY, julianDay)
                .findFirst();
    }

    @Override
    public void saveStopTimes(final List<DbStopTime> stopTimes) {
        save(DbStopTime.class, stopTimes);
    }

    @Override
    public void saveCalendarInfoEx(List<DbCalendarInfoEx> calInfoEx) {
        save(DbCalendarInfoEx.class, calInfoEx);
    }

    @Override
    public void saveGraphEdges(List<DbGraphEdge> edges) {
        save(DbGraphEdge.class, edges);
    }

    @Override
    public void saveStops(List<DbStop> stops) {
        save(DbStop.class, stops);
    }

    @Override
    public void saveTrips(List<DbTrip> trips) {
        save(DbTrip.class, trips);
    }

    @Override
    public void saveRoutes(List<DbRoute> routes) {
        save(DbRoute.class, routes);
    }

    @Override
    public void saveCalendarInfo(List<DbCalendarInfo> calInfo) {
        save(DbCalendarInfo.class, calInfo);
    }

    private <T extends RealmObject> void save(final Class<T> cls, final List<T> models) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(io.realm.Realm realm) {
                realm.clear(cls);
                realm.copyToRealm(models);
            }
        });
    }

    private Realm getRealm() {
        return Realm.getInstance(context);
    }
}
