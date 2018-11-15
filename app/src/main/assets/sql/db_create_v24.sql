-- Creator:       MySQL Workbench 8.0.12/ExportSQLite Plugin 0.1.0
-- Author:        pulma
-- Caption:       New Model
-- Project:       Name of the project
-- Changed:       2018-09-16 22:19
-- Created:       2018-03-20 10:27
PRAGMA foreign_keys = OFF;

-- Schema: mydb
BEGIN;
CREATE TABLE "mydb"."Remedio"(
  "idRemedio" INTEGER PRIMARY KEY NOT NULL,
  "nome" VARCHAR(45) NOT NULL
);
CREATE TABLE "mydb"."Animal"(
  "idAnimal" INTEGER PRIMARY KEY NOT NULL,
  "numero" VARCHAR(10) NOT NULL,
  "nome" VARCHAR(45) NOT NULL,
  "ativo" BOOLEAN NOT NULL
);
CREATE TABLE "mydb"."Enfermidade"(
  "idEnfermidade" INTEGER PRIMARY KEY NOT NULL,
  "descricao" VARCHAR(100) NOT NULL
);
CREATE TABLE "mydb"."Parto"(
  "idParto" INTEGER NOT NULL,
  "idAnimal" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "cria" INTEGER NOT NULL,
  PRIMARY KEY("idParto","idAnimal"),
  CONSTRAINT "fk_Animal_Parto_Animal1"
    FOREIGN KEY("idAnimal")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX "mydb"."Parto.fk_Animal_Parto_Animal1_idx" ON "Parto" ("idAnimal");
CREATE TABLE "mydb"."Inseminacao"(
  "idInseminacao" INTEGER NOT NULL,
  "idAnimal" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  PRIMARY KEY("idInseminacao","idAnimal"),
  CONSTRAINT "fk_Animal_Inseminacao_Animal1"
    FOREIGN KEY("idAnimal")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX "mydb"."Inseminacao.fk_Animal_Inseminacao_Animal1_idx" ON "Inseminacao" ("idAnimal");
CREATE TABLE "mydb"."Pessoa"(
  "idPessoa" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "nome" VARCHAR(45) NOT NULL,
  "cpf" VARCHAR(11) NOT NULL,
  "rg" VARCHAR(14) NOT NULL
);
CREATE TABLE "mydb"."Medida"(
  "idMedida" INTEGER PRIMARY KEY NOT NULL,
  "nome" VARCHAR(45) NOT NULL
);
CREATE TABLE "mydb"."Animal_Enfermidade"(
  "idAnimal_Enfermidade" INTEGER NOT NULL,
  "idAnimal" INTEGER NOT NULL,
  "idEnfermidade" INTEGER NOT NULL,
  "idPessoa" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  PRIMARY KEY("idAnimal_Enfermidade","idAnimal","idEnfermidade","idPessoa"),
  CONSTRAINT "fk_Vaca_Enfermidade_Vaca1"
    FOREIGN KEY("idAnimal")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Vaca_Enfermidade_Enfermidade1"
    FOREIGN KEY("idEnfermidade")
    REFERENCES "Enfermidade"("idEnfermidade"),
  CONSTRAINT "fk_Animal_Enfermidade_Pessoa1"
    FOREIGN KEY("idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX "mydb"."Animal_Enfermidade.fk_Vaca_Enfermidade_Vaca1_idx" ON "Animal_Enfermidade" ("idAnimal");
CREATE INDEX "mydb"."Animal_Enfermidade.fk_Vaca_Enfermidade_Enfermidade1_idx" ON "Animal_Enfermidade" ("idEnfermidade");
CREATE INDEX "mydb"."Animal_Enfermidade.fk_Animal_Enfermidade_Pessoa1_idx" ON "Animal_Enfermidade" ("idPessoa");
CREATE TABLE "mydb"."Usuario"(
  "idUsuario" INTEGER NOT NULL,
  "idPessoa" INTEGER NOT NULL,
  "login" VARCHAR(45) NOT NULL,
  "senha" VARCHAR(45) NOT NULL,
  PRIMARY KEY("idUsuario","idPessoa"),
  CONSTRAINT "fk_Funcionario_Pessoa1"
    FOREIGN KEY("idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX "mydb"."Usuario.fk_Funcionario_Pessoa1_idx" ON "Usuario" ("idPessoa");
CREATE TABLE "mydb"."Cio"(
  "idCio" INTEGER NOT NULL,
  "idAnimalPorCima" INTEGER NOT NULL,
  "idAnimalPorBaixo" INTEGER NOT NULL,
  "idPessoa" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  PRIMARY KEY("idCio","idAnimalPorCima","idAnimalPorBaixo","idPessoa"),
  CONSTRAINT "fk_Animal_Cio_Animal2"
    FOREIGN KEY("idAnimalPorBaixo")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Cio_Pessoa1"
    FOREIGN KEY("idPessoa")
    REFERENCES "Pessoa"("idPessoa"),
  CONSTRAINT "fk_Cio_Animal1"
    FOREIGN KEY("idAnimalPorCima")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX "mydb"."Cio.fk_Animal_Cio_Animal2_idx" ON "Cio" ("idAnimalPorBaixo");
CREATE INDEX "mydb"."Cio.fk_Cio_Pessoa1_idx" ON "Cio" ("idPessoa");
CREATE INDEX "mydb"."Cio.fk_Cio_Animal1_idx" ON "Cio" ("idAnimalPorCima");
CREATE TABLE "mydb"."Administrador"(
  "idAdministrador" INTEGER NOT NULL,
  "Pessoa_idPessoa" INTEGER NOT NULL,
  "login" VARCHAR(45) NOT NULL,
  "senha" VARCHAR(45) NOT NULL,
  PRIMARY KEY("idAdministrador","Pessoa_idPessoa"),
  CONSTRAINT "fk_Administrador_Pessoa1"
    FOREIGN KEY("Pessoa_idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX "mydb"."Administrador.fk_Administrador_Pessoa1_idx" ON "Administrador" ("Pessoa_idPessoa");
CREATE TABLE "mydb"."Animal_Remedio"(
  "idAnimal_Remedio" INTEGER NOT NULL,
  "idAnimal" INTEGER NOT NULL,
  "idRemedio" INTEGER NOT NULL,
  "idMedida" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "quantidade" INTEGER NOT NULL,
  PRIMARY KEY("idAnimal_Remedio","idAnimal","idMedida","idRemedio"),
  CONSTRAINT "fk_Vaca_remedio_Vaca1"
    FOREIGN KEY("idAnimal")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Vaca_remedio_Remedio1"
    FOREIGN KEY("idRemedio")
    REFERENCES "Remedio"("idRemedio"),
  CONSTRAINT "fk_Animal_Remedio_Medida1"
    FOREIGN KEY("idMedida")
    REFERENCES "Medida"("idMedida")
);
CREATE INDEX "mydb"."Animal_Remedio.fk_Vaca_remedio_Vaca1_idx" ON "Animal_Remedio" ("idAnimal");
CREATE INDEX "mydb"."Animal_Remedio.fk_Vaca_remedio_Remedio1_idx" ON "Animal_Remedio" ("idRemedio");
CREATE INDEX "mydb"."Animal_Remedio.fk_Animal_Remedio_Medida1_idx" ON "Animal_Remedio" ("idMedida");
CREATE TABLE "mydb"."Telefone"(
  "idTelefone" INTEGER NOT NULL,
  "idPessoa" INTEGER NOT NULL,
  "numero" VARCHAR(14) NOT NULL,
  PRIMARY KEY("idTelefone","idPessoa"),
  CONSTRAINT "fk_Telefone_Pessoa1"
    FOREIGN KEY("idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX "mydb"."Telefone.fk_Telefone_Pessoa1_idx" ON "Telefone" ("idPessoa");
CREATE TABLE "mydb"."Administrador_Notifica_Pessoa"(
  "idAdministrador_Notifica_Pessoa" INTEGER,
  "idAdministrador" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "descricao" VARCHAR(100) NOT NULL,
  PRIMARY KEY("idAdministrador_Notifica_Pessoa","idAdministrador"),
  CONSTRAINT "fk_Administrador_Notifica_Pessoa_Administrador1"
    FOREIGN KEY("idAdministrador")
    REFERENCES "Administrador"("idAdministrador")
);
CREATE INDEX "mydb"."Administrador_Notifica_Pessoa.fk_Administrador_Notifica_Pessoa_Administrador1_idx" ON "Administrador_Notifica_Pessoa" ("idAdministrador");

INSERT INTO "Medida"("idMedida","nome") VALUES(1, "Miligramas");
INSERT INTO "Administrador"("idAdministrador","Pessoa_idPessoa","login","senha","admin") VALUES(1, 1, 'admin', '5050', 1);
INSERT INTO "Pessoa"("idPessoa","nome","cpf","rg") VALUES(1, 'Administrador', '08491584943', '124600081');

