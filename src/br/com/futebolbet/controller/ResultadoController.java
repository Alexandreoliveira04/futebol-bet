package br.com.futebolbet.controller;

import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Resultado;
import br.com.futebolbet.repository.PartidaRepository;
import br.com.futebolbet.service.ApostaService;

import java.util.List;

public class ResultadoController {

    private final PartidaRepository partidaRepository;
    private final ApostaService apostaService;

    public ResultadoController() {
        this.partidaRepository = PartidaRepository.getInstance();
        this.apostaService = new ApostaService();
    }

    public void registrarResultado(Partida partida, int golsCasa, int golsFora) throws Exception {
        if (partida == null) {
            throw new Exception("Nenhuma partida selecionada.");
        }
        partida.setResultado(new Resultado(golsCasa, golsFora));
        partidaRepository.registrarResultado(partida);
        apostaService.processarResultadosPartida(partida);
    }

    public List<Partida> listarPartidas() {
        return partidaRepository.obterTodas();
    }
}
