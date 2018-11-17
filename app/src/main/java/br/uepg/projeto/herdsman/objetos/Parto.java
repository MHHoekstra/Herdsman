package br.uepg.projeto.herdsman.objetos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Parto {
    private long id;
    private long Animal_idAnimal;
    private int cria;
    private long data;
    public Parto()
    {

    }
    public Parto(long animal_idAnimal, int cria, long data) {
        Animal_idAnimal = animal_idAnimal;
        this.cria = cria;
        this.data = data;
    }

    public Parto(long id, long animal_idAnimal, int cria, long data) {
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

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String s;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.data);
        s = "Data: " + calendar.get(Calendar.DAY_OF_MONTH)+ '/'+(calendar.get(Calendar.MONTH)+1) +'/'+calendar.get(Calendar.YEAR) + "\n";
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
