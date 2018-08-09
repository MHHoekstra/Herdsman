package br.uepg.projeto.herdsman.Objetos;

public class Enfermidade {
    private int id;
    private String descricao;

    public Enfermidade()
    {

    }
    public Enfermidade(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Enfermidade(String descricao) {
        this.id = 0;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
