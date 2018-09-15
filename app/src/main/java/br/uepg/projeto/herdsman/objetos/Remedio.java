package br.uepg.projeto.herdsman.objetos;

public class Remedio {
    private int idRemedio;
    private String nome;

    public Remedio() {
    }

    public int getIdRemedio() {
        return idRemedio;
    }

    public void setIdRemedio(int idRemedio) {
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

    public Remedio(int idRemedio, String nome) {
        this.idRemedio = idRemedio;
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
