package br.com.futebolbet.models;

import java.time.LocalDateTime;

public class Partida {
    private Clube clubeCasa;
    private Clube clubeFora;
    private Resultado resultado;
    private LocalDateTime dataHora;

    public Partida(Clube clubeCasa, Clube clubeFora, LocalDateTime dataHora){
        this.clubeCasa = clubeCasa;
        this.clubeFora = clubeFora;
        this.dataHora = dataHora;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Clube getClubeCasa() {
        return clubeCasa;
    }

    public Clube getClubeFora() {
        return clubeFora;
    }

    @Override
    public String toString() {
        return clubeCasa.getNome() + " x " + clubeFora.getNome() + " (" + dataHora + ")";
    }
}
