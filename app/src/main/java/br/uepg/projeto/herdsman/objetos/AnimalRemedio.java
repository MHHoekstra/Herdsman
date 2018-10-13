package br.uepg.projeto.herdsman.objetos;

public class AnimalRemedio {
    private long idAnimalRemedio;
    private long Remedio_idRemedio;
    private long Animal_idAnimal;
    private long Medida_idMedida;
    private long quantidade;
    private String data;
    private Animal animal;
    private Remedio remedio;
    private Medida medida;

    public AnimalRemedio(long remedio_idRemedio, long animal_idAnimal, long medida_idMedida, long quantidade, String data) {
        Remedio_idRemedio = remedio_idRemedio;
        Animal_idAnimal = animal_idAnimal;
        Medida_idMedida = medida_idMedida;
        this.quantidade = quantidade;
        this.data = data;
    }

    public AnimalRemedio() {

    }

    public AnimalRemedio(long idAnimalRemedio, long remedio_idRemedio, long animal_idAnimal, long medida_idMedida, String data, long quantidade) {
        this.idAnimalRemedio = idAnimalRemedio;
        Remedio_idRemedio = remedio_idRemedio;
        Animal_idAnimal = animal_idAnimal;
        Medida_idMedida = medida_idMedida;
        this.quantidade = quantidade;

        this.data = data;
    }

    public long getIdAnimalRemedio() {
        return idAnimalRemedio;
    }

    public void setIdAnimalRemedio(long idAnimalRemedio) {
        this.idAnimalRemedio = idAnimalRemedio;
    }

    public long getRemedio_idRemedio() {
        return Remedio_idRemedio;
    }

    public void setRemedio_idRemedio(int remedio_idRemedio) {
        Remedio_idRemedio = remedio_idRemedio;
    }

    public long getAnimal_idAnimal() {
        return Animal_idAnimal;
    }

    public void setAnimal_idAnimal(long animal_idAnimal) {
        Animal_idAnimal = animal_idAnimal;
    }

    public long getMedida_idMedida() {
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

    public long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString()
    {
        return remedio.getNome() + "\n"+ String.valueOf(quantidade) + " " + medida.getNome() + "\nData: " + data;
    }
}
