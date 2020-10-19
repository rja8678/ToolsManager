DROP TABLE IF EXISTS collection;
DROP TABLE IF EXISTS tool_log ;
DROP TABLE IF EXISTS user_owns_tool;
DROP TABLE IF EXISTS tool_tooltype;
DROP TABLE IF EXISTS user_lendinglog;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
  iduser SERIAL,
  fname VARCHAR(45) NULL,
  lname VARCHAR(45) NULL,
  PRIMARY KEY (iduser)
);


DROP TABLE IF EXISTS tool;
CREATE TABLE tool (
  idtool SERIAL,
  name VARCHAR(45) NULL,
  lendable TINYINT(1) NULL,
  purchase_date DATE NULL,
  PRIMARY KEY (idtool)
);


DROP TABLE IF EXISTS tooltype ;
CREATE TABLE tooltype (
  idtool_type INT NOT NULL,
  type_name VARCHAR(45) NULL,
  PRIMARY KEY (idtool_type)
);


DROP TABLE IF EXISTS lendinglog;
CREATE TABLE lendinglog (
  idlog SERIAL,
  log_date DATE NOT NULL DEFAULT CURRENT_DATE,
  action INT NULL,
  return_date DATE NULL,
  PRIMARY KEY (idlog)
);


CREATE TABLE tool_log (
  idtool INT NOT NULL,
  idlog INT NOT NULL,
  PRIMARY KEY (idtool, idlog),
  FOREIGN  KEY (idtool) REFERENCES tool (idtool),
  FOREIGN  KEY (idlog) REFERENCES lendinglog (idlog)
);


CREATE TABLE user_owns_tool (
  iduser INT NOT NULL,
  idtool INT NOT NULL,
  PRIMARY KEY (iduser, idtool),
  FOREIGN  KEY (iduser) REFERENCES user (iduser),
  FOREIGN  KEY (idtool) REFERENCES tool (idtool)
);


CREATE TABLE tool_tooltype (
  idtool INT NOT NULL,
  idtool_type INT NOT NULL,
  PRIMARY KEY (idtool, idtool_type),
  FOREIGN  KEY (idtool) REFERENCES tool (idtool),
  FOREIGN  KEY (idtool_type) REFERENCES tooltype (idtool_type)
);


CREATE TABLE user_lending_log (
  to_iduser INT NOT NULL,
  idlog INT NOT NULL,
  from_iduser INT NOT NULL,
  PRIMARY KEY (to_iduser, idlog, from_iduser),
  FOREIGN  KEY (to_iduser) REFERENCES user (iduser),
  FOREIGN  KEY (from_iduser) REFERENCES user (iduser),
  FOREIGN  KEY (idlog) REFERENCES lendinglog (idlog)
);


CREATE TABLE collection (
  iduser INT NOT NULL,
  idtool INT NOT NULL,
  PRIMARY KEY (iduser, idtool),
  FOREIGN  KEY (iduser) REFERENCES user (iduser),
  FOREIGN  KEY (idtool) REFERENCES tool (idtool)
);
