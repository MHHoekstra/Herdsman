package br.uepg.projeto.herdsman.Objetos;

import java.io.Serializable;

public class Pessoa implements Serializable {
    public int idPessoa;
    public String nome;
    public String cpf;
    public String rg;

    public Pessoa()
    {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Pessoa(String nome, String cpf, String rg) {
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;

    }

    public Pessoa(int id, String nome, String cpf, String rg) {
        this.idPessoa = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
