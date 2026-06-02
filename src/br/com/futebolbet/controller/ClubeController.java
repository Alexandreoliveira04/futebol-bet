package br.com.futebolbet.controller;

import br.com.futebolbet.models.Clube;
import br.com.futebolbet.repository.ClubeRepository;

import java.util.List;

public class ClubeController {

    private final ClubeRepository clubeRepository;

    public ClubeController() {
        this.clubeRepository = ClubeRepository.getInstance();
    }

    public void cadastrarClube(String nome) throws Exception {
        String nomeAjustado = nome == null ? "" : nome.trim();
        if (nomeAjustado.isEmpty()) {
            throw new Exception("O nome do clube não pode ser vazio.");
        }
        boolean jaExiste = clubeRepository.obterTodos().stream()
                .anyMatch(c -> c.getNome().equalsIgnoreCase(nomeAjustado));
        if (jaExiste) {
            throw new Exception("Já existe um clube com este nome.");
        }
        clubeRepository.adicionar(new Clube(nomeAjustado));
    }

    public List<Clube> listarClubes() {
        return clubeRepository.obterTodos();
    }
}
