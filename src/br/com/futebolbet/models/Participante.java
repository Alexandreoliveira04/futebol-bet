package br.com.futebolbet.models;

public class Participante extends Usuario{
    private Integer pontos;

    public Participante(String nome, String email, String senha){
        super(nome, email, senha);
        this.pontos = 0;
    }

    public Integer getPontos(){
        return this.pontos;
    }

    public void adicionarPontos(Integer pontos){
        this.pontos += pontos;
    }
}
