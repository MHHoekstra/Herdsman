package br.uepg.projeto.herdsman.objetos;

import java.io.Serializable;

public class Telefone implements Serializable {
    private int idTelefone;
    private int Pessoa_idPessoa;
    private String numero;

    public Telefone()
    {

    }
    public Telefone(int pessoa_idPessoa, String numero) {
        Pessoa_idPessoa = pessoa_idPessoa;
        this.numero = numero;
    }

    public Telefone(int idTelefone, int pessoa_idPessoa, String numero) {
        this.idTelefone = idTelefone;
        Pessoa_idPessoa = pessoa_idPessoa;
        this.numero = numero;
    }

    public int getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(int idTelefone) {
        this.idTelefone = idTelefone;
    }

    public int getPessoa_idPessoa() {
        return Pessoa_idPessoa;
    }

    public void setPessoa_idPessoa(int pessoa_idPessoa) {
        Pessoa_idPessoa = pessoa_idPessoa;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return numero;
    }
}
