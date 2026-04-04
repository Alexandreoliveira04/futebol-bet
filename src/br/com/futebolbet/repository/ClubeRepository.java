package br.com.futebolbet.repository;

import br.com.futebolbet.models.Clube;

import java.util.ArrayList;
import java.util.List;

public class ClubeRepository {
    private static ClubeRepository instance;
    private List<Clube> clubes;

    private ClubeRepository(){
        this.clubes = new ArrayList<>();
    }

    public static ClubeRepository getInstance(){
        if(instance == null){
            instance = new ClubeRepository();
        }
        return instance;
    }

    public void adicionar(Clube clube){
        clubes.add(clube);
    }

    public List<Clube> obterTodos(){
        return new ArrayList<>(clubes);
    }
}
