package br.uepg.projeto.herdsman.objetos;

public class AdministradorNotificaPessoa {
    private int idAdministradorNotificaPessoa;
    private String mensagem;
    private String data;
    private int idAdministrador;

    public AdministradorNotificaPessoa() {
    }

    public AdministradorNotificaPessoa(int idAdministradorNotificaPessoa, String mensagem, String data) {
        this.idAdministradorNotificaPessoa = idAdministradorNotificaPessoa;
        this.mensagem = mensagem;
        this.data = data;
    }

    public AdministradorNotificaPessoa(String mensagem, String data) {
        this.mensagem = mensagem;
        this.data = data;
        idAdministrador = 1;
    }

    public AdministradorNotificaPessoa(int idAdministradorNotificaPessoa, String mensagem, String data, int idAdministrador) {
        this.idAdministradorNotificaPessoa = idAdministradorNotificaPessoa;
        this.mensagem = mensagem;
        this.data = data;
        this.idAdministrador = idAdministrador;
    }

    public int getIdAdministradorNotificaPessoa() {
        return idAdministradorNotificaPessoa;
    }

    public void setIdAdministradorNotificaPessoa(int idAdministradorNotificaPessoa) {
        this.idAdministradorNotificaPessoa = idAdministradorNotificaPessoa;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    @Override
    public String toString() {
        return mensagem + "\n" + "Data: " + data;
    }
}
