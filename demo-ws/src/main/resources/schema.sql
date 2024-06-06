DROP TABLE IF EXISTS tools;

CREATE TABLE IF NOT EXISTS tools (
 `tool_code` varchar(20) NOT NULL,
 `tool_type` varchar(20) NOT NULL,
 `brand` varchar(20) NOT NULL,
 `daily_charge` decimal(4,2) NOT NULL,
 `weekday_charge` varchar(5) NOT NULL,
 `weekend_charge` varchar(5) NOT NULL,
 `holiday_charge` varchar(5) NOT NULL,
 PRIMARY KEY(`tool_code`)
);

INSERT INTO tools VALUES ('CHNS', 'Chainsaw', 'Stihl',1.49,'YES','NO','YES');
INSERT INTO tools VALUES ('LADW', 'Ladder', 'Werner',1.99,'YES','YES','NO');
INSERT INTO tools VALUES ('JAKD', 'Jackhammer', 'DeWalt',2.99,'YES','NO','NO');
INSERT INTO tools VALUES ('JAKR', 'Jackhammer', 'Ridgid',2.99,'YES','NO','NO');
