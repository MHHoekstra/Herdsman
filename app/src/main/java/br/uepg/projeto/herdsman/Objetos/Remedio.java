package br.uepg.projeto.herdsman.Objetos;

public class Remedio {
    private int idRemedio;
    private String descricao;

    public int getIdRemedio() {
        return idRemedio;
    }

    public void setIdRemedio(int idRemedio) {
        this.idRemedio = idRemedio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Remedio(String s) {
        descricao = s;

    }
}
