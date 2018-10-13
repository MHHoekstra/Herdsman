package br.uepg.projeto.herdsman.objetos;

public class Medida {

    private long idMedida;
    private String nome;
    public Medida(){

    }

    public Medida(long idMedida, String nome) {
        this.idMedida = idMedida;
        this.nome = nome;
    }

    public long getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(long idMedida) {
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
