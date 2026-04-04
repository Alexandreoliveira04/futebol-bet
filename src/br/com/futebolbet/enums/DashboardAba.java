package br.com.futebolbet.enums;


public enum DashboardAba {
    ADM_CAMPEONATOS("adm_campeonatos"),
    ADM_PARTIDAS("adm_partidas"),
    ADM_RESULTADOS("adm_resultados"),
    ADM_GRUPOS("adm_grupos"),
    ADM_CLASSIFICACAO("adm_classificacao"),

    PAR_PARTIDAS("par_partidas"),
    PAR_CLASSIFICACAO("par_classificacao"),
    PAR_GRUPOS("par_grupos"),
    PAR_APOSTAS("par_apostas");

    private final String cardId;

    DashboardAba(String cardId) {
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }
}
