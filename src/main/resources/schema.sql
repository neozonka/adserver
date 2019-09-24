DROP TABLE IF EXISTS ads ;

CREATE TABLE ads (
adId integer NOT NULL PRIMARY KEY,
custName varchar(40) NOT NULL,
catId integer NOT NULL,
custEmail varchar(50) NOT NULL,
sTime integer NOT NULL
);

DROP TABLE IF EXISTS impressions;

CREATE TABLE impressions (
impressionid integer NOT NULL primary key,
adcat integer NOT NULL,
adid integer NOT NULL,
dateimpressed DATE NOT NULL,
timeimpressed TIME NOT NULL,
rlogid integer NOT NULL);

DROP TABLE IF EXISTS clicks;

CREATE TABLE clicks (
clickid integer NOT NULL primary key,
impressionid integer NOT NULL,
adcat integer NOT NULL,
adid integer NOT NULL,
dateclicked DATE NOT NULL,
timeclicked TIME NOT NULL,
rlogid integer NOT NULL);

DROP TABLE IF EXISTS common;

CREATE TABLE common (
lastadid integer DEFAULT 0 NOT NULL,
lastimpressionid integer DEFAULT 0 NOT NULL,
lastclickid integer DEFAULT 0 NOT NULL);
