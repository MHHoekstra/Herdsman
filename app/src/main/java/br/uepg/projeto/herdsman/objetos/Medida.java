package br.uepg.projeto.herdsman.objetos;

public class Medida {

    private int idMedida;
    private String nome;
    public Medida(){

    }

    public Medida(int idMedida, String nome) {
        this.idMedida = idMedida;
        this.nome = nome;
    }

    public int getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(int idMedida) {
        this.idMedida = idMedida;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString()
    {
        return nome;
    }
}
