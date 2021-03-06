package br.uepg.projeto.herdsman.objetos;

import java.util.Calendar;

public class Inseminacao {
    private long idInseminacao;
    private long idAnimal;
    private long data;

    public Inseminacao(){

    }
    public Inseminacao(long idInseminacao, long idAnimal, long data) {
        this.idInseminacao = idInseminacao;
        this.idAnimal = idAnimal;
        this.data = data;
    }

    public Inseminacao(long idAnimal, long data) {
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

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    @Override
    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(data);
        return "Data: " + c.get(Calendar.DAY_OF_MONTH) + '/' +(c.get(Calendar.MONTH)+1) + '/'+c.get(Calendar.YEAR);

    }
}
