package br.uepg.projeto.herdsman.objetos;

public class Inseminacao {
    private long idInseminacao;
    private long idAnimal;
    private String data;

    public Inseminacao(){

    }
    public Inseminacao(long idInseminacao, long idAnimal, String data) {
        this.idInseminacao = idInseminacao;
        this.idAnimal = idAnimal;
        this.data = data;
    }

    public Inseminacao(long idAnimal, String data) {
        this.idAnimal = idAnimal;
        this.data = data;
    }

    public long getIdInseminacao() {
        return idInseminacao;
    }

    public void setIdInseminacao(long idInseminacao) {
        this.idInseminacao = idInseminacao;
    }

    public long getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(long idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Data: " + data;

    }
}
