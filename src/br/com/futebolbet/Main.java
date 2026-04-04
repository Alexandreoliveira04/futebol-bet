package br.com.futebolbet;

import br.com.futebolbet.models.Administrador;
import br.com.futebolbet.repository.UsuarioRepository;
import br.com.futebolbet.ui.LoginUi;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        UsuarioRepository repository = UsuarioRepository.getInstance();
        if (repository.obterTodos().isEmpty()) {
            Administrador adminMaster = new Administrador("Admin", "admin@futebolbet.com", "admin123");
            repository.adicionar(adminMaster);
        }

        SwingUtilities.invokeLater(() -> {
            LoginUi loginUi = new LoginUi();
            loginUi.setVisible(true);
        });
    }
}