package br.uepg.projeto.herdsman.objetos;

import java.util.Calendar;

public class AnimalEnfermidade {
    private long idAnimalEnfermidade;
    private long idAnimal;
    private long idEnfermidade;
    private long idFuncionario;
    private long data;

    private Animal animal;
    private Enfermidade enfermidade;
    private Pessoa pessoa;

    public AnimalEnfermidade() {
    }

    public AnimalEnfermidade(long idAnimalEnfermidade, long idAnimal, long idEnfermidade, long idFuncionario, long data) {
        this.idAnimalEnfermidade = idAnimalEnfermidade;
        this.idAnimal = idAnimal;
        this.idEnfermidade = idEnfermidade;
        this.idFuncionario = idFuncionario;
        this.data = data;
    }

    public AnimalEnfermidade(long idAnimal, long idEnfermidade, long idFuncionario, long data) {
        this.idAnimal = idAnimal;
        this.idEnfermidade = idEnfermidade;
        this.idFuncionario = idFuncionario;
        this.data = data;
    }

    public long getIdAnimalEnfermidade() {
        return idAnimalEnfermidade;
    }

    public void setIdAnimalEnfermidade(long idAnimalEnfermidade) {
        this.idAnimalEnfermidade = idAnimalEnfermidade;
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

    public long getData() {
        return data;
    }

    public void setData(long data) {
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
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(data);
        return "Número: " + this.animal.getNumero() + "\n" + "Enfermidade: "+ enfermidade.getDescricao() + "\n" +"Data: " + c.get(Calendar.DAY_OF_MONTH) +'/'+(c.get(Calendar.MONTH)+1)+'/'+c.get(Calendar.YEAR);
    }
}
