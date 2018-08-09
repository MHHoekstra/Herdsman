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

        public static class UsuarioEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Usuario";
            public static final String COLUMN_NAME_IDUSUARIO = "idUsuario";
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
            public static final String COLUMN_NAME_DESCRICAO = "descricao";
        }


        public static class AnimalEnfermidadeEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Animal_Enfermidade";
            public static final String COLUMN_NAME_IDANIMAL_ENFERMIDADE = "idAnimal_Enfermidade";
            public static final String COLUMN_NAME_ANIMAL_IDANIMAL = "Animal_idAnimal";
            public static final String COLUMN_NAME_USUARIO_IDUSUARIO = "Usuario_idUsuario";
            public static final String COLUMN_NAME_ENFERMIDADE_IDENFERMIDADE = "Enfermidade_idEnfermidade";
            public static final String COLUMN_NAME_DATA = "data";
        }

        public static class CioEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Cio";
            public static final String COLUMN_NAME_IDANIMAL_CIO = "idAnimal_Cio";
            public static final String COLUMN_NAME_ANIMAL_IDANIMALPORCIMA = "Animal_idAnimalPorCima";
            public static final String COLUMN_NAME_ANIMAL_IDANIMALPORBAIXO = "Animal_idAnimalPorBaixo";
            public static final String COLUMN_NAME_USUARIO_IDUSUARIO = "Usuario_idUsuario";
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

        }

        public static class PartoEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Parto";
            public static final String COLUMN_NAME_IDPARTO = "idAnimal_Parto";
            public static final String COLUMN_NAME_ANIMAL_IDANIMAL = "Animal_idAnimal";
            public static final String COLUMN_NAME_DATA = "data";
            public static final String COLUMN_NAME_CRIA = "cria";

        }


        public static class UsuarioNotificaUsuarioEntry implements BaseColumns
        {
            public static final String TABLE_NAME = "Admin_Notifica_Usuario";
            public static final String COLUMN_NAME_IDADMIN_NOTIFICA_USUARIO = "idAdmin_Notifica_Usuario";
            public static final String COLUMN_NAME_USUARIO_IDNOTIFICA = "Usuario_idNotifica";
            public static final String COLUMN_NAME_USUARIO_IDNOTIFICADO = "Usuario_idNotificado";
            public static final String COLUMN_NAME_DATA = "data";
            public static final String COLUMN_NAME_DESCRICAO = "descricao";
        }

}
