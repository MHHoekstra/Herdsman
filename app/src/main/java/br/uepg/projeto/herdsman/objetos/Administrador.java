package br.uepg.projeto.herdsman.objetos;

import java.io.Serializable;

public class Administrador implements Serializable {

    private long idAdministrador;
    private String login;
    private String senha;
    private long idPessoa;
    private int admin;

    public Administrador()
    {

    }
    public long getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(long idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public Administrador(int admin, String login, String senha, long idPessoa, long idUsuario) {
        this.login = login;
        this.senha = senha;
        this.idPessoa = idPessoa;
        this.idAdministrador = idUsuario;
    }

    public int isAdmin() {
        return admin;
    }

    public long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Administrador(String login, String senha, long idPessoa) {
        this.admin = 1;
        this.login = login;
        this.senha = senha;
        this.idPessoa = idPessoa;
    }

    public Administrador(boolean admin, String login, String senha) {
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
