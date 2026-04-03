package br.com.futebolbet.repository;

import br.com.futebolbet.models.Grupo;

import java.util.ArrayList;
import java.util.List;

public class GrupoRepository {
    private static GrupoRepository instance;
    private List<Grupo> grupos;

    private GrupoRepository() {
        this.grupos = new ArrayList<>();
    }

    public static GrupoRepository getInstance() {
        if (instance == null) {
            instance = new GrupoRepository();
        }
        return instance;
    }

    public void adicionar(Grupo grupo) {
        grupos.add(grupo);
    }

    public List<Grupo> obterTodos() {
        return new ArrayList<>(grupos);
    }

    public Grupo obterPorNome(String nome) {
        for (Grupo g : grupos) {
            if (g.getNome().equals(nome)) {
                return g;
            }
        }
        return null;
    }

    public void remover(Grupo grupo) {
        grupos.remove(grupo);
    }

    public void limpar() {
        grupos.clear();
    }
}
