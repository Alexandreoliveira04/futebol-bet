package br.com.futebolbet.controller;

import br.com.futebolbet.models.Clube;
import br.com.futebolbet.repository.ClubeRepository;
import br.com.futebolbet.service.CampeonatoService;

import java.util.List;

public class CampeonatoController {

    private final CampeonatoService campeonatoService;
    private final ClubeRepository clubeRepository;

    public CampeonatoController() {
        this.campeonatoService = new CampeonatoService();
        this.clubeRepository = ClubeRepository.getInstance();
    }

    public void criarCampeonato(String nome, List<Clube> clubes) throws Exception {
        campeonatoService.criarCampeonato(nome, clubes);
    }

    public List<Clube> listarClubes() {
        return clubeRepository.obterTodos();
    }
}
