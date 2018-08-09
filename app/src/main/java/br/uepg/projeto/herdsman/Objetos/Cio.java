package br.uepg.projeto.herdsman.Objetos;

public class Cio {
    int idCio;
    int idAnimalPorCima;
    int idAnimalPorBaixo;
    Animal animalPorCima;
    Animal animalPorBaixo;

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

    String data;

    public Cio(int idCio, int idAnimalPorCima, int idAnimalPorBaixo, String data) {
        this.idCio = idCio;
        this.idAnimalPorCima = idAnimalPorCima;
        this.idAnimalPorBaixo = idAnimalPorBaixo;
        this.data = data;
    }

    public int getIdCio() {
        return idCio;
    }

    public void setIdCio(int idCio) {
        this.idCio = idCio;
    }

    public int getIdAnimalPorCima() {
        return idAnimalPorCima;
    }

    public void setIdAnimalPorCima(int idAnimalPorCima) {
        this.idAnimalPorCima = idAnimalPorCima;
    }

    public int getIdAnimalPorBaixo() {
        return idAnimalPorBaixo;
    }

    public void setIdAnimalPorBaixo(int idAnimalPorBaixo) {
        this.idAnimalPorBaixo = idAnimalPorBaixo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public String toString()
    {
        String s = "Animal por cima: " + animalPorCima.getNumero() + "\nAnimal por baixo: " + animalPorBaixo.getNumero() + "\nData: " + this.getData();
        return s;
    }
}
