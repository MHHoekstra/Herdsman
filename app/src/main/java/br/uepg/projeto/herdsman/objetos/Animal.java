package br.uepg.projeto.herdsman.objetos;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Objects;

public class Animal implements Serializable {
    private long id;
    private String numero;
    private String nome;
    private int ativo;
    public Animal()
    {

    }
    public Animal(long id, String n, String s)
    {
        this.id = id;
        this.numero = n;
        this.nome = s;
    }
    public Animal(String numero, String nome)
    {
        this.id = 0;
        this.numero = numero;
        this.nome = nome;
    }

    public Animal(long id, String numero, String nome, int ativo) {
        this.id = id;
        this.numero = numero;
        this.nome = nome;
        this.ativo = ativo;
    }

    public Animal(String nome, String numero, int ativo) {
        this.nome = nome;
        this.numero = numero;
        this.ativo = ativo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "# " + numero + " - " +  nome;
    }

    public String getNumero() {

        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id &&
                ativo == animal.ativo &&
                Objects.equals(numero, animal.numero) &&
                Objects.equals(nome, animal.nome);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {

        return Objects.hash(id, numero, nome, ativo);
    }
}
