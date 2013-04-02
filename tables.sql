/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

DROP TABLE Fine;
DROP TABLE Borrowing;
DROP TABLE HoldRequest;
DROP TABLE HasSubject;
DROP TABLE HasAuthor;
DROP TABLE BookCopy;
DROP TABLE Book;
DROP TABLE Borrower;
DROP TABLE BorrowerType;

CREATE TABLE BorrowerType (
	type			VARCHAR(20)		NOT NULL,
	bookTimeLimit	INTEGER			NOT NULL,
	PRIMARY KEY		(type)
);

INSERT INTO BorrowerType VALUES	('Student', 14);
INSERT INTO BorrowerType VALUES	('Faculty', 84);
INSERT INTO BorrowerType VALUES	('Staff', 42);

CREATE TABLE Borrower (
	bid				INTEGER			NOT NULL,
	password 		VARCHAR(20)		NOT NULL,
	name			VARCHAR(50)		NOT NULL,
	address			VARCHAR(50),
	phone			FLOAT,
	emailAddress	VARCHAR(50)		UNIQUE,
	sinOrStNo		FLOAT			UNIQUE NOT NULL,
	expiryDate		DATE,
	type			VARCHAR(10)		NOT NULL,
	PRIMARY KEY		(bid),
	FOREIGN KEY		(type)			REFERENCES BorrowerType
);

INSERT INTO Borrower VALUES	(1, '123', 'Alec', '1234 Vancouver', 6043101010, 'Alec@gmail.com', 23056096, '2015-01-01', 'Student' );
INSERT INTO Borrower VALUES	(2, 'qwerty', 'Kevin P.', 'North Burnaby', 7789975538, 'cemric@hotmail.com', 65500084, '2015-01-01', 'Staff' );

CREATE TABLE Book (
	callNumber		INTEGER			NOT NULL,
	isbn			FLOAT			UNIQUE NOT NULL,
	title			VARCHAR(50)		NOT NULL,
	mainAuthor		VARCHAR(50)		NOT NULL,
	publisher		VARCHAR(50),
	year			INTEGER,
	PRIMARY KEY		(callNumber)
);

INSERT INTO Book VALUES (1, 12345, 'SQL for Dummies', 'Kevin Petersen', 'Code 4 U inc.', 2010);
INSERT INTO Book VALUES (2, 23456, 'Java for Dummies', 'Kevin Petersen', 'Code 4 U inc.', 2011);
INSERT INTO Book VALUES (3, 34567, 'Java for Fools!', 'Alec', 'Coders R Us', 2011);

CREATE TABLE BookCopy (
	callNumber		INTEGER			NOT NULL,
	copyNo			INTEGER			NOT NULL,
	status			VARCHAR(10)		NOT NULL,
	PRIMARY KEY		(callNumber, copyNo),
	FOREIGN KEY		(callNumber)	REFERENCES Book
);

INSERT INTO BookCopy VALUES (1, 1, 'in');
INSERT INTO BookCopy VALUES (2, 1, 'in');
INSERT INTO BookCopy VALUES (2, 2, 'in');
INSERT INTO BookCopy VALUES (2, 3, 'out');
INSERT INTO BookCopy VALUES (3, 1, 'on-hold');
INSERT INTO BookCopy VALUES (3, 2, 'in');

CREATE TABLE HasAuthor (
	callNumber		INTEGER			NOT NULL,
	name			VARCHAR(50)		NOT NULL,
	PRIMARY KEY		(callNumber, name),
	FOREIGN KEY		(callNumber)	REFERENCES Book
);

INSERT INTO HasAuthor VALUES (1, 'Kevin Petersen');
INSERT INTO HasAuthor VALUES (2, 'Kevin Petersen');
INSERT INTO HasAuthor VALUES (2, 'Alec');
INSERT INTO HasAuthor VALUES (3, 'Alec');

CREATE TABLE HasSubject (
	callNumber		INTEGER			NOT NULL,
	subject			VARCHAR(50)		NOT NULL,
	PRIMARY KEY		(callNumber, subject),
	FOREIGN KEY		(callNumber)	REFERENCES Book
);

INSERT INTO HasSubject VALUES (1, 'SQL');
INSERT INTO HasSubject VALUES (1, 'Code');
INSERT INTO HasSubject VALUES (2, 'Java');
INSERT INTO HasSubject VALUES (2, 'Code');
INSERT INTO HasSubject VALUES (3, 'Java');
INSERT INTO HasSubject VALUES (3, 'Code');

CREATE TABLE HoldRequest(
	hid				INTEGER			NOT NULL,
	bid				INTEGER			NOT NULL,
	callNumber		INTEGER			NOT NULL,
	issuedDate		DATE,
	PRIMARY KEY		(hid),
	FOREIGN KEY		(bid)			REFERENCES Borrower,
	FOREIGN KEY		(callNumber)	REFERENCES Book
);

INSERT INTO HoldRequest VALUES (1, 1, 3, '2013-03-14');

CREATE TABLE Borrowing(
	borid			INTEGER			NOT NULL,
	bid				INTEGER			NOT NULL,
	callNumber		INTEGER			NOT NULL,
	copyNo			INTEGER			NOT NULL,
	outDate			DATE			NOT NULL,
	inDate			DATE,
	PRIMARY KEY		(borid),
	FOREIGN KEY		(bid)			REFERENCES Borrower,
	FOREIGN KEY		(callNumber, copyNo) REFERENCES BookCopy
);

INSERT INTO Borrowing VALUES (1, 2, 2, 3, '2012-01-01', '2013-01-01');

CREATE TABLE Fine (
	fid				INTEGER			NOT NULL,
	amount			FLOAT			NOT NULL,
	issuedDate		DATE			NOT NULL,
	paidDate		DATE,
	borid			INTEGER			NOT NULL,
	PRIMARY KEY		(fid),
	FOREIGN KEY		(borid)			REFERENCES Borrowing
);

INSERT INTO Fine VALUES (1, 10.55, '2013-01-01', NULL, 1);
