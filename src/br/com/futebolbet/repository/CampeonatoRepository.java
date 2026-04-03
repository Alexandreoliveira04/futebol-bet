package br.com.futebolbet.repository;

import br.com.futebolbet.models.Campeonato;

import java.util.ArrayList;
import java.util.List;

public class CampeonatoRepository {
    private static CampeonatoRepository instance;
    private List<Campeonato> campeonatos;

    private CampeonatoRepository() {
        this.campeonatos = new ArrayList<>();
    }

    public static CampeonatoRepository getInstance() {
        if (instance == null) {
            instance = new CampeonatoRepository();
        }
        return instance;
    }

    public void adicionar(Campeonato campeonato) {
        if (campeonato.getClubes().size() <= 8) {
            campeonatos.add(campeonato);
        }
    }

    public List<Campeonato> obterTodos() {
        return new ArrayList<>(campeonatos);
    }

    public Campeonato obterPorNome(String nome) {
        for (Campeonato c : campeonatos) {
            if (c.getNome().equals(nome)) {
                return c;
            }
        }
        return null;
    }

    public void remover(Campeonato campeonato) {
        campeonatos.remove(campeonato);
    }

    public void limpar() {
        campeonatos.clear();
    }
}
