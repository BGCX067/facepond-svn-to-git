select * from app_user where last_updated <= '2011-06-14 07:20:14'

select * from user_deals

CREATE TABLE `auth_properties` (
  `NAME` varchar(30) NOT NULL,
  `VALUE` varchar(256) NOT NULL,
  PRIMARY KEY (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$



CREATE TABLE `auth_properties` (
  `NAME` varchar(30) NOT NULL,
  `VALUE` varchar(256) NOT NULL,
  PRIMARY KEY (`NAME`)
)

CREATE TABLE `city_division` (
  `city` varchar(45) NOT NULL,
  `division_id` varchar(45) NOT NULL,
  `lat` decimal(7,5) DEFAULT NULL,
  `lng` decimal(8,5) DEFAULT NULL,
  PRIMARY KEY (`city`)
);

insert into city_division values ('Los Angeles', 'los-angeles', 34.0522, -118.243)
insert into city_division values ('San Francisco', 'san-francisco', 37.7752, -122.419)
insert into city_division (city, division_id) values ('Chicago', 'chicago');
insert into city_division (city, division_id) values ('New York City', 'new-york');
insert into city_division (city, division_id) values ('New York', 'new-york');

select * from auth_properties

insert into auth_properties values('return.url','http://facepond.couponpond.com:8080/facepond/loginWithSSO/')

insert into auth_properties values('app.id', '446309825388322');
insert into auth_properties values('app.secret', 'f8a03eb79d45b4436519fc266ffb31d8')