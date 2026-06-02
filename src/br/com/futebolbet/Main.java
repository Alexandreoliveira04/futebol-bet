package br.com.futebolbet;

import br.com.futebolbet.models.Administrador;
import br.com.futebolbet.repository.ApostaRepository;
import br.com.futebolbet.repository.CampeonatoRepository;
import br.com.futebolbet.repository.ClubeRepository;
import br.com.futebolbet.repository.DatabaseInitializer;
import br.com.futebolbet.repository.GrupoRepository;
import br.com.futebolbet.repository.PartidaRepository;
import br.com.futebolbet.repository.UsuarioRepository;
import br.com.futebolbet.ui.LoginUi;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Cria as tabelas no banco se ainda nao existirem
        DatabaseInitializer.init();

        // Inicializa os repositorios na ordem correta (respeitando dependencias)
        ClubeRepository.getInstance();
        CampeonatoRepository.getInstance();
        PartidaRepository.getInstance();
        UsuarioRepository usuarioRepo = UsuarioRepository.getInstance();
        GrupoRepository.getInstance();
        ApostaRepository.getInstance();

        // Cria administrador padrao se o banco estiver vazio
        if (usuarioRepo.obterTodos().isEmpty()) {
            Administrador adminMaster = new Administrador("Admin", "admin@futebolbet.com", "admin123");
            usuarioRepo.adicionar(adminMaster);
        }

        SwingUtilities.invokeLater(() -> {
            LoginUi loginUi = new LoginUi();
            loginUi.setVisible(true);
        });
    }
}
