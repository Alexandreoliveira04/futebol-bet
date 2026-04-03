package br.com.futebolbet.repository;

import br.com.futebolbet.models.Partida;

import java.util.ArrayList;
import java.util.List;

public class PartidaRepository {
    private static PartidaRepository instance;
    private List<Partida> partidas;

    private PartidaRepository() {
        this.partidas = new ArrayList<>();
    }

    public static PartidaRepository getInstance() {
        if (instance == null) {
            instance = new PartidaRepository();
        }
        return instance;
    }

    public void adicionar(Partida partida) {
        partidas.add(partida);
    }

    public List<Partida> obterTodas() {
        return new ArrayList<>(partidas);
    }

    public void remover(Partida partida) {
        partidas.remove(partida);
    }

    public void limpar() {
        partidas.clear();
    }
}
