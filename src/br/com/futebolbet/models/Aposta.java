package br.com.futebolbet.models;

public class Aposta {
    private Participante participante;
    private Partida partida;
    private Integer resultadoEsperado; // 0 = Casa, 1 = Empate, 2 = Fora
    private Integer placarEsperado; // 0 = gols casa, 1 = gols fora
    private Integer pontos;

    public Aposta(Participante participante, Partida partida, Integer resultadoEsperado, Integer placarEsperado) {
        this.participante = participante;
        this.partida = partida;
        this.resultadoEsperado = resultadoEsperado;
        this.placarEsperado = placarEsperado;
        this.pontos = 0;
    }

    public Participante getParticipante() {
        return participante;
    }

    public Partida getPartida() {
        return partida;
    }

    public Integer getResultadoEsperado() {
        return resultadoEsperado;
    }

    public Integer getPlacarEsperado() {
        return placarEsperado;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void calcularPontos(Resultado resultadoReal) {
        if (resultadoReal == null) {
            this.pontos = 0;
            return;
        }

        Integer golsCasa = resultadoReal.getGolsCasa();
        Integer golsFora = resultadoReal.getGolsFora();
        Integer resultadoReal_int;

        // Calcular resultado real: 0 = Casa, 1 = Empate, 2 = Fora
        if (golsCasa > golsFora) {
            resultadoReal_int = 0;
        } else if (golsCasa < golsFora) {
            resultadoReal_int = 2;
        } else {
            resultadoReal_int = 1;
        }

        // 10 pontos para placar exato
        if (golsCasa == this.placarEsperado && golsFora == this.placarEsperado) {
            this.pontos = 10;
        } 
        // 5 pontos para resultado correto
        else if (resultadoReal_int.equals(this.resultadoEsperado)) {
            this.pontos = 5;
        } 
        // 0 pontos se errou
        else {
            this.pontos = 0;
        }
    }
}
