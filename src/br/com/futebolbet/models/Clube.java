package br.com.futebolbet.models;

public class Clube {
    private String nome;

    public Clube(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
