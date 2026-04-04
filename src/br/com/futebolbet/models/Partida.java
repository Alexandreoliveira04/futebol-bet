package br.com.futebolbet.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Partida {
    private Campeonato campeonato;
    private Clube clubeCasa;
    private Clube clubeFora;
    private Resultado resultado;
    private LocalDateTime dataHora;

    public Partida(Campeonato campeonato,Clube clubeCasa, Clube clubeFora, LocalDateTime dataHora){
        this.campeonato = campeonato;
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

    public Campeonato getCampeonato() {
        return campeonato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Partida partida = (Partida) o;
        return Objects.equals(dataHora, partida.dataHora)
                && nomeCampeonato(campeonato).equals(nomeCampeonato(partida.campeonato))
                && nomeClube(clubeCasa).equals(nomeClube(partida.clubeCasa))
                && nomeClube(clubeFora).equals(nomeClube(partida.clubeFora));
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataHora, nomeCampeonato(campeonato), nomeClube(clubeCasa), nomeClube(clubeFora));
    }

    private static String nomeCampeonato(Campeonato c) {
        return c != null && c.getNome() != null ? c.getNome() : "";
    }

    private static String nomeClube(Clube cl) {
        return cl != null && cl.getNome() != null ? cl.getNome() : "";
    }
}
