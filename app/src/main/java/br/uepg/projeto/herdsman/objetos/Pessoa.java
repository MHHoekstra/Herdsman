package br.uepg.projeto.herdsman.objetos;

import java.io.Serializable;

public class Pessoa implements Serializable {
    private long idPessoa;
    private  String nome;
    private  String cpf;
    private  String rg;
    private  int ativo;

    public int getAtivo() {
        return ativo;
    }

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

    public long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public Pessoa(String nome, String cpf, String rg) {
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;

    }

    public Pessoa(long id, String nome, String cpf, String rg) {
        this.idPessoa = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }
}
