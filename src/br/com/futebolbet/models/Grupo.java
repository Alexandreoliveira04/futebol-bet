package br.com.futebolbet.models;

import java.util.ArrayList;
import java.util.List;

public class Grupo {

    private static final int MAX_PARTICIPANTES = 5;

    private int id;
    private String nome;
    private List<Participante> participantes;
    private List<Aposta> apostas;

    public Grupo(String nome) {
        this.nome = nome;
        this.participantes = new ArrayList<>();
        this.apostas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public List<Aposta> getApostas() {
        return apostas;
    }

    public boolean adicionarParticipante(Participante participante) {
        if (participantes.size() >= MAX_PARTICIPANTES) {
            return false;
        }
        participantes.add(participante);
        return true;
    }

    public boolean removerParticipante(Participante participante) {
        return participantes.remove(participante);
    }

    public void adicionarAposta(Aposta aposta) {
        apostas.add(aposta);
    }

    public List<Aposta> getApostasPorParticipante(Participante participante) {
        List<Aposta> resultado = new ArrayList<>();
        for (Aposta aposta : apostas) {
            if (aposta.getParticipante().equals(participante)) {
                resultado.add(aposta);
            }
        }
        return resultado;
    }

    public List<Participante> getClassificacao() {
        List<Participante> classificacao = new ArrayList<>(participantes);
        classificacao.sort((p1, p2) -> p2.getPontos().compareTo(p1.getPontos()));
        return classificacao;
    }
}
