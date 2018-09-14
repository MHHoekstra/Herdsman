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
CREATE TABLE  "Usuario"(
  "idUsuario" INTEGER PRIMARY KEY NOT NULL,
  "Pessoa_idPessoa" INTEGER NOT NULL,
  "login" VARCHAR(45) NOT NULL,
  "senha" VARCHAR(45) NOT NULL,
  "admin" BOOLEAN NOT NULL,
  CONSTRAINT "fk_Funcionario_Pessoa1"
    FOREIGN KEY("Pessoa_idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX  "Usuario.fk_Funcionario_Pessoa1_idx" ON "Usuario" ("Pessoa_idPessoa");
INSERT INTO "Usuario"("idUsuario","Pessoa_idPessoa","login","senha","admin") VALUES(1, 1, 'admin', '5050', 1);
CREATE TABLE  "Usuario_Notifica_Usuario"(
  "idAdmin_Notifica_Usuario" INTEGER PRIMARY KEY,
  "Usuario_idNotificado" INTEGER NOT NULL,
  "Usuario_idNotifica" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "descricao" VARCHAR(100) NOT NULL,
  CONSTRAINT "fk_Admin_Notifica_Usuario_Usuario2"
    FOREIGN KEY("Usuario_idNotificado")
    REFERENCES "Usuario"("idUsuario"),
  CONSTRAINT "fk_Admin_Notifica_Usuario_Usuario1"
    FOREIGN KEY("Usuario_idNotifica")
    REFERENCES "Usuario"("idUsuario")
);
CREATE INDEX  "Usuario_Notifica_Usuario.fk_Admin_Notifica_Usuario_Usuario2_idx" ON "Usuario_Notifica_Usuario" ("Usuario_idNotificado");
CREATE INDEX  "Usuario_Notifica_Usuario.fk_Admin_Notifica_Usuario_Usuario1_idx" ON "Usuario_Notifica_Usuario" ("Usuario_idNotifica");
CREATE TABLE  "Cio"(
  "idAnimal_Cio" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimalPorCima" VARCHAR(10) NOT NULL,
  "Animal_idAnimalPorBaixo" VARCHAR(10) NOT NULL,
  "Usuario_idUsuario" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  CONSTRAINT "fk_Animal_Cio_Animal1"
    FOREIGN KEY("Animal_idAnimalPorCima")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Animal_Cio_Usuario1"
    FOREIGN KEY("Usuario_idUsuario")
    REFERENCES "Usuario"("idUsuario"),
  CONSTRAINT "fk_Animal_Cio_Animal2"
    FOREIGN KEY("Animal_idAnimalPorBaixo")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX  "Cio.fk_Animal_Cio_Animal1_idx" ON "Cio" ("Animal_idAnimalPorCima");
CREATE INDEX  "Cio.fk_Animal_Cio_Usuario1_idx" ON "Cio" ("Usuario_idUsuario");
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
  "Usuario_idUsuario" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  CONSTRAINT "fk_Vaca_Enfermidade_Vaca1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Vaca_Enfermidade_Enfermidade1"
    FOREIGN KEY("Enfermidade_idEnfermidade")
    REFERENCES "Enfermidade"("idEnfermidade"),
  CONSTRAINT "fk_Animal_Enfermidade_Usuario1"
    FOREIGN KEY("Usuario_idUsuario")
    REFERENCES "Usuario"("idUsuario")
);
CREATE INDEX  "Animal_Enfermidade.fk_Vaca_Enfermidade_Vaca1_idx" ON "Animal_Enfermidade" ("Animal_idAnimal");
CREATE INDEX  "Animal_Enfermidade.fk_Vaca_Enfermidade_Enfermidade1_idx" ON "Animal_Enfermidade" ("Enfermidade_idEnfermidade");
CREATE INDEX  "Animal_Enfermidade.fk_Animal_Enfermidade_Usuario1_idx" ON "Animal_Enfermidade" ("Usuario_idUsuario");
COMMIT;
