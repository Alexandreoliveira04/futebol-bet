package br.com.futebolbet.service;

import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.repository.GrupoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GrupoService {

    private final GrupoRepository grupoRepository;

    public GrupoService() {
        this.grupoRepository = GrupoRepository.getInstance();
    }

    public void criarGrupo(String nomeGrupo) throws Exception {
        if (nomeGrupo == null || nomeGrupo.trim().isEmpty()) {
            throw new Exception("Digite um nome para o grupo.");
        }
        if (grupoRepository.obterTodos().size() >= 5) {
            throw new Exception("O sistema já atingiu o limite máximo de 5 grupos.");
        }
        if (grupoRepository.obterPorNome(nomeGrupo.trim()) != null) {
            throw new Exception("Já existe um grupo cadastrado com este nome.");
        }
        Grupo novoGrupo = new Grupo(nomeGrupo.trim());
        grupoRepository.adicionar(novoGrupo);
    }

    public void ingressarNoGrupo(Grupo grupo, Participante participante) throws Exception {
        if (grupo.getParticipantes().contains(participante)) {
            throw new Exception("Você já faz parte deste grupo.");
        }
        boolean adicionado = grupo.adicionarParticipante(participante);
        if (!adicionado) {
            throw new Exception("Este grupo já atingiu a capacidade máxima de 5 participantes.");
        }
        grupoRepository.salvarMembro(grupo, participante);
    }

    public List<Grupo> obterTodosGrupos() {
        return grupoRepository.obterTodos();
    }

    public List<Grupo> obterGruposDoParticipante(Participante participante) {
        if (participante == null) return new ArrayList<>();
        return obterTodosGrupos().stream()
                .filter(g -> g.getParticipantes().contains(participante))
                .collect(Collectors.toList());
    }
}
