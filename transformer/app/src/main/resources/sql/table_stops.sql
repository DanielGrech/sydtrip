CREATE TABLE stops (
    _id INTEGER PRIMARY KEY,
    code TEXT,
    name INTEGER,
    lat REAL,
    lng REAL,
    type INTEGER,
    parentId INTEGER,
    platformCode TEXT
) WITHOUT ROWID;