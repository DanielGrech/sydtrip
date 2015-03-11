CREATE TABLE calendar_info_ex (
    tripId INTEGER,
    julianDay INTEGER,
    exceptionType INTEGER,
    PRIMARY KEY (tripId, julianDay)
) WITHOUT ROWID