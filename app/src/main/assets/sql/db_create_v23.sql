-- Creator:       MySQL Workbench 6.3.8/ExportSQLite Plugin 0.1.0
-- Author:        pulma
-- Caption:       New Model
-- Project:       Name of the project
-- Changed:       2018-07-12 02:35
-- Created:       2018-03-20 10:27
PRAGMA foreign_keys = OFF;

BEGIN;
CREATE TABLE  "Remedio"(
  "idRemedio" INTEGER PRIMARY KEY NOT NULL,
  "nome" VARCHAR(45) NOT NULL
);
CREATE TABLE  "Animal"(
  "idAnimal" INTEGER PRIMARY KEY NOT NULL,
  "nome" VARCHAR(45) NOT NULL,
  "ativo" BOOL,
  "numero" VARCHAR(10)
);
CREATE TABLE  "Enfermidade"(
  "idEnfermidade" INTEGER PRIMARY KEY NOT NULL,
  "descricao" VARCHAR(100) NOT NULL
);
CREATE TABLE  "Parto"(
  "idAnimal_Parto" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimal" VARCHAR(10) NOT NULL,
  "data" DATE NOT NULL,
  "cria" INTEGER NOT NULL,
  CONSTRAINT "fk_Animal_Parto_Animal1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX  "Parto.fk_Animal_Parto_Animal1_idx" ON "Parto" ("Animal_idAnimal");
CREATE TABLE  "Inseminacao"(
  "idAnimal_Inseminacao" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimal" VARCHAR(10) NOT NULL,
  "data" DATE NOT NULL,
  CONSTRAINT "fk_Animal_Inseminacao_Animal1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX  "Inseminacao.fk_Animal_Inseminacao_Animal1_idx" ON "Inseminacao" ("Animal_idAnimal");
CREATE TABLE  "Pessoa"(
  "idPessoa" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "nome" VARCHAR(45) NOT NULL,
  "cpf" VARCHAR(14) NOT NULL,
  "rg" VARCHAR(14) NOT NULL,
  "ativo" BOOL
);
INSERT INTO "Pessoa"("idPessoa","nome","cpf","rg","ativo") VALUES(1, 'Admin', '0', '0', 1);
CREATE TABLE  "Medida"(
  "idMedida" INTEGER PRIMARY KEY NOT NULL,
  "nome" VARCHAR(45) NOT NULL
);
INSERT INTO "Medida"("idMedida","nome") VALUES(1, "Miligramas");
CREATE TABLE  "Administrador"(
  "idAdministrador" INTEGER PRIMARY KEY NOT NULL,
  "Pessoa_idPessoa" INTEGER NOT NULL,
  "login" VARCHAR(45) NOT NULL,
  "senha" VARCHAR(45) NOT NULL,
  "admin" BOOLEAN NOT NULL,
  CONSTRAINT "fk_Funcionario_Pessoa1"
    FOREIGN KEY("Pessoa_idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX  "Administrador.fk_Funcionario_Pessoa1_idx" ON "Administrador" ("Pessoa_idPessoa");
INSERT INTO "Administrador"("idAdministrador","Pessoa_idPessoa","login","senha","admin") VALUES(1, 1, 'admin', '5050', 1);
CREATE TABLE  "Administrador_Notifica_Pessoa"(
  "idAdministrador_Notifica_Pessoa" INTEGER PRIMARY KEY,
  "Administrador_idNotifica" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "descricao" VARCHAR(100) NOT NULL,
  CONSTRAINT "fk_Administrador_Notifica_Administrador_Administrador1"
    FOREIGN KEY("Administrador_idNotifica")
    REFERENCES "Administrador"("idAdministrador")
);
CREATE INDEX  "Administrador_Notifica_Pessoa.fk_Administrador_Notifica_Pessoa_Administrador1_idx" ON "Administrador_Notifica_Pessoa" ("Administrador_idNotifica");
CREATE TABLE  "Cio"(
  "idAnimal_Cio" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimalPorCima" VARCHAR(10) NOT NULL,
  "Animal_idAnimalPorBaixo" VARCHAR(10) NOT NULL,
  "Pessoa_idPessoa" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  CONSTRAINT "fk_Animal_Cio_Animal1"
    FOREIGN KEY("Animal_idAnimalPorCima")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Animal_Cio_Pessoa1"
    FOREIGN KEY("Pessoa_idPessoa")
    REFERENCES "Pessoa"("idPessoa"),
  CONSTRAINT "fk_Animal_Cio_Animal2"
    FOREIGN KEY("Animal_idAnimalPorBaixo")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX  "Cio.fk_Animal_Cio_Animal1_idx" ON "Cio" ("Animal_idAnimalPorCima");
CREATE INDEX  "Cio.fk_Animal_Cio_Pessoa1_idx" ON "Cio" ("Pessoa_idPessoa");
CREATE INDEX  "Cio.fk_Animal_Cio_Animal2_idx" ON "Cio" ("Animal_idAnimalPorBaixo");
CREATE TABLE  "Animal_Remedio"(
  "idAnimal_Remedio" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimal" VARCHAR(10) NOT NULL,
  "Remedio_idRemedio" INTEGER NOT NULL,
  "Medida_idMedida" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "quantidade" INTEGER NOT NULL,
  CONSTRAINT "fk_Vaca_remedio_Vaca1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Vaca_remedio_Remedio1"
    FOREIGN KEY("Remedio_idRemedio")
    REFERENCES "Remedio"("idRemedio"),
  CONSTRAINT "fk_Animal_Remedio_Medida1"
    FOREIGN KEY("Medida_idMedida")
    REFERENCES "Medida"("idMedida")
);
CREATE INDEX  "Animal_Remedio.fk_Vaca_remedio_Vaca1_idx" ON "Animal_Remedio" ("Animal_idAnimal");
CREATE INDEX  "Animal_Remedio.fk_Vaca_remedio_Remedio1_idx" ON "Animal_Remedio" ("Remedio_idRemedio");
CREATE INDEX  "Animal_Remedio.fk_Animal_Remedio_Medida1_idx" ON "Animal_Remedio" ("Medida_idMedida");
CREATE TABLE  "Telefone"(
  "idTelefone" INTEGER PRIMARY KEY NOT NULL,
  "Pessoa_idPessoa" INTEGER NOT NULL,
  "numero" VARCHAR(14) NOT NULL,
  CONSTRAINT "fk_Telefone_Pessoa1"
    FOREIGN KEY("Pessoa_idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX  "Telefone.fk_Telefone_Pessoa1_idx" ON "Telefone" ("Pessoa_idPessoa");
CREATE TABLE  "Animal_Enfermidade"(
  "idAnimal_Enfermidade" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimal" VARCHAR(10) NOT NULL,
  "Enfermidade_idEnfermidade" INTEGER NOT NULL,
  "Pessoa_idPessoa" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  CONSTRAINT "fk_Vaca_Enfermidade_Vaca1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Vaca_Enfermidade_Enfermidade1"
    FOREIGN KEY("Enfermidade_idEnfermidade")
    REFERENCES "Enfermidade"("idEnfermidade"),
  CONSTRAINT "fk_Animal_Enfermidade_Pessoa1"
    FOREIGN KEY("Pessoa_idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX  "Animal_Enfermidade.fk_Vaca_Enfermidade_Vaca1_idx" ON "Animal_Enfermidade" ("Animal_idAnimal");
CREATE INDEX  "Animal_Enfermidade.fk_Vaca_Enfermidade_Enfermidade1_idx" ON "Animal_Enfermidade" ("Enfermidade_idEnfermidade");
CREATE INDEX  "Animal_Enfermidade.fk_Animal_Enfermidade_Pessoa1_idx" ON "Animal_Enfermidade" ("Pessoa_idPessoa");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (1, "Frisio Uman Cassandra", 1, "012");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(1,1,"2018-08-12");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(1,1,"2018-02-27",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (2, "Djani Garrett Copel",1,"336");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(2,2,"2018-09-13");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(2,2,"2018-04-21", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (3,"Frisio Super Kate",1,"004");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(3,3,"2017-12-05");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(3,3,"2018-09-10",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (4,"Frisio Dream Chique", 1, "041");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(4,4,"2018-05-15");



INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (5,"Frisio Yano Celeste", 1, "038");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(5,5,"2018-03-23");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (6,"Frisio Soprano Fibria", 1, "037");



INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (7,"Frisio Cabriolet Natasja",1, "36");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(6,7,"2017-10-14");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (8,"Frisio Cabriolet Natalie",1, "34");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(7,8,"2018-11-20");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (9,"Frisio Yano Upload", 1, "0032");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(8,9,"2017-05-18");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(4,9,"2018-02-19",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (10, "Frisio Soprano Umikaze", 1, "031");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(9,10,"2018-04-19");



INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (11,"Frisio Cabriolet Jessy",1,"029");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(83,11,"2018-08-14");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(5,11,"2017-10-09",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (12,"Frisio Cabriolet Caixa",1, "028");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(10,12,"2018-06-10");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(6,12,"2017-11-27",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (13,"Frisio Silver Ritalina",1,"0027");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(11,13,"2018-05-18");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(7,13,"2016-09-09",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (14,"Frisio Cabriolet Johnson",1,"025");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(12,14,"2018-05-18");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(8,14,"2017-10-15",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (15,"Frisio Silver Sentra",1,"022");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(13,15,"2018-06-10");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(9,15,"2018-03-10",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (16,"Frisio Dorcy Cassis", 1, "021");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(14,16,"2018-07-07");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(10,16,"2017-10-09",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (17,"Frisio Yano Garcia", 1, "020");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(15,17,"2018-06-16");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(11,17,"2018-03-21",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (18,"Frisio Yano Celeste", 1, "019");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(16,18,"2017-12-25");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(12,18,"2017-09-03",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (19, "Frisio Uman Condessa", 1, "018");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(17,19,"2018-03-09");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(13,19,"2017-08-24",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (20,"Frisio Yano Conquest", 1, "17");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(18,20,"2018-03-08");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(14,20,"2017-12-29", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (21,"Frisio Freddie Grazer", 1, "014");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(19,21,"2017-11-26");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(15,21,"2017-06-18", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (22,"Frisio Uman Uvaranas",1,"013");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(20,22,"2016-05-10");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(16,22,"2017-02-21",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (23, "Frisio Freddie Zope", 1, "011");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(21,23,"2018-01-30");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(17,23,"2017-07-14",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (24,"Frisio Freddie Chique",1,"010");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(22,24,"2016-03-11");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(18,24,"2016-12-03",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (25,"Frisio Freddie Beckett", 1, "008");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(23,25,"2016-10-18");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(19,25,"2017-07-23",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (26,"Frisio Super Castle",1,"007");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(24,26,"2017-10-12");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(20,26,"2017-07-16",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (27,"Frisio Yano Uke", 1, "033");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(25,27,"2018-02-27");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (28, "Frisio Soprano Natalie", 1, "030");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(84,28,"2018-07-13");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(21,28,"2018-02-21",2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (29,"Frisio Freddie Gabi", 1 , "006");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(26,29,"2017-10-12");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(22,29,"2018-07-08",2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (30,"Frisio Mercury Gabi", 1, "40");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(27,30,"2018-08-02");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (31,"Frisio Yano Copel", 1, "023");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(28,31,"2018-07-23");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(23,31,"2017-11-29",1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (32, "Frisio Cabriolet Cereja", 1, "026");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(29,32, "2018-07-25");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(24,32,"2018-04-06", 1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (33,"Djani Freddie Garcia",1, "332");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(30,33,"2017-08-11");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(25,33,"2018-05-17",1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (34,"Frisio Mercury TCC", 1, "35");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(31,34,"2017-11-13");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(26,34,"2018-08-11",5);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (35,"Frisio Soprano Unica", 1, "039");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(32,35,"2018-11-18");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(27,35,"2018-08-13",2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (36,"Frisio Cole Legend",1,"005");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(33,36,"2017-11-08");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(28,36,"2017-07-25",2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (37,"Frisio Dream Bellucci",1,"42");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(85,37,"2018-05-14");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (38,"Frisio Commander Jairo",1,"043");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(86,38,"2018-05-08");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (39, "Frisio MVP Sentra", 1, "044");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(87,39,"2018-06-08");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (40, "Frisio MVP Wilson", 1, "045");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (41, "Frisio MVP Uvaranas", 1, "46");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (42, "Frisio Commander Cassandra", 1, "47");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(34,42,"2018-05-15");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (43, "Frisio Megastar Alpha", 1, "48");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (44, "Frisio Commander Natalie", 1, "49");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (45, "Frisio Dreamweaver Molotov", 1, "50");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (46, "Frisio Commander Zuleide", 1, "051");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (47, "Frisio Megastar Centro 052", 1, "052");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (48, "Frisio MVP Jessy", 1, "53");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (49, "Frisio Cabriolet Cassis", 1, "54");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (50, "Frisio Balisto Copel", 1, "55");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (51, "Frisio Commander Umwelt", 1,"56");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (52, "Frisio Balisto Conquest", 1, "57");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (108, "Frisio Balisto Reset", 1, "58");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (53, "Frisio Commander Cassandra", 1, "59");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (54, "Frisio Balisto Cereja",1,"61");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (55, "Frisio MVP Natasja", 1, "62");


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (56, "Djani Boliver Reset", 1, "057");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(35,56,"2017-03-27");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(29,56,"2017-12-30",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (57, "Djani Ashlar Bellucci", 1, "73");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(36,57,"2017-06-30");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(30,57, "2018-04-13",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (58,"Djani Gerard Zanzul", 1, "201");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(37,58,"2017-09-26");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(31,58,"2018-07-02",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (59,"Djani Winners Umwelt", 1, "204");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(38,59,"2017-03-23");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(32,59,"2017-12-24",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (60,"Djani Gerard Celeste",1,"206");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(39,60,"2017-07-20");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(33,60,"2018-05-08",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (61, "Djani Legend Gramoxone", 1, "208");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(40,61,"2017-10-16");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(34,61,"2016-09-27",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (62, "Djani Cole Calma", 1, "231");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(41,62,"2018-01-30");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(35,62,"2017-09-18",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (63, "Djani Super Special", 1, "248");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(42,63,"2018-03-08");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(36,63,"2014-10-27",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (64,"Djani Super Chique", 1, "259");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(43, 64, "2018-03-12");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(37,64,"2017-05-05",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (65, "Djani Super Jairo", 1, "303");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(44, 65, "2018-04-19");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(38, 65, " 2017-12-01",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (66, "Djani Freddie Usdolar", 1,"335");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(45, 66 ,"2017-03-04");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(39, 66 ,"2018-07-08", 1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (67, "Frisio Garret Natalie", 1, "383");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(46, 67, "2017-11-08");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(40, 67, "2017-07-07",1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (68, "Frisio Garrett Natasja", 1, "384");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(47, 68, "2017-07-12");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(41, 68, "2018-04-19", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (69, "Djani Win Uke", 1, "648");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(48, 69, "2017-08-28");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(42, 69, "2017-06-08", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (70, "Djani Hatley Alpha", 1, "979");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(49, 70, "2017-08-21");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(43, 70, "2018-05-17", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (71, "Frisio Cole Celeste", 1, "003");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(50, 71, "2017-05-08");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(44, 71, "2016-07-24", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (72, "Frisio Focus Unica", 1, "009");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(51, 72, "2016-01-03");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(45, 72, "2017-08-17", 2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (73, "Djani Gerard Uliana", 1, "230");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(52, 73, "2017-12-16");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(46, 73, "2017-03-22", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (74, "Frisio Jacey Ziek", 1, "015");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(53, 74, "2016-10-07");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(47, 74, "2018-07-14",1);



INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (75, "Djani Cole Jade", 1, "346");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(54, 75, "2017-03-17");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(48, 75, "2016-02-11", 2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (76, "Djani Concord Zope", 1, "742");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(55, 76, "2015-08-16");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(49, 76, "2016-05-13", 1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (77, "Djani Boliver Milady", 1, "002");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(56, 77, "2017-06-08");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(50, 77, "2015-02-25", 2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (78, "Frisio Yano Graca", 1, "024");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (79, "Djani Dorne Camelia", 1, "123");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(57, 79, "2016-04-05");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(51, 79, "2015-09-18", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (80, "Djani Potter Borenstein", 1, "919");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(58, 80, "2017-08-02");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(52, 80, "2018-06-25", 4);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (81, "Djani Cole  Sentra", 1, "211");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(59, 81, "2016-04-19");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(53, 81, "2017-01-15",1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (82, "Djani Goldwyn Katrina", 1 ,"024");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(60, 82, "2015-01-16");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(54, 82, "2015-10-17", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (83, "Djani Dorne Uziel", 1, "977");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(61,83,"2015-03-22");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(55, 83, "2015-12-29", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (84, "Djani Allegro Telles", 1, "013");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(62, 84, "2012-10-23");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(56,84, "2014-11-17",2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (85, "Djani Boliver Lidia", 1, "016");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(63, 85, "2016-09-21");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(57, 85, "2016-01-14", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (86, "Djani Kian Quartel", 1, "816");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(64, 86, "2013-08-13");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(58, 86, "2014-07-04", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (87, "Djani Legend Lambari", 1, "216");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(65, 87, "2014-08-20");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(59, 87, "2015-06-01", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (88, "Djani Super Graciosa", 1, "255");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(66, 88, "2015-08-02");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(60, 88, "2015-03-15", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (89, "Djani Gerard Jermina", 1, "139");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(67, 89, "2015-01-26");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(61, 89, "2015-11-01", 1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (90, "Djani Ashlar Kaitlin", 1, "103");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(68, 90, "2016-03-06");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(62, 90, "2015-10-24", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (91, "Djani Boliver Wege", 1, "949");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(69, 91, "2015-01-24");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(63, 91, "2015-11-02", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (92, "Djani Gerard Usiminas", 1, "226");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(70, 92, "2015-04-30");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(64, 92, "2014-04-08", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (93, "Djani Magnetism Cosette", 1, "345");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(71, 93, "2015-10-18");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(65, 93, "2016-09-12", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (94, "Djani Garrett Bematec", 1, "321");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(72, 94, "2016-01-24");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(66, 94, "2015-10-12", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (95, "Djani Dorne Card", 1, "129");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(73, 95, "2014-12-01");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(67, 95, "2015-09-07", 2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (96, "Djani Gerard Bruna", 1, "142");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(74, 96, "2015-02-23");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(68, 96, "2015-11-25", 2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (97, "Djani Freddie Karla", 1, "305");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(75, 97, "2016-01-25");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(69,97,"2015-03-25", 2);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (98, "Djani Apollo Cassandra", 1, "027");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(76, 98, "2015-05-21");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(70, 98, "2014-10-14", 1);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (99, "Djani Apollo Japonsesa", 1, "029");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(77, 99, "2015-01-16");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(71, 99, "2014-07-08", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (100, "Frisio Focus Bellucci", 1, "001");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (101, "Frisio Cole Zuleide", 1, "002");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (102, "Djani Eddie Natalie", 1, "864");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(78, 102, "2015-01-28");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(72,102, "2015-11-02", 1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (103, "Djani Levi Cassis", 1, "262");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(79, 103, "2014-11-10");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(73,103,"2015-08-20",1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (104, "Djani Boliver Meires", 1, "140");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(80, 104, "2014-05-07");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(74, 104, "2015-02-12", 1);

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (105, "Frisio MVP Wilson", 1, "045");

INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (106, "Djani Shampoo Cora", 1, "128");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(81, 106, "2014-01-15");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(75, 106, "2014-12-03", 2);


INSERT INTO "Animal"("idAnimal","nome","ativo","numero") VALUES (107, "Djani Goldwyn Koreana", 1, "015");
INSERT INTO "Inseminacao"("idAnimal_Inseminacao","Animal_idAnimal","data") VALUES(82, 107, "2014-05-17");
INSERT INTO "Parto"("idAnimal_Parto", "Animal_idAnimal", "data", "cria") VALUES(76, 107, "2015-02-16", 1);
COMMIT;
