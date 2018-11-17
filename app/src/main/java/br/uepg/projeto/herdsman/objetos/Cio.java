package br.uepg.projeto.herdsman.objetos;

import java.util.Calendar;

public class Cio {
    private long idCio;
    private long idAnimalPorCima;
    private long idAnimalPorBaixo;
    private Animal animalPorCima;
    private Animal animalPorBaixo;
    private long data;
    private long idFuncionario;

    public Cio()
    {

    }
    public Cio(long animalPorCima, long animalPorBaixo, long data, long pessoa_idPessoa) {
        idAnimalPorCima = animalPorCima;
        idAnimalPorBaixo = animalPorBaixo;
        this.data = data;
        idFuncionario = pessoa_idPessoa;
    }



    public Cio(long idCio, long idAnimalPorCima, long idAnimalPorBaixo, long data, long pessoa_idPessoa) {
        this.idCio = idCio;
        this.idAnimalPorCima = idAnimalPorCima;
        this.idAnimalPorBaixo = idAnimalPorBaixo;
        this.data = data;
        this.idFuncionario = pessoa_idPessoa;
    }


    public Animal getAnimalPorCima() {
        return animalPorCima;
    }

    public void setAnimalPorCima(Animal animalPorCima) {
        this.animalPorCima = animalPorCima;
    }

    public Animal getAnimalPorBaixo() {
        return animalPorBaixo;
    }

    public void setAnimalPorBaixo(Animal animalPorBaixo) {
        this.animalPorBaixo = animalPorBaixo;
    }

    public long getIdCio() {
        return idCio;
    }

    public void setIdCio(long idCio) {
        this.idCio = idCio;
    }

    public long getIdAnimalPorCima() {
        return idAnimalPorCima;
    }

    public void setIdAnimalPorCima(long idAnimalPorCima) {
        this.idAnimalPorCima = idAnimalPorCima;
    }

    public long getIdAnimalPorBaixo() {
        return idAnimalPorBaixo;
    }

    public void setIdAnimalPorBaixo(long idAnimalPorBaixo) {
        this.idAnimalPorBaixo = idAnimalPorBaixo;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
    public String toString()
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(data);
        String s = "Animal por cima: " + animalPorCima.getNumero() + "\nAnimal por baixo: " + animalPorBaixo.getNumero() + "\nData: " + c.get(Calendar.DAY_OF_MONTH) + '/' + (c.get(Calendar.MONTH)+1)+'/'+c.get(Calendar.YEAR);
        return s;
    }

    public long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}
