package br.com.futebolbet.controller;

import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.service.GrupoService;

import java.util.List;

public class ClassificacaoController {

    private final GrupoService grupoService;

    public ClassificacaoController() {
        this.grupoService = new GrupoService();
    }

    public List<Grupo> listarGrupos() {
        return grupoService.obterTodosGrupos();
    }

    public List<Grupo> listarGruposDoParticipante(Participante participante) {
        return grupoService.obterGruposDoParticipante(participante);
    }

    public List<Participante> obterClassificacao(Grupo grupo) {
        return grupo.getClassificacao();
    }
}
