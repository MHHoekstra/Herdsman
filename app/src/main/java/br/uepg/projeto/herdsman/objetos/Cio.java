package br.uepg.projeto.herdsman.objetos;

public class Cio {
    private long idCio;
    private long idAnimalPorCima;
    private long idAnimalPorBaixo;
    private Animal animalPorCima;
    private Animal animalPorBaixo;
    private String data;
    private long idFuncionario;

    public Cio()
    {

    }
    public Cio(long animalPorCima, long animalPorBaixo, String data, long pessoa_idPessoa) {
        idAnimalPorCima = animalPorCima;
        idAnimalPorBaixo = animalPorBaixo;
        this.data = data;
        idFuncionario = pessoa_idPessoa;
    }


    public Cio(long idCio, long idAnimalPorCima, long idAnimalPorBaixo, String data) {
        this.idCio = idCio;
        this.idAnimalPorCima = idAnimalPorCima;
        this.idAnimalPorBaixo = idAnimalPorBaixo;
        this.data = data;
    }

    public Cio(long idCio, long idAnimalPorCima, long idAnimalPorBaixo, String data, long pessoa_idPessoa) {
        this.idCio = idCio;
        this.idAnimalPorCima = idAnimalPorCima;
        this.idAnimalPorBaixo = idAnimalPorBaixo;
        this.data = data;
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

    public long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}
