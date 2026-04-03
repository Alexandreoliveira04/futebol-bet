package br.com.futebolbet.repository;

import br.com.futebolbet.models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private static UsuarioRepository instance;
    private List<Usuario> usuarios;

    private UsuarioRepository() {
        this.usuarios = new ArrayList<>();
    }

    public static UsuarioRepository getInstance() {
        if (instance == null) {
            instance = new UsuarioRepository();
        }
        return instance;
    }

    public void adicionar(Usuario usuario) {
        usuarios.add(usuario);
    }

    public List<Usuario> obterTodos() {
        return new ArrayList<>(usuarios);
    }

    public Usuario obterPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public void remover(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public void limpar() {
        usuarios.clear();
    }
}
