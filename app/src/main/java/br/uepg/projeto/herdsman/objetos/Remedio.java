package br.uepg.projeto.herdsman.objetos;

import java.io.Serializable;

public class Remedio implements Serializable {
    private long idRemedio;
    private String nome;

    public Remedio() {
    }

    public long getIdRemedio() {
        return idRemedio;
    }

    public void setIdRemedio(long idRemedio) {
        this.idRemedio = idRemedio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Remedio(String s) {
        nome = s;

    }

    public Remedio(long idRemedio, String nome) {
        this.idRemedio = idRemedio;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
