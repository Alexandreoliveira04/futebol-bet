package br.com.futebolbet.controller;

import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.service.GrupoService;

import java.util.List;

public class GrupoController {

    private final GrupoService grupoService;

    public GrupoController() {
        this.grupoService = new GrupoService();
    }

    public void criarGrupo(String nome) throws Exception {
        grupoService.criarGrupo(nome);
    }

    public void ingressarNoGrupo(String nomeGrupo, Participante participante) throws Exception {
        Grupo grupo = grupoService.obterTodosGrupos().stream()
                .filter(g -> g.getNome().equals(nomeGrupo))
                .findFirst()
                .orElseThrow(() -> new Exception("Grupo não encontrado."));
        grupoService.ingressarNoGrupo(grupo, participante);
    }

    public List<Grupo> listarGrupos() {
        return grupoService.obterTodosGrupos();
    }

    public List<Grupo> listarGruposDoParticipante(Participante participante) {
        return grupoService.obterGruposDoParticipante(participante);
    }
}
