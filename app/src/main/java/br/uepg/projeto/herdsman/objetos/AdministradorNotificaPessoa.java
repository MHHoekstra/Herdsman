package br.uepg.projeto.herdsman.objetos;

public class AdministradorNotificaPessoa {
    private long idAdministradorNotificaPessoa;
    private String mensagem;
    private long data;
    private long idAdministrador;

    public AdministradorNotificaPessoa() {
    }

    public AdministradorNotificaPessoa(long idAdministradorNotificaPessoa, String mensagem, long data) {
        this.idAdministradorNotificaPessoa = idAdministradorNotificaPessoa;
        this.mensagem = mensagem;
        this.data = data;
    }

    public AdministradorNotificaPessoa(String mensagem, long data) {
        this.mensagem = mensagem;
        this.data = data;
        idAdministrador = 1;
    }

    public AdministradorNotificaPessoa(long idAdministradorNotificaPessoa, String mensagem, long data, long idAdministrador) {
        this.idAdministradorNotificaPessoa = idAdministradorNotificaPessoa;
        this.mensagem = mensagem;
        this.data = data;
        this.idAdministrador = idAdministrador;
    }

    public long getIdAdministradorNotificaPessoa() {
        return idAdministradorNotificaPessoa;
    }

    public void setIdAdministradorNotificaPessoa(long idAdministradorNotificaPessoa) {
        this.idAdministradorNotificaPessoa = idAdministradorNotificaPessoa;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public long getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(long idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    @Override
    public String toString() {
        return mensagem + "\n" + "Data: " + data;
    }
}
