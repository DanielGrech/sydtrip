CREATE TABLE stop_times (
    tripId INTEGER NOT NULL,
    stopId INTEGER NOT NULL,
    secondsSinceMidnight INTEGER,
    PRIMARY KEY (tripId, stopId)
) WITHOUT ROWID;