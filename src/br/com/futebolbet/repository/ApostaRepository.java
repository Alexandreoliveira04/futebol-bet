package br.com.futebolbet.repository;

import br.com.futebolbet.models.Aposta;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Participante;

import java.util.ArrayList;
import java.util.List;

public class ApostaRepository {
    private static ApostaRepository instance;
    private List<Aposta> apostas;

    private ApostaRepository() {
        this.apostas = new ArrayList<>();
    }

    public static ApostaRepository getInstance() {
        if (instance == null) {
            instance = new ApostaRepository();
        }
        return instance;
    }

    public void adicionar(Aposta aposta) {
        apostas.add(aposta);
    }

    public List<Aposta> obterTodas() {
        return new ArrayList<>(apostas);
    }

    public List<Aposta> obterPorParticipante(Participante participante) {
        List<Aposta> resultado = new ArrayList<>();
        for (Aposta a : apostas) {
            if (a.getParticipante().equals(participante)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public List<Aposta> obterPorPartida(Partida partida) {
        List<Aposta> resultado = new ArrayList<>();
        for (Aposta a : apostas) {
            if (a.getPartida().equals(partida)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public void remover(Aposta aposta) {
        apostas.remove(aposta);
    }

    public void limpar() {
        apostas.clear();
    }
}
