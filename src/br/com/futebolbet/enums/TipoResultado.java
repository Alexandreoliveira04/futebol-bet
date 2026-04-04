package br.com.futebolbet.enums;

public enum TipoResultado {
    VITORIA_CASA,
    EMPATE,
    VITORIA_FORA;

    public String getDescricao() {
        switch (this) {
            case VITORIA_CASA:
                return "Vitória do mandante (casa)";
            case EMPATE:
                return "Empate";
            case VITORIA_FORA:
                return "Vitória do visitante (fora)";
            default:
                return name();
        }
    }
}