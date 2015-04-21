CREATE TABLE SHOW (
    _id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    trailer TEXT NOT NULL,
    description TEXT NOT NULL,
    logo BLOB NOT NULL,  -- add default logo
    -- rating_count INTEGER NOT NULL,
    rating REAL NOT NULL
);


CREATE TABLE CHANNEL (
    _id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    frequency TEXT NOT NULL,
    code TEXT NOT NULL,
    vertical INTEGER NOT NULL,
    logo BLOB NOT NULL,
    -- rating_count INTEGER NOT NULL,
    rating REAL NOT NULL
);

CREATE TABLE TV_RECORD (
    _id INTEGER PRIMARY KEY,
    channel_id INTEGER,
    show_id INTEGER,
    start_time TEXT NOT NULL,
    end_time TEXT NOT NULL,
    FOREIGN KEY(channel_id) REFERENCES CHANNEL(_id),
    FOREIGN KEY(show_id) REFERENCES SHOW(_id)
);

CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US')
