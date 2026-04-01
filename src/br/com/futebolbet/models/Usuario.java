package br.com.futebolbet.models;

public abstract class Usuario {
    private String nome;
    private String email;
    private String senha;

    public Usuario(){}

    public Usuario(String nome, String email, String senha){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }
}
