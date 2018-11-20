DELIMITER //
CREATE TRIGGER Parto_BEFORE_INSERT BEFORE INSERT ON Parto FOR EACH ROW
BEGIN
IF (SELECT 1 as existe from Parto p where TIMESTAMPDIFF(MONTH,p.data,NEW.data)<8 and p.idAnimal=NEW.idAnimal)=1 THEN
   SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Existe um parto muito recente';
  END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Parto_BEFORE_UPDATE BEFORE UPDATE ON Parto FOR EACH ROW
BEGIN
IF (SELECT 1 as existe from Parto p where TIMESTAMPDIFF(MONTH,p.data,NEW.data)<8 and p.idAnimal=NEW.idAnimal)=1  THEN
   SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Existe um parto muito recente';
  END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Cio_BEFORE_INSERT BEFORE INSERT ON Cio FOR EACH ROW
BEGIN
IF (SELECT 1 as existe from Inseminacao p where TIMESTAMPDIFF(DAY,p.data,NEW.data)<18 and p.idAnimal=NEW.idAnimal)=1  THEN
   INSERT INTO Administrador_Notifica_Pessoa (Administrador_idNotifica,data,descricao)  VALUES ((SELECT idUsuario FROM Usuario p WHERE p.admin = 1) ,NOW(), 'Animal '+NEW.idAnimal+'está com provável problema reprodutivo');
   SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Animal está com provável problema reprodutivo';
  END IF;
IF (SELECT 1 as existe from Inseminacao p where TIMESTAMPDIFF(DAY,p.data,NEW.data)>18 and TIMESTAMPDIFF(DAY,p.data,NEW.data)<60 and p.idAnimal=NEW.idAnimal)=1  THEN
   INSERT INTO Administrador_Notifica_Pessoa (Administrador_idNotifica,data,descricao)  VALUES ((SELECT idUsuario FROM Usuario p WHERE p.admin = 1) ,NOW(), 'Animal '+NEW.idAnimal+'necessita inseminação');
   SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Animal necessita inseminação';
  END IF;
IF (SELECT 1 as existe from Inseminacao p where TIMESTAMPDIFF(DAY,p.data,NEW.data)>60 and p.idAnimal=NEW.idAnimal)=1  THEN
   INSERT INTO Administrador_Notifica_Pessoa (Administrador_idNotifica,data,descricao)  VALUES ((SELECT idUsuario FROM Usuario p WHERE p.admin = 1) ,NOW(), 'Animal '+NEW.idAnimal+'necessita inspeção');
   SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Animal necessita inspeção';
  END IF;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER Cio_BEFORE_UPDATE BEFORE UPDATE ON Cio FOR EACH ROW
BEGIN
IF (SELECT 1 as existe from Inseminacao p where TIMESTAMPDIFF(DAY,p.data,NEW.data)<18 and p.idAnimal=NEW.idAnimal)=1  THEN
   INSERT INTO Administrador_Notifica_Pessoa (Administrador_idNotifica,data,descricao)  VALUES ((SELECT idUsuario FROM Usuario p WHERE p.admin = 1) ,NOW(), 'Animal '+NEW.idAnimal+'está com provável problema reprodutivo');
   SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Animal está com provável problema reprodutivo';
  END IF;
IF (SELECT 1 as existe from Inseminacao p where TIMESTAMPDIFF(DAY,p.data,NEW.data)>18 and TIMESTAMPDIFF(DAY,p.data,NEW.data)<60 and p.idAnimal=NEW.idAnimal)=1  THEN
   INSERT INTO Administrador_Notifica_Pessoa (Administrador_idNotifica,data,descricao)  VALUES ((SELECT idUsuario FROM Usuario p WHERE p.admin = 1) ,NOW(), 'Animal '+NEW.idAnimal+'necessita inseminação');
   SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Animal necessita inseminação';
  END IF;
IF (SELECT 1 as existe from Inseminacao p where TIMESTAMPDIFF(DAY,p.data,NEW.data)>60 and p.idAnimal=NEW.idAnimal)=1  THEN
   INSERT INTO Administrador_Notifica_Pessoa (Administrador_idNotifica,data,descricao)  VALUES ((SELECT idUsuario FROM Usuario p WHERE p.admin = 1) ,NOW(), 'Animal '+NEW.idAnimal+'necessita inspeção');
   SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = 'Animal necessita inspeção';
  END IF;
END//
DELIMITER ;

ALTER TABLE Remedio MODIFY nome VARCHAR(45) NOT NULL UNIQUE;
ALTER TABLE Medida MODIFY nome VARCHAR(45) NOT NULL UNIQUE;
ALTER TABLE Enfermidade MODIFY descricao VARCHAR(45) NOT NULL UNIQUE;
ALTER TABLE Telefone MODIFY numero VARCHAR(45) NOT NULL UNIQUE;

ALTER TABLE Parto ADD CONSTRAINT check_cria CHECK (cria in(1, 2, 3, 4,5));

DELIMITER //
CREATE TRIGGER valida_pessoa_insert BEFORE INSERT ON Pessoa
FOR EACH ROW
BEGIN
IF (SELECT valida_cpf(new.cpf))=0
THEN
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'CPF inválido!';
END IF;
END
DELIMITER ;

SET GLOBAL log_bin_trust_function_creators = 1;
DELIMITER //
CREATE FUNCTION `valida_cpf`(CPF CHAR(14)) RETURNS double
BEGIN
    DECLARE INDICE INT;
    DECLARE SOMA INT;
    DECLARE DIG1 INT;
    DECLARE DIG2 INT;
    DECLARE CPF_TEMP VARCHAR(11);
    DECLARE DIGITOS_IGUAIS CHAR(1);
    DECLARE RESULTADO CHAR(1);

    SET RESULTADO = FALSE;

    /*
    Verificando se os dígitos são iguais
    */

    SET CPF_TEMP = SUBSTRING(CPF,1,1);

    SET INDICE = 1;
    SET DIGITOS_IGUAIS = 'S';

  IF (LENGTH(CPF)>11) THEN
    SET DIGITOS_IGUAIS = 'S';
  ELSE
    SET DIG1=LENGTH(CPF);
    WHILE (DIG1<=12) DO
      SET CPF=CONCAT("0",CPF);
      SET DIG1=DIG1+1;
    END WHILE;
    SET CPF=RIGHT(CPF,11);
      WHILE (INDICE <= 11) DO
          IF (SUBSTRING(CPF,INDICE,1) <> CPF_TEMP) Then
              SET DIGITOS_IGUAIS = 'N';
          END IF;
          SET INDICE = INDICE + 1;
      END WHILE;
  END IF;

    /*Caso os dígitos não sejam todos iguais Começo o calculo do dígitos*/
    IF (DIGITOS_IGUAIS = 'N') THEN
        /*Cálculo do 1º dígito*/
        SET SOMA = 0;
        SET INDICE = 1;
        WHILE (INDICE <= 9) DO
            SET Soma = Soma + CAST(SUBSTRING(CPF,INDICE,1) AS UNSIGNED) * (11 - INDICE);             SET INDICE = INDICE + 1;
         END WHILE;
         SET DIG1 = 11 - (SOMA % 11);
         IF (DIG1 > 9) THEN
            SET DIG1 = 0;
         END IF;

        -- Cálculo do 2º dígito }
        SET SOMA = 0;
        SET INDICE = 1;
        WHILE (INDICE <= 10) DO
             SET Soma = Soma + CAST(SUBSTRING(CPF,INDICE,1) AS UNSIGNED) * (12 - INDICE);              SET INDICE = INDICE + 1;
        END WHILE;
        SET DIG2 = 11 - (SOMA % 11);
        IF DIG2 > 9 THEN
            SET DIG2 = 0;
        END IF;

        -- Validando
        IF (DIG1 = SUBSTRING(CPF,LENGTH(CPF)-1,1)) AND (DIG2 = SUBSTRING(CPF,LENGTH(CPF),1)) THEN
            SET RESULTADO = TRUE;
        ELSE
            SET RESULTADO = FALSE;
        END IF;

    END IF;
    RETURN RESULTADO;
END//
