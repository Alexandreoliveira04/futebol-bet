package br.com.futebolbet.models;

import java.util.Objects;

public class Clube {

    private int id;
    private String nome;

    public Clube(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clube clube = (Clube) o;
        return Objects.equals(nome, clube.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}
