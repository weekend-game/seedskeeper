--DROP TABLE Brands;

CREATE TABLE Brands (
   id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   code CHAR(32),
   name VARCHAR(255),
   link VARCHAR(1024)
);

CREATE UNIQUE INDEX Brands_IDX1 ON Brands (code);

--DROP TABLE Qualities;

CREATE TABLE Qualities (
   id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   numb INT,
   code CHAR(9),
   color CHAR(6)
);

CREATE UNIQUE INDEX Qualities_IDX1 ON Qualities (code);

INSERT INTO Qualities (numb, code, color) VALUES (1,'Yes      ','baffba');
INSERT INTO Qualities (numb, code, color) VALUES (2,'No       ','ffd1d1');
INSERT INTO Qualities (numb, code, color) VALUES (3,'Run out  ','babaff');
INSERT INTO Qualities (numb, code, color) VALUES (4,'Lost     ','ffd48f');
INSERT INTO Qualities (numb, code, color) VALUES (5,'Re-grad  ','e49af3');
INSERT INTO Qualities (numb, code, color) VALUES (6,'> 1      ','83d3d3');
INSERT INTO Qualities (numb, code, color) VALUES (7,'Expensive','ffff4d');

