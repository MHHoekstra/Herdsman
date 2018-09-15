package br.uepg.projeto.herdsman.objetos;

public class Inseminacao {
    private int idInseminacao;
    private int idAnimal;
    private String data;

    public Inseminacao(){

    }
    public Inseminacao(int idInseminacao, int idAnimal, String data) {
        this.idInseminacao = idInseminacao;
        this.idAnimal = idAnimal;
        this.data = data;
    }

    public Inseminacao(int idAnimal, String data) {
        this.idAnimal = idAnimal;
        this.data = data;
    }

    public int getIdInseminacao() {
        return idInseminacao;
    }

    public void setIdInseminacao(int idInseminacao) {
        this.idInseminacao = idInseminacao;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
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
