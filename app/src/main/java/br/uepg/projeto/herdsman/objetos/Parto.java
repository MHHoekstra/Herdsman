package br.uepg.projeto.herdsman.objetos;

public class Parto {
    private long id;
    private long Animal_idAnimal;
    private int cria;
    private String data;
    public Parto()
    {

    }
    public Parto(long animal_idAnimal, int cria, String data) {
        Animal_idAnimal = animal_idAnimal;
        this.cria = cria;
        this.data = data;
    }

    public Parto(long id, long animal_idAnimal, int cria, String data) {
        this.id = id;
        Animal_idAnimal = animal_idAnimal;
        this.cria = cria;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAnimal_idAnimal() {
        return Animal_idAnimal;
    }

    public void setAnimal_idAnimal(long animal_idAnimal) {
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
