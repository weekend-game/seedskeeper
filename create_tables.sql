CREATE TABLE General (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  code CHAR(16),
  name CHAR(64),
  value CHAR(64)
);

CREATE INDEX General_IDX1 ON General (code, name);

CREATE TABLE Brands (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   code CHAR(32),
   name VARCHAR(255),
   link VARCHAR(1024),
   CONSTRAINT Brands_PK PRIMARY KEY (id)
);

CREATE UNIQUE INDEX Brands_IDX1 ON Brands (code);

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

CREATE TABLE Qualities (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   numb INTEGER,
   code CHAR(9),
   color CHAR(6),
   CONSTRAINT Qualities_PK PRIMARY KEY (id)
);

CREATE UNIQUE INDEX Qualities_IDX1 ON Qualities (code);

CREATE TABLE Tags (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   name CHAR(32)
);

CREATE INDEX Tags_IDX1 ON Tags (name);

CREATE TABLE Seeds (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   name VARCHAR(255),
   numb CHAR(16),
   category CHAR(32),
   use_by INTEGER,
   brand_id INTEGER,
   vegetation CHAR(32),
   color_id INTEGER,
   mass CHAR(32),
   height CHAR(32),
   yield CHAR(32),
   kind_id INTEGER,
   length CHAR(32),
   quality_id INTEGER,
   sowing_time CHAR(32),
   transplant_time CHAR(32),
   in_ground CHAR(32),
   planting_scheme CHAR(32),
   ground CHAR(3),
   hybrid BOOLEAN DEFAULT FALSE NOT NULL,
   photo BLOB,
   description LONG VARCHAR,
   mark BOOLEAN DEFAULT FALSE NOT NULL,
   selected BOOLEAN DEFAULT FALSE NOT NULL,
   crt_date DATE,
   CONSTRAINT Seeds_PK PRIMARY KEY (id)
);

CREATE INDEX Seeds_IDX1 ON Seeds (name);
CREATE INDEX Seeds_IDX2 ON Seeds (mark);
CREATE INDEX Seeds_IDX3 ON Seeds (selected);
CREATE INDEX Seeds_IDX4 ON Seeds (category);
CREATE INDEX Seeds_IDX5 ON Seeds (brand_id);
CREATE INDEX Seeds_IDX6 ON Seeds (color_id);
CREATE INDEX Seeds_IDX7 ON Seeds (kind_id);
CREATE INDEX Seeds_IDX8 ON Seeds (quality_id);
CREATE UNIQUE INDEX Seeds_IDX9 ON Seeds (category_id, numb);

CREATE TABLE Seed_tag (
   seed_id INTEGER,
   tag_id INTEGER
);

CREATE INDEX seed_tag_IDX1 ON Seed_tag (seed_id);
CREATE INDEX seed_tag_IDX2 ON Seed_tag (tag_id);

CREATE TABLE Planting (
   id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
   seed_id INTEGER,
   planting_year INTEGER,
   numb CHAR(16),
   name VARCHAR(255),
   category CHAR(32),
   brand_id INTEGER,
   rem VARCHAR(128)
);

CREATE INDEX Planting_IDX1 ON Planting (seed_id);
CREATE INDEX Planting_IDX2 ON Planting (planting_year);

CREATE TABLE Filter_Tags (
   id INTEGER,
   name CHAR(32)
);

CREATE INDEX Filter_Tags_IDX1 ON Filter_Tags (id);
CREATE INDEX Filter_Tags_IDX2 ON Filter_Tags (name);

INSERT INTO Brands (code, name, link) VALUES ('суперпомидорки', 'ООО "Суперпомидорки"', 'http://www.bbb-bbb.ru');
INSERT INTO Brands (code, name, link) VALUES ('суперсемена', 'ООО "Суперсемена"', 'http://www.aaa-aaa.ru');

INSERT INTO Colors (name) VALUES ('Красный');
INSERT INTO Colors (name) VALUES ('Желтый');
INSERT INTO Colors (name) VALUES ('Розовый');
INSERT INTO Colors (name) VALUES ('Малиновый');

INSERT INTO Kinds (name) VALUES ('Мускатный');
INSERT INTO Kinds (name) VALUES ('Круглый');
INSERT INTO Kinds (name) VALUES ('Сливовидный');
INSERT INTO Kinds (name) VALUES ('Конический');

INSERT INTO Qualities (numb, code, color) VALUES (1, 'Да       ', 'baffba');
INSERT INTO Qualities (numb, code, color) VALUES (2, 'Нет      ', 'ffd1d1');
INSERT INTO Qualities (numb, code, color) VALUES (3, 'Кончились', 'babaff');
INSERT INTO Qualities (numb, code, color) VALUES (4, 'Утерян   ', 'ffd48f');
INSERT INTO Qualities (numb, code, color) VALUES (5, 'Пересорт ', 'e49af3');
INSERT INTO Qualities (numb, code, color) VALUES (6, '> 1      ', '83d3d3');
INSERT INTO Qualities (numb, code, color) VALUES (7, 'Дорогие  ', 'ffff4d');
