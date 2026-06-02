package br.com.futebolbet.controller;

import br.com.futebolbet.models.Campeonato;
import br.com.futebolbet.models.Clube;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.repository.CampeonatoRepository;
import br.com.futebolbet.repository.ClubeRepository;
import br.com.futebolbet.repository.PartidaRepository;

import java.time.LocalDateTime;
import java.util.List;

public class PartidaController {

    private final PartidaRepository partidaRepository;
    private final CampeonatoRepository campeonatoRepository;
    private final ClubeRepository clubeRepository;

    public PartidaController() {
        this.partidaRepository = PartidaRepository.getInstance();
        this.campeonatoRepository = CampeonatoRepository.getInstance();
        this.clubeRepository = ClubeRepository.getInstance();
    }

    public void agendarPartida(Campeonato campeonato, Clube clubeCasa,
                               Clube clubeFora, LocalDateTime dataHora) throws Exception {
        if (campeonato == null || clubeCasa == null || clubeFora == null) {
            throw new Exception("Todos os campos de seleção são obrigatórios.");
        }
        if (clubeCasa.equals(clubeFora)) {
            throw new Exception("Um clube não pode jogar contra si mesmo.");
        }
        partidaRepository.adicionar(new Partida(campeonato, clubeCasa, clubeFora, dataHora));
    }

    public List<Partida> listarPartidas() {
        return partidaRepository.obterTodas();
    }

    public List<Campeonato> listarCampeonatos() {
        return campeonatoRepository.obterTodos();
    }

    public List<Clube> listarClubes() {
        return clubeRepository.obterTodos();
    }
}
