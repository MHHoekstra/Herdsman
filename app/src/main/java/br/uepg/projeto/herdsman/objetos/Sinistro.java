package br.uepg.projeto.herdsman.objetos;

public class Sinistro {
    private long idSinistro;
    private long idAnimal;
    private long idEnfermidade;
    private long idFuncionario;
    private String data;

    private Animal animal;
    private Enfermidade enfermidade;
    private Pessoa pessoa;

    public Sinistro() {
    }

    public Sinistro(long idSinistro, long idAnimal, long idEnfermidade, long idFuncionario, String data) {
        this.idSinistro = idSinistro;
        this.idAnimal = idAnimal;
        this.idEnfermidade = idEnfermidade;
        this.idFuncionario = idFuncionario;
        this.data = data;
    }

    public Sinistro(long idAnimal, long idEnfermidade, long idFuncionario, String data) {
        this.idAnimal = idAnimal;
        this.idEnfermidade = idEnfermidade;
        this.idFuncionario = idFuncionario;
        this.data = data;
    }

    public long getIdSinistro() {
        return idSinistro;
    }

    public void setIdSinistro(long idSinistro) {
        this.idSinistro = idSinistro;
    }

    public long getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(long idAnimal) {
        this.idAnimal = idAnimal;
    }

    public long getIdEnfermidade() {
        return idEnfermidade;
    }

    public void setIdEnfermidade(long idEnfermidade) {
        this.idEnfermidade = idEnfermidade;
    }

    public long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Enfermidade getEnfermidade() {
        return enfermidade;
    }

    public void setEnfermidade(Enfermidade enfermidade) {
        this.enfermidade = enfermidade;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public String toString() {
        return "Número: " + this.animal.getNumero() + "\n" + enfermidade.getDescricao() + "\n" +data;
    }
}
