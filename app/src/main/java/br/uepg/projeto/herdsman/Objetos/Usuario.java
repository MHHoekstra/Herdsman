package br.uepg.projeto.herdsman.Objetos;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int admin;
    private String login;
    private String senha;
    private int idPessoa;
    private int idUsuario;

    public Usuario(int admin, String login, String senha, int idPessoa, int idUsuario) {
        this.admin = admin;
        this.login = login;
        this.senha = senha;
        this.idPessoa = idPessoa;
        this.idUsuario = idUsuario;
    }

    public int isAdmin() {
        return admin;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Usuario(String login, String senha, int idPessoa) {
        this.admin = 0;
        this.login = login;
        this.senha = senha;
        this.idPessoa = idPessoa;
    }

    public Usuario(boolean admin, String login, String senha) {
        this.admin = 0;
        this.login = login;
        this.senha = senha;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
