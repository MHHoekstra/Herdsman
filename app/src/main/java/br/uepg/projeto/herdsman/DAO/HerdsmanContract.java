package br.uepg.projeto.herdsman.DAO;

import android.provider.BaseColumns;

public final class HerdsmanContract {
        private HerdsmanContract() {}

        public static class AnimalEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Animal";
            public static final String COLUMN_NAME_IDANIMAL = "idAnimal";
            public static final String COLUMN_NAME_NUMERO = "numero";
            public static final String COLUMN_NAME_NOME = "nome";
            public static final String COLUMN_NAME_ATIVO = "ativo";
        }

        public static  class PessoaEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Pessoa";
            public static final String COLUMN_NAME_IDPESSOA = "idPessoa";
            public static final String COLUMN_NAME_NOME = "nome";
            public static final String COLUMN_NAME_CPF = "cpf";
            public static final String COLUMN_NAME_RG = "rg";
            public static final String COLUMN_NAME_ATIVO = "ativo";

        }

        public  static class TelefoneEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Telefone";
            public static final String COLUMN_NAME_IDTELEFONE = "idTelefone";
            public static final String COLUMN_NAME_NUMERO = "numero";
            public static final String COLUMN_NAME_PESSOA_IDPESSOA = "Pessoa_idPessoa";
        }

        public static class AdministradorEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Administrador";
            public static final String COLUMN_NAME_IDADMINISTRADOR = "idAdministrador";
            public static final String COLUMN_NAME_PESSOA_IDPESSOA = "Pessoa_idPessoa";
            public static final String COLUMN_NAME_LOGIN = "login";
            public static final String COLUMN_NAME_SENHA = "senha";
            public static final String COLUMN_NAME_ADMIN = "admin";
        }

        public static class EnfermidadeEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Enfermidade";
            public static final String COLUMN_NAME_IDENFERMIDADE = "idEnfermidade";
            public static final String COLUMN_NAME_DESCRICAO = "descricao";
        }

        public static class RemedioEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Remedio";
            public static final String COLUMN_NAME_IDREMEDIO = "idRemedio";
            public static final String COLUMN_NAME_NOME = "nome";
        }


        public static class SinistroEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Animal_Enfermidade";
            public static final String COLUMN_NAME_IDANIMAL_ENFERMIDADE = "idAnimal_Enfermidade";
            public static final String COLUMN_NAME_ANIMAL_IDANIMAL = "Animal_idAnimal";
            public static final String COLUMN_NAME_PESSOA_IDOPESSOA = "Pessoa_idPessoa";
            public static final String COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE = "Enfermidade_idEnfermidade";
            public static final String COLUMN_NAME_DATA = "data";
        }

        public static class MedidaEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Medida";
            public static final String COLUMN_NAME_IDMEDIDA = "idMedida";
            public static final String COLUMN_NAME_NOME = "nome";
        }

        public static class CioEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Cio";
            public static final String COLUMN_NAME_IDANIMAL_CIO = "idAnimal_Cio";
            public static final String COLUMN_NAME_ANIMAL_IDANIMALPORCIMA = "Animal_idAnimalPorCima";
            public static final String COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO = "Animal_idAnimalPorBaixo";
            public static final String COLUMN_NAME_PESSOA_IDPESSOA = "Pessoa_idPessoa";
            public static final String COLUMN_NAME_DATA = "data";
        }

        public static class AnimalInseminacaoEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Inseminacao";
            public static final String COLUMN_NAME_IDANIMAL_INSEMINACAO = "idAnimal_Inseminacao";
            public static final String COLUMN_NAME_ANIMAL_IDANIMAL = "Animal_idAnimal";
            public static final String COLUMN_NAME_DATA = "data";
        }

        public static class AnimalRemedioEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Animal_Remedio";
            public static final String COLUMN_NAME_IDANIMAL_REMEDIO = "idAnimal_Remedio";
            public static final String COLUMN_NAME_ANIMAL_IDANIMAL = "Animal_idAnimal";
            public static final String COLUMN_NAME_REMEDIO_IDREMEDIO = "Remedio_idRemedio";
            public static final String COLUMN_NAME_DATA = "data";
            public static final String COLUMN_NAME_QUANTIDADE = "quantidade";
            public static final String COLUMN_NAME_MEDIDA_IDMEDIDA = "Medida_idMedida";



        }

        public static class PartoEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Parto";
            public static final String COLUMN_NAME_IDPARTO = "idAnimal_Parto";
            public static final String COLUMN_NAME_ANIMAL_IDANIMAL = "Animal_idAnimal";
            public static final String COLUMN_NAME_DATA = "data";
            public static final String COLUMN_NAME_CRIA = "cria";

        }


        public static class AdministradorNotificaPessoaEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Administrador_Notifica_Pessoa";
            public static final String COLUMN_NAME_IDADMINISTRADOR_NOTIFICA_PESSOA = "idAdministrador_Notifica_Pessoa";
            public static final String COLUMN_NAME_ADMINISTRADOR_IDNOTIFICA = "Administrador_idNotifica";
            public static final String COLUMN_NAME_PESSOA_IDNOTIFICADO = "Pessoa_idNotificado";
            public static final String COLUMN_NAME_DATA = "data";
            public static final String COLUMN_NAME_DESCRICAO = "descricao";
        }

}
