package br.uepg.projeto.herdsman.objetos;

public class AnimalEnfermidade {
    private int idAnimalEnfermidade;
    private int Animal_idAnimal;
    private int Enfermidade_idEnfermidade;
    private String data;
    private int Usuario_idUsuario;

    public AnimalEnfermidade(){}

    public AnimalEnfermidade(int idAnimalEnfermidade, int animal_idAnimal, int enfermidade_idEnfermidade, String data, int usuario_idUsuario) {
        this.idAnimalEnfermidade = idAnimalEnfermidade;
        Animal_idAnimal = animal_idAnimal;
        Enfermidade_idEnfermidade = enfermidade_idEnfermidade;
        this.data = data;
        Usuario_idUsuario = usuario_idUsuario;
    }

    public int getIdAnimalEnfermidade() {
        return idAnimalEnfermidade;
    }

    public void setIdAnimalEnfermidade(int idAnimalEnfermidade) {
        this.idAnimalEnfermidade = idAnimalEnfermidade;
    }

    public int getAnimal_idAnimal() {
        return Animal_idAnimal;
    }

    public void setAnimal_idAnimal(int animal_idAnimal) {
        Animal_idAnimal = animal_idAnimal;
    }

    public int getEnfermidade_idEnfermidade() {
        return Enfermidade_idEnfermidade;
    }

    public void setEnfermidade_idEnfermidade(int enfermidade_idEnfermidade) {
        Enfermidade_idEnfermidade = enfermidade_idEnfermidade;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getUsuario_idUsuario() {
        return Usuario_idUsuario;
    }

    public void setUsuario_idUsuario(int usuario_idUsuario) {
        Usuario_idUsuario = usuario_idUsuario;
    }
}
