package br.uepg.projeto.herdsman.objetos;

import java.io.Serializable;

public class Telefone implements Serializable {
    private long idTelefone;
    private long Pessoa_idPessoa;
    private String numero;

    public Telefone()
    {

    }
    public Telefone(long pessoa_idPessoa, String numero) {
        this.Pessoa_idPessoa = pessoa_idPessoa;
        this.numero = numero;
    }

    public Telefone(long idTelefone, long pessoa_idPessoa, String numero) {
        this.idTelefone = idTelefone;
        this.Pessoa_idPessoa = pessoa_idPessoa;
        this.numero = numero;
    }

    public long getIdTelefone() {
        return idTelefone;
    }

    public void setIdTelefone(long idTelefone) {
        this.idTelefone = idTelefone;
    }

    public long getPessoa_idPessoa() {
        return Pessoa_idPessoa;
    }

    public void setPessoa_idPessoa(int pessoa_idPessoa) {
        this.Pessoa_idPessoa = pessoa_idPessoa;
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
