
PRAGMA foreign_keys = OFF;
BEGIN;
CREATE TABLE "Remedio"(
  "idRemedio" INTEGER PRIMARY KEY NOT NULL,
  "nome" VARCHAR(45) NOT NULL,
  "descricao" VARCHAR(100)
);
CREATE TABLE "Enfermidade"(
  "idEnfermidade" INTEGER PRIMARY KEY NOT NULL,
  "descricao" VARCHAR(100) NOT NULL
);
CREATE TABLE "Animal"(
  "idAnimal" INTEGER PRIMARY KEY NOT NULL,
  "nome" VARCHAR(45) NOT NULL,
  "numero" VARCHAR(45) NOT NULL
);
CREATE TABLE "Pessoa"(
  "idPessoa" INTEGER PRIMARY KEY NOT NULL,
  "cpf" VARCHAR(45) NOT NULL,
  "nome" VARCHAR(45) NOT NULL,
  "sobrenome" VARCHAR(45),
  "rg" VARCHAR(45)
);
CREATE TABLE "Animal_Parto"(
  "idAnimal_Parto" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimal" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "femea" BOOLEAN NOT NULL,
  CONSTRAINT "fk_Animal_Parto_Animal1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX "Animal_Parto.fk_Animal_Parto_Animal1_idx" ON "Animal_Parto" ("Animal_idAnimal");
CREATE TABLE "Animal_Enfermidade"(
  "idAnimal_Enfermidade" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimal" INTEGER NOT NULL,
  "Enfermidade_idEnfermidade" INTEGER NOT NULL,
  "data" DATE,
  CONSTRAINT "fk_Vaca_Enfermidade_Vaca1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Vaca_Enfermidade_Enfermidade1"
    FOREIGN KEY("Enfermidade_idEnfermidade")
    REFERENCES "Enfermidade"("idEnfermidade")
);
CREATE INDEX "Animal_Enfermidade.fk_Vaca_Enfermidade_Vaca1_idx" ON "Animal_Enfermidade" ("Animal_idAnimal");
CREATE INDEX "Animal_Enfermidade.fk_Vaca_Enfermidade_Enfermidade1_idx" ON "Animal_Enfermidade" ("Enfermidade_idEnfermidade");
CREATE TABLE "Telefone"(
  "idTelefone" INTEGER PRIMARY KEY NOT NULL,
  "Pessoa_idPessoa" INTEGER NOT NULL,
  "numero" VARCHAR(14),
  CONSTRAINT "fk_Telefone_Pessoa1"
    FOREIGN KEY("Pessoa_idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX "Telefone.fk_Telefone_Pessoa1_idx" ON "Telefone" ("Pessoa_idPessoa");
CREATE TABLE "Usuario"(
  "idUsuario" INTEGER PRIMARY KEY NOT NULL,
  "Pessoa_idPessoa" INTEGER NOT NULL,
  "login" VARCHAR(45) NOT NULL,
  "senha" VARCHAR(45) NOT NULL,
  "admin" BOOLEAN NOT NULL,
  CONSTRAINT "fk_Funcionario_Pessoa1"
    FOREIGN KEY("Pessoa_idPessoa")
    REFERENCES "Pessoa"("idPessoa")
);
CREATE INDEX "Usuario.fk_Funcionario_Pessoa1_idx" ON "Usuario" ("Pessoa_idPessoa");
CREATE TABLE "Animal_Inseminacao"(
  "idAnimal_Inseminacao" INTEGER PRIMARY KEY NOT NULL,
  "Inseminacao_idInseminacao" INTEGER NOT NULL,
  "Animal_idAnimal" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  CONSTRAINT "fk_Animal_Inseminacao_Animal1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX "Animal_Inseminacao.fk_Animal_Inseminacao_Animal1_idx" ON "Animal_Inseminacao" ("Animal_idAnimal");
CREATE TABLE "Notifica_Enfermidade"(
  "idNotifica_Enfermidade" INTEGER PRIMARY KEY NOT NULL,
  "Usuario_idUsuario" INTEGER NOT NULL,
  "Enfermidade_idEnfermidade" INTEGER NOT NULL,
  "data" DATE,
  CONSTRAINT "fk_Notifica_Enfermidade_Usuario1"
    FOREIGN KEY("Usuario_idUsuario")
    REFERENCES "Usuario"("idUsuario"),
  CONSTRAINT "fk_Notifica_Enfermidade_Enfermidade1"
    FOREIGN KEY("Enfermidade_idEnfermidade")
    REFERENCES "Enfermidade"("idEnfermidade")
);
CREATE INDEX "Notifica_Enfermidade.fk_Notifica_Enfermidade_Usuario1_idx" ON "Notifica_Enfermidade" ("Usuario_idUsuario");
CREATE INDEX "Notifica_Enfermidade.fk_Notifica_Enfermidade_Enfermidade1_idx" ON "Notifica_Enfermidade" ("Enfermidade_idEnfermidade");
CREATE TABLE "Admin_Notifica_Usuario"(
  "idAdmin_Notifica_Usuario" INTEGER PRIMARY KEY,
  "Usuario_idNotifica" INTEGER NOT NULL,
  "Usuario_idNotificado" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "descricao" VARCHAR(100),
  CONSTRAINT "fk_Admin_Notifica_Usuario_Usuario1"
    FOREIGN KEY("Usuario_idNotifica")
    REFERENCES "Usuario"("idUsuario"),
  CONSTRAINT "fk_Admin_Notifica_Usuario_Usuario2"
    FOREIGN KEY("Usuario_idNotificado")
    REFERENCES "Usuario"("idUsuario")
);
CREATE INDEX "Admin_Notifica_Usuario.fk_Admin_Notifica_Usuario_Usuario1_idx" ON "Admin_Notifica_Usuario" ("Usuario_idNotifica");
CREATE INDEX "Admin_Notifica_Usuario.fk_Admin_Notifica_Usuario_Usuario2_idx" ON "Admin_Notifica_Usuario" ("Usuario_idNotificado");
CREATE TABLE "Notifica_Cio"(
  "idNotificacao" INTEGER PRIMARY KEY NOT NULL,
  "Usuario_idUsuario" INTEGER NOT NULL,
  "data" DATE,
  "descricao" VARCHAR(100),
  CONSTRAINT "fk_Notifica_Cio_Usuario1"
    FOREIGN KEY("Usuario_idUsuario")
    REFERENCES "Usuario"("idUsuario")
);
CREATE INDEX "Notifica_Cio.fk_Notifica_Cio_Usuario1_idx" ON "Notifica_Cio" ("Usuario_idUsuario");
CREATE TABLE "Animal_Remedio"(
  "idAnimal_Remedio" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimal" INTEGER NOT NULL,
  "Remedio_idRemedio" INTEGER NOT NULL,
  "data" DATE,
  "quantidade" INTEGER,
  CONSTRAINT "fk_Vaca_remedio_Vaca1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Vaca_remedio_Remedio1"
    FOREIGN KEY("Remedio_idRemedio")
    REFERENCES "Remedio"("idRemedio")
);
CREATE INDEX "Animal_Remedio.fk_Vaca_remedio_Vaca1_idx" ON "Animal_Remedio" ("Animal_idAnimal");
CREATE INDEX "Animal_Remedio.fk_Vaca_remedio_Remedio1_idx" ON "Animal_Remedio" ("Remedio_idRemedio");
CREATE TABLE "Notifica"(
  "idNotifica" INTEGER PRIMARY KEY NOT NULL,
  "Usuario_idUsuario" INTEGER NOT NULL,
  "Animal_idAnimal" INTEGER NOT NULL,
  "tipo" INTEGER NOT NULL,
  "data" DATE NOT NULL,
  "descricao" VARCHAR(100),
  CONSTRAINT "fk_Notifica_Usuario1"
    FOREIGN KEY("Usuario_idUsuario")
    REFERENCES "Usuario"("idUsuario"),
  CONSTRAINT "fk_Notifica_Animal1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal")
);
CREATE INDEX "Notifica.fk_Notifica_Usuario1_idx" ON "Notifica" ("Usuario_idUsuario");
CREATE INDEX "Notifica.fk_Notifica_Animal1_idx" ON "Notifica" ("Animal_idAnimal");
CREATE TABLE "Animal_Cio"(
  "idAnimal_Cio" INTEGER PRIMARY KEY NOT NULL,
  "Animal_idAnimal" INTEGER NOT NULL,
  "Notifica_Cio_idNotificacao" INTEGER NOT NULL,
  "data" DATE,
  CONSTRAINT "fk_Vaca_Cio_Vaca1"
    FOREIGN KEY("Animal_idAnimal")
    REFERENCES "Animal"("idAnimal"),
  CONSTRAINT "fk_Animal_Cio_Notifica_Cio1"
    FOREIGN KEY("Notifica_Cio_idNotificacao")
    REFERENCES "Notifica_Cio"("idNotificacao")
);
CREATE INDEX "Animal_Cio.fk_Vaca_Cio_Vaca1_idx" ON "Animal_Cio" ("Animal_idAnimal");
CREATE INDEX "Animal_Cio.fk_Animal_Cio_Notifica_Cio1_idx" ON "Animal_Cio" ("Notifica_Cio_idNotificacao");
COMMIT;
