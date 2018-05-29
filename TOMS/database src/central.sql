BEGIN TRANSACTION;
DROP TABLE IF EXISTS Vertex;
DROP TABLE IF EXISTS Edge;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Trailer;
DROP TABLE IF EXISTS Truck;
DROP TABLE IF EXISTS Driver;
DROP TABLE IF EXISTS Carrier;
DROP TABLE IF EXISTS CustomerOrder;
DROP TABLE IF EXISTS Disposition;



CREATE TABLE 'Vertex'(
	'id' INTEGER PRIMARY KEY AUTOINCREMENT,
	'country'   TEXT NOT NULL,
	'city' TEXT NOT NULL,
	'lat' TEXT NOT NULL,
	'lon' TEXT NOT NULL
);
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (1,'Greece','Athens','37.98332623','23.73332108');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (2,'Greece','Thessaloniki','40.69610638','22.88500077');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (3,'Greece','Kalamata','37.03891359','22.11419511');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (4,'Greece','Mitilini','39.11041506','26.55464758');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (5,'Greece','Patra','38.23000368','21.72998083');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (6,'Italy','Ancona','43.60037355','13.49994055');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (7,'Italy','Bari','41.1142204','16.87275793');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (8,'Italy','Ravenna','44.42037518','12.22001868');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (9,'Italy','Como','45.81000612','9.08000362');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (10,'Italy','Verona','45.44039044','10.99001623');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (11,'Italy','Venice','45.43865928','12.33499874');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (12,'Germany','Berlin','52.52181866','13.40154862');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (13,'Germany','Munich','48.12994204','11.57499345');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (14,'Germany','Frankfurt','50.09997683','8.67501542');
INSERT INTO `Vertex` (id,country,city,lat,lon) VALUES (15,'Germany','Kiel','54.33039044','10.13001705');
CREATE TABLE 'Edge'(
	`id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`source`INTEGER NOT NULL,
	`destination`INTEGER NOT NULL,
	`weight`REAL NOT NULL,
	FOREIGN KEY(`source`) REFERENCES `Vertex`(`id`),
	FOREIGN KEY(`destination`) REFERENCES `Vertex`(`id`)


);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (1,1,2,310339.180766384);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (2,2,7,507303.746525706);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (3,7,8,528414.283624247);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (4,8,10,149126.784623417);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (5,10,14,546172.85237663);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (6,1,3,177261.597714881);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (7,3,5,136695.843929643);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (8,5,2,291585.993654909);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (9,1,5,177412.084831717);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (10,5,11,1115808.30467388);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (11,11,14,585888.06925575);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (12,1,4,275493.793492972);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (13,4,3,451729.866318876);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (14,5,7,524903.529601123);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (15,7,10,676550.284986918);
INSERT INTO `Edge` (id,source,destination,weight) VALUES (16,10,11,104936.131743116);
CREATE TABLE `User` (
`UserID` INTEGER PRIMARY KEY AUTOINCREMENT,
`UserFirstName`TEXT NOT NULL,
`UserLastName`TEXT NOT NULL,
`UserPassword`TEXT NOT NULL,
`UserIsAdmin`INTEGER NOT NULL

);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10000,'Efthymios','Chatziathanasiadis','password',1);
INSERT INTO `User` (UserID,UserFirstName,UserLastName,UserPassword,UserIsAdmin) VALUES (10001,'Efthymios','Chatziathanasiadis','password',0);


CREATE TABLE `Customer` (
`id` INTEGER PRIMARY KEY AUTOINCREMENT,
`company`TEXT NOT NULL,
'phone' TEXT NOT NULL,
'addressId' INTEGER NOT NULL,
FOREIGN KEY(`addressId`) REFERENCES `Vertex`(`id`)
);

CREATE TABLE `Trailer` (
`id` INTEGER PRIMARY KEY AUTOINCREMENT,
`availabilityStatus`INTEGER NOT NULL,
'plateNumber' TEXT NOT NULL,
`conditionStatus`INTEGER NOT NULL,
'addressId' INTEGER NOT NULL,
FOREIGN KEY(`addressId`) REFERENCES `Vertex`(`id`)
);
CREATE TABLE `Truck` (
`id` INTEGER PRIMARY KEY AUTOINCREMENT,
`availabilityStatus`INTEGER NOT NULL,
`conditionStatus`INTEGER NOT NULL,
'plateNumber' TEXT NOT NULL,
'addressId' INTEGER NOT NULL,
FOREIGN KEY(`addressId`) REFERENCES `Vertex`(`id`)

);
CREATE TABLE `Driver` (
`id` INTEGER PRIMARY KEY AUTOINCREMENT,
`availabilityStatus`INTEGER NOT NULL,
`firstName`INTEGER NOT NULL,
`lastName`INTEGER NOT NULL,
'addressId' INTEGER NOT NULL,
FOREIGN KEY(`addressId`) REFERENCES `Vertex`(`id`)
);
CREATE TABLE `Carrier` (
`id` INTEGER PRIMARY KEY AUTOINCREMENT,
`company`TEXT NOT NULL,
'role' INTEGER NOT NULL,
'mode' TEXT NOT NULL
);

CREATE TABLE `CustomerOrder` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	'type' TEXT NOT NULL,
	`orderDate`	TEXT NOT NULL,
	`deliveryDate`	TEXT NOT NULL,
	`kg`	REAL NOT NULL,
  `trailerId`	INTEGER NOT NULL,
	`tarrif`	REAL NOT NULL,
	`customerId`	INTEGER NOT NULL,
  `systemUserId`	INTEGER NOT NULL,
	`originAddressId`	INTEGER NOT NULL,
	`destinationAddressId`	INTEGER NOT NULL,
	FOREIGN KEY(`customerId`) REFERENCES `Customer`(`id`),
	FOREIGN KEY(`systemUserId`) REFERENCES `User`(`UserID`),
	FOREIGN KEY(`trailerId`) REFERENCES `Trailer`(`id`),
	FOREIGN KEY(`originAddressId`) REFERENCES `Vertex`(`id`),
	FOREIGN KEY(`destinationAddressId`) REFERENCES `Vertex`(`id`)

);

CREATE TABLE `Disposition` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`carrierId`	INTEGER NOT NULL,
	`startDate`	TEXT NOT NULL,
  `finishDate`	TEXT NOT NULL,
	`tarrif`	REAL NOT NULL,
	'truckId' INTEGER,
	'driverId' INTEGER,
  'customerOrderId' INTEGER NOT NULL,
	`originAddressId`	INTEGER NOT NULL,
	`destinationAddressId`	INTEGER NOT NULL,
	FOREIGN KEY(`truckId`) REFERENCES `Truck`(`id`),
	FOREIGN KEY(`driverId`) REFERENCES `Driver`(`id`),
	FOREIGN KEY(`carrierId`) REFERENCES `Carrier`(`id`),
  FOREIGN KEY(`customerOrderId`) REFERENCES `CustomerOrder`(`id`),
	FOREIGN KEY(`originAddressId`) REFERENCES `Vertex`(`id`),
	FOREIGN KEY(`destinationAddressId`) REFERENCES `Vertex`(`id`)

);

CREATE TRIGGER DELETE_Customer
AFTER DELETE
ON Customer

BEGIN
DELETE FROM CustomerOrder WHERE  customerId =old.id;

END;
CREATE TRIGGER DELETE_CustomerOrder 
AFTER DELETE
ON CustomerOrder

BEGIN

DELETE FROM Disposition WHERE customerOrderId = old.id;

END;



COMMIT;
