package br.com.futebolbet.service;

import br.com.futebolbet.models.Usuario;

import java.util.List;

public class AuthService {
    public Usuario login(List<Usuario> usuarios, String email, String senha){
        for(Usuario u: usuarios){
            if(u.getEmail().equals(email) && u.autenticar(senha)){
                return u;
            }
        }
        return null;
    }
}
