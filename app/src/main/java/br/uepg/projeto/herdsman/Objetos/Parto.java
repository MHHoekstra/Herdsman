package br.uepg.projeto.herdsman.Objetos;

public class Parto {
    private int id;
    private int Animal_idAnimal;
    private int cria;
    private String data;
    public Parto()
    {

    }
    public Parto(int animal_idAnimal, int cria, String data, String s) {
        Animal_idAnimal = animal_idAnimal;
        this.cria = cria;
        this.data = data;
    }

    public Parto(int id, int animal_idAnimal, int cria, String data) {
        this.id = id;
        Animal_idAnimal = animal_idAnimal;
        this.cria = cria;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimal_idAnimal() {
        return Animal_idAnimal;
    }

    public void setAnimal_idAnimal(int animal_idAnimal) {
        Animal_idAnimal = animal_idAnimal;
    }

    public int getCria() {
        return cria;
    }

    public void setCria(int cria) {
        this.cria = cria;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String s;
        s = "Data: " + this.getData() + "\n";
        switch (this.getCria())
        {
            case 1:
            {
                s = s.concat("Cria: Fêmea");
                break;
            }

            case 2:
            {
                s = s.concat("Cria: Macho");
                break;
            }

            case 3:
            {
                s = s.concat("Cria: Gêmeos Fêmeas");
                break;
            }

            case 4:
            {
                s = s.concat("Cria: Gêmeos Machos");
                break;
            }

            case 5:
            {
                s = s.concat("Cria: Gêmeos Macho - Fêmea");
                break;
            }
        }
        return s;
    }
}
