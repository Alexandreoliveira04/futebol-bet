package br.com.futebolbet.controller;

import br.com.futebolbet.enums.TipoResultado;
import br.com.futebolbet.models.Aposta;
import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.repository.PartidaRepository;
import br.com.futebolbet.service.ApostaService;
import br.com.futebolbet.service.GrupoService;

import java.util.List;

public class ApostaController {

    private final ApostaService apostaService;
    private final PartidaRepository partidaRepository;
    private final GrupoService grupoService;

    public ApostaController() {
        this.apostaService = new ApostaService();
        this.partidaRepository = PartidaRepository.getInstance();
        this.grupoService = new GrupoService();
    }

    public void registrarAposta(Grupo grupo, Partida partida, TipoResultado resultado,
                                int golsCasa, int golsFora,
                                Participante participante) throws Exception {
        if (grupo == null) {
            throw new Exception("Selecione um grupo ou ingresse em um grupo antes de apostar.");
        }
        if (partida == null) {
            throw new Exception("Nenhuma partida disponível para apostar.");
        }
        if (resultado == null) {
            throw new Exception("Selecione o resultado esperado.");
        }
        Aposta aposta = new Aposta(participante, partida, resultado, golsCasa, golsFora);
        aposta.setGrupoId(grupo.getId());
        apostaService.registrarAposta(aposta);
        grupo.adicionarAposta(aposta);
    }

    public List<Partida> listarPartidas() {
        return partidaRepository.obterTodas();
    }

    public List<Grupo> listarGruposDoParticipante(Participante participante) {
        return grupoService.obterGruposDoParticipante(participante);
    }
}
