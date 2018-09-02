package br.uepg.projeto.herdsman.Objetos;

public class Sinistro {
    int idSinistro;
    int idAnimal;
    int idEnfermidade;
    int idFuncionario;
    String data;

    Animal animal;
    Enfermidade enfermidade;
    Pessoa pessoa;

    public Sinistro() {
    }

    public Sinistro(int idSinistro, int idAnimal, int idEnfermidade, int idFuncionario, String data) {
        this.idSinistro = idSinistro;
        this.idAnimal = idAnimal;
        this.idEnfermidade = idEnfermidade;
        this.idFuncionario = idFuncionario;
        this.data = data;
    }

    public Sinistro(int idAnimal, int idEnfermidade, int idFuncionario, String data) {
        this.idAnimal = idAnimal;
        this.idEnfermidade = idEnfermidade;
        this.idFuncionario = idFuncionario;
        this.data = data;
    }

    public int getIdSinistro() {
        return idSinistro;
    }

    public void setIdSinistro(int idSinistro) {
        this.idSinistro = idSinistro;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public int getIdEnfermidade() {
        return idEnfermidade;
    }

    public void setIdEnfermidade(int idEnfermidade) {
        this.idEnfermidade = idEnfermidade;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
