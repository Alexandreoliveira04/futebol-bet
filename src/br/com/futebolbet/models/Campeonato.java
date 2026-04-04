package br.com.futebolbet.models;

import java.util.List;

public class Campeonato {
    private String nome;
    private List<Clube> clubes;

    public Campeonato(String nome, List<Clube> clubes){
        this.nome = nome;
        this.clubes = clubes;
    }

    public String getNome(){
        return nome;
    }

    public List<Clube> getClubes(){
        return clubes;
    }

    @Override
    public String toString() {
        return nome != null ? nome : "";
    }
}
