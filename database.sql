CREATE TABLE SHOW (
	_id	INTEGER,
	name	TEXT NOT NULL,
	trailer	TEXT,
	logo	BLOB NOT NULL,
	rating	REAL NOT NULL DEFAULT 0,
	previous_rate	REAL NOT NULL DEFAULT 0,
	rating_count	INTEGER NOT NULL DEFAULT 0,
	description	TEXT ,
	is_favorite	INTEGER NOT NULL DEFAULT 0,
	priority	INTEGER NOT NULL DEFAULT 0,
	server_id	TEXT NOT NULL,
	PRIMARY KEY(_id)
);


CREATE TABLE CHANNEL (
	_id	INTEGER,
	name	TEXT NOT NULL,
	logo	BLOB NOT NULL,
	rating	REAL NOT NULL DEFAULT 0,
	description	TEXT ,
	previous_rate	REAL NOT NULL DEFAULT 0,
	rating_count	INTEGER NOT NULL DEFAULT 0,
	is_favorite	INTEGER NOT NULL DEFAULT 0,
	priority	INTEGER NOT NULL DEFAULT 0,
	server_id	TEXT NOT NULL,
	PRIMARY KEY(_id)
);

CREATE TABLE TV_RECORD (
	_id	INTEGER,
	channel_id	INTEGER NOT NULL,
	show_id	INTEGER NOT NULL,
	start_time	TEXT NOT NULL,
	end_time	TEXT NOT NULL,
	server_id	TEXT NOT NULL,
	is_reminded	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY(_id),
	FOREIGN KEY(channel_id) REFERENCES CHANNEL ( id ),
	FOREIGN KEY(show_id) REFERENCES SHOW ( id )
);
CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT en_US)
