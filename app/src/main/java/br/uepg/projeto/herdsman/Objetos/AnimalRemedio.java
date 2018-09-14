package br.uepg.projeto.herdsman.Objetos;

public class AnimalRemedio {
    int idAnimalRemedio;
    int Remedio_idRemedio;
    int Animal_idAnimal;
    int Medida_idMedida;
    int quantidade;
    String data;
    Animal animal;
    Remedio remedio;
    Medida medida;

    public AnimalRemedio(int remedio_idRemedio, int animal_idAnimal, int medida_idMedida, int quantidade, String data) {
        Remedio_idRemedio = remedio_idRemedio;
        Animal_idAnimal = animal_idAnimal;
        Medida_idMedida = medida_idMedida;
        this.quantidade = quantidade;
        this.data = data;
    }

    public AnimalRemedio() {

    }

    public AnimalRemedio(int idAnimalRemedio, int remedio_idRemedio, int animal_idAnimal, int medida_idMedida, String data, int quantidade) {
        this.idAnimalRemedio = idAnimalRemedio;
        Remedio_idRemedio = remedio_idRemedio;
        Animal_idAnimal = animal_idAnimal;
        Medida_idMedida = medida_idMedida;
        this.quantidade = quantidade;

        this.data = data;
    }

    public int getIdAnimalRemedio() {
        return idAnimalRemedio;
    }

    public void setIdAnimalRemedio(int idAnimalRemedio) {
        this.idAnimalRemedio = idAnimalRemedio;
    }

    public int getRemedio_idRemedio() {
        return Remedio_idRemedio;
    }

    public void setRemedio_idRemedio(int remedio_idRemedio) {
        Remedio_idRemedio = remedio_idRemedio;
    }

    public int getAnimal_idAnimal() {
        return Animal_idAnimal;
    }

    public void setAnimal_idAnimal(int animal_idAnimal) {
        Animal_idAnimal = animal_idAnimal;
    }

    public int getMedida_idMedida() {
        return Medida_idMedida;
    }

    public void setMedida_idMedida(int medida_idMedida) {
        Medida_idMedida = medida_idMedida;
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

    public Remedio getRemedio() {
        return remedio;
    }

    public void setRemedio(Remedio remedio) {
        this.remedio = remedio;
    }

    public Medida getMedida() {
        return medida;
    }

    public void setMedida(Medida medida) {
        this.medida = medida;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString()
    {
        return remedio.getNome() + "\n"+ String.valueOf(quantidade) + " " + medida.getNome() + "\nData: " + data;
    }
}
