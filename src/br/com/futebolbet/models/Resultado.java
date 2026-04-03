package br.com.futebolbet.models;

public class Resultado {
    private Integer golsCasa;
    private Integer golsFora;

    public Resultado(Integer golsCasa, Integer golsFora){
        this.golsCasa = golsCasa;
        this.golsFora = golsFora;
    }

    public Integer getGolsCasa(){
        return golsCasa;
    }

    public Integer getGolsFora(){
        return golsFora;
    }
}
