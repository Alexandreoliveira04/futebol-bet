package br.com.futebolbet.ui;

import br.com.futebolbet.controller.LoginController;
import br.com.futebolbet.models.Usuario;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginUi extends JFrame {

    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JButton cadastrarButton;
    private JLabel labelStatus;

    private final LoginController loginController;

    public LoginUi() {
        this.loginController = new LoginController();

        UiTheme.applyDarkOptionPaneDefaults();

        setTitle("Futebol Bet — Login");
        setSize(480, 420);
        setMinimumSize(new Dimension(420, 360));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        UiTheme.applyRoot(this);
        setLayout(new BorderLayout());

        add(criarPainelLogo(), BorderLayout.NORTH);
        add(criarPainelFormulario(), BorderLayout.CENTER);
    }

    private JPanel criarPainelLogo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(UiTheme.BG_CARD);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.ACCENT_BORDER),
                new EmptyBorder(24, 32, 20, 32)));

        JLabel icone = new JLabel("FUTEBOL BET");
        icone.setFont(new Font(Font.DIALOG, Font.BOLD, 26));
        icone.setForeground(UiTheme.ACCENT_GREEN);
        painel.add(icone, BorderLayout.WEST);

        JLabel sub = new JLabel("Plataforma de bolao esportivo");
        sub.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        sub.setForeground(UiTheme.FG_MUTED);
        painel.add(sub, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        UiTheme.applyPanel(painel);
        painel.setBorder(new EmptyBorder(28, 40, 28, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lEmail = new JLabel("E-mail");
        UiTheme.styleLabel(lEmail, false);
        painel.add(lEmail, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 16, 0);
        emailField = new JTextField();
        UiTheme.styleTextField(emailField);
        painel.add(emailField, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lSenha = new JLabel("Senha");
        UiTheme.styleLabel(lSenha, false);
        painel.add(lSenha, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 20, 0);
        senhaField = new JPasswordField();
        UiTheme.stylePasswordField(senhaField);
        senhaField.addActionListener(e -> realizarLogin());
        painel.add(senhaField, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 10, 0);
        loginButton = new JButton("Entrar");
        UiTheme.stylePrimaryButton(loginButton);
        loginButton.addActionListener(e -> realizarLogin());
        painel.add(loginButton, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 16, 0);
        cadastrarButton = new JButton("Cadastrar-se");
        UiTheme.styleSecondaryButton(cadastrarButton);
        cadastrarButton.addActionListener(e -> new CadastroUsuarioUI());
        painel.add(cadastrarButton, gbc);

        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        painel.add(labelStatus, gbc);

        return painel;
    }

    private void realizarLogin() {
        String email = emailField.getText().trim();
        String senha = new String(senhaField.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            UiTheme.setLabelError(labelStatus, "Preencha e-mail e senha.");
            return;
        }

        Usuario usuarioLogado = loginController.autenticar(email, senha);

        if (usuarioLogado != null) {
            new MenuPrincipalUI(usuarioLogado);
            dispose();
        } else {
            UiTheme.setLabelError(labelStatus, "E-mail ou senha invalidos.");
            senhaField.setText("");
        }
    }
}
