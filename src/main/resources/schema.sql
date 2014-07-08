CREATE TABLE snapshot (
	id integer auto_increment NOT NULL,
	syncKey VARCHAR NOT NULL,
	deviceId VARCHAR NOT NULL,
	collectionId int NOT NULL,
	filterType int,
	uidNext int
);

CREATE TABLE email_snapshot (
	id integer auto_increment NOT NULL,
	snapshotId int NOT NULL,
	uid int NOT NULL,
	read boolean,
	internalDate timestamp,
	answered boolean,
	FOREIGN KEY (snapshotId) REFERENCES snapshot(id)
);