--DROP TABLE Brands;
--DROP TABLE Categories;
--DROP TABLE Colors;
--DROP TABLE Kinds;
--DROP TABLE Qualities;

CREATE TABLE Brands (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   code CHAR(32),
   name VARCHAR(255),
   link VARCHAR(1024),
   CONSTRAINT Brands_PK PRIMARY KEY (id)
);

CREATE UNIQUE INDEX Brands_IDX1 ON Brands (code);

CREATE TABLE Qualities (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   numb INT,
   code CHAR(9),
   color CHAR(6),
   CONSTRAINT Qualities_PK PRIMARY KEY (id)
);

CREATE UNIQUE INDEX Qualities_IDX1 ON Qualities (code);

CREATE TABLE Categories (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   numb INT,
   name CHAR(64),
   CONSTRAINT Categories_PK PRIMARY KEY (id)
);

CREATE INDEX Categories_IDX_numb ON categories(numb);
CREATE INDEX Categories_IDX_name ON categories(name);

CREATE TABLE Colors (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   name CHAR(64),
   CONSTRAINT Colors_PK PRIMARY KEY (id)
);

CREATE TABLE Kinds (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   name CHAR(64),
   CONSTRAINT Kinds_PK PRIMARY KEY (id)
);

INSERT INTO Brands (code, name, link) VALUES ('supertomatoes', 'Supertomatoes Ltd.', 'http://www.bbb.com');
INSERT INTO Brands (code, name, link) VALUES ('superseeds', 'Superseeds Ltd.', 'http://www.aaa.com');

INSERT INTO Categories (numb, name) VALUES (1, 'Tomatoes');
INSERT INTO Categories (numb, name) VALUES (2, 'Peppers');
INSERT INTO Categories (numb, name) VALUES (3, 'Eggplants');

INSERT INTO Qualities (numb, code, color) VALUES (1,'Yes      ','baffba');
INSERT INTO Qualities (numb, code, color) VALUES (2,'No       ','ffd1d1');
INSERT INTO Qualities (numb, code, color) VALUES (3,'Run out  ','babaff');
INSERT INTO Qualities (numb, code, color) VALUES (4,'Lost     ','ffd48f');
INSERT INTO Qualities (numb, code, color) VALUES (5,'Re-grad  ','e49af3');
INSERT INTO Qualities (numb, code, color) VALUES (6,'> 1      ','83d3d3');
INSERT INTO Qualities (numb, code, color) VALUES (7,'Expensive','ffff4d');

INSERT INTO Colors (name) VALUES ('Red');
INSERT INTO Colors (name) VALUES ('Yellow');
INSERT INTO Colors (name) VALUES ('Pink');
INSERT INTO Colors (name) VALUES ('Raspberry');

INSERT INTO Kinds (name) VALUES ('Muscat');
INSERT INTO Kinds (name) VALUES ('Round');
INSERT INTO Kinds (name) VALUES ('Plum');
INSERT INTO Kinds (name) VALUES ('Conical');
