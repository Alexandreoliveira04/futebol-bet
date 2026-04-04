package br.com.futebolbet.models;

import br.com.futebolbet.enums.TipoResultado;

public class Aposta {
    private Participante participante;
    private Partida partida;
    private TipoResultado resultadoEsperado;
    private Integer golsCasaEsperado;
    private Integer golsForaEsperado;
    private Integer pontos;

    public Aposta(Participante participante, Partida partida, TipoResultado resultadoEsperado, Integer golsCasaEsperado, Integer golsForaEsperado) {
        this.participante = participante;
        this.partida = partida;
        this.resultadoEsperado = resultadoEsperado;
        this.golsCasaEsperado = golsCasaEsperado;
        this.golsForaEsperado = golsForaEsperado;
        this.pontos = 0;
    }

    public Participante getParticipante() {
        return participante;
    }

    public Partida getPartida() {
        return partida;
    }

    public TipoResultado getResultadoEsperado() {
        return resultadoEsperado;
    }

    public Integer getGolsCasaEsperado() {
        return golsCasaEsperado;
    }

    public Integer getGolsForaEsperado() {
        return golsForaEsperado;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos){
        this.pontos = pontos;
    }
}