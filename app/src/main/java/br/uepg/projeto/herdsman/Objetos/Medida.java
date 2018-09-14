package br.uepg.projeto.herdsman.Objetos;

public class Medida {

    int idMedida;
    String nome;

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
