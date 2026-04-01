package br.com.futebolbet.ui;

import br.com.futebolbet.models.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class LoginUi extends JFrame {

    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;

    private List<Usuario> usuarios;

    public LoginUi(List<Usuario> usuarios) {
        this.usuarios = usuarios;

        setTitle("Login - Futebol Bet");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        add(senhaField);

        loginButton = new JButton("Entrar");
        add(new JLabel());
        add(loginButton);

        loginButton.addActionListener(this::realizarLogin);
    }

    private void realizarLogin(ActionEvent e) {
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());

        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email) && u.autenticar(senha)) {
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                dispose();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Email ou senha inválidos!");
    }
}