INSERT INTO "user" (fname, lname) VALUES ('zach','easley');
INSERT INTO "user" (fname, lname) VALUES ('riley','adams');
INSERT INTO "user" (fname, lname) VALUES ('andrew','nash');
INSERT INTO "user" (fname, lname) VALUES ('mackenzie','rhody');

INSERT INTO tool (name, lendable, purchase_date) VALUES ('STIHL MSA 161 T', 1, '2020-10-20');
INSERT INTO tool (name, lendable, purchase_date) VALUES ('Nikro Dewalt 20V Cordless Drill', 1, '2020-10-20');
INSERT INTO tool (name, lendable, purchase_date) VALUES ('Bosch PS31-2A 12-Volt Max', 0, '2020-10-20');

INSERT INTO tooltype (idtool_type, type_name) VALUES (1, 'power');
INSERT INTO tooltype (idtool_type, type_name) VALUES (2, 'saw');
INSERT INTO tooltype (idtool_type, type_name) VALUES (3, 'drill');

INSERT INTO tool_tooltype VALUES (1,1);
INSERT INTO tool_tooltype VALUES (1,2);

INSERT INTO tool_tooltype VALUES (2,1);
INSERT INTO tool_tooltype VALUES (2,3);

INSERT INTO tool_tooltype VALUES (3,1);
INSERT INTO tool_tooltype VALUES (3,3);