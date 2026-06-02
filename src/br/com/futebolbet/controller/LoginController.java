package br.com.futebolbet.controller;

import br.com.futebolbet.models.Participante;
import br.com.futebolbet.models.Usuario;
import br.com.futebolbet.repository.UsuarioRepository;
import br.com.futebolbet.service.AuthService;

public class LoginController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    public LoginController() {
        this.authService = new AuthService();
        this.usuarioRepository = UsuarioRepository.getInstance();
    }

    public Usuario autenticar(String email, String senha) {
        return authService.login(usuarioRepository.obterTodos(), email, senha);
    }

    public void cadastrarParticipante(String nome, String email, String senha,
                                      String confirmacaoSenha) throws Exception {
        if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.isEmpty()) {
            throw new Exception("Todos os campos são obrigatórios.");
        }
        if (!senha.equals(confirmacaoSenha)) {
            throw new Exception("As senhas não coincidem.");
        }
        if (usuarioRepository.obterPorEmail(email.trim()) != null) {
            throw new Exception("Este e-mail já está cadastrado.");
        }
        usuarioRepository.adicionar(new Participante(nome.trim(), email.trim(), senha));
    }
}
