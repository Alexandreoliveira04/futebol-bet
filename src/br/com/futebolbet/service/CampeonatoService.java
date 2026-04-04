package br.com.futebolbet.service;

import br.com.futebolbet.models.Campeonato;
import br.com.futebolbet.models.Clube;
import br.com.futebolbet.repository.CampeonatoRepository;

import java.util.List;

public class CampeonatoService {

    private CampeonatoRepository campeonatoRepository;

    public CampeonatoService() {
        this.campeonatoRepository = CampeonatoRepository.getInstance();
    }

    public void criarCampeonato(String nome, List<Clube> clubesParticipantes) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome do campeonato é obrigatório.");
        }

        if (clubesParticipantes.size() > 8) {
            throw new Exception("Um campeonato pode ter no máximo 8 clubes participantes.");
        }

        if (clubesParticipantes.size() < 2) {
            throw new Exception("Selecione pelo menos 2 clubes para o campeonato.");
        }

        if (campeonatoRepository.obterPorNome(nome) != null) {
            throw new Exception("Já existe um campeonato com este nome.");
        }

        Campeonato campeonato = new Campeonato(nome, clubesParticipantes);

        campeonatoRepository.adicionar(campeonato);
    }
}