package br.com.futebolbet.ui;

import br.com.futebolbet.models.Usuario;
import br.com.futebolbet.repository.UsuarioRepository;
import br.com.futebolbet.service.AuthService;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUi extends JFrame {

    private JTextField emailField;
    private JPasswordField senhaField;
    private JButton loginButton;
    private JButton cadastrarButton;

    private AuthService authService;
    private UsuarioRepository usuarioRepository;

    public LoginUi() {
        this.authService = new AuthService();
        this.usuarioRepository = UsuarioRepository.getInstance();

        UiTheme.applyDarkOptionPaneDefaults();
        UIManager.put("Panel.background", UiTheme.BG_PRIMARY);
        UIManager.put("Label.foreground", UiTheme.FG_PRIMARY);

        setTitle("Login — Futebol Bet");
        setSize(520, 380);
        setMinimumSize(new Dimension(480, 340));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        UiTheme.applyRoot(this);
        setLayout(new BorderLayout());

        JPanel centro = new JPanel(new GridBagLayout());
        UiTheme.applyPanel(centro);
        centro.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 8, 12, 8);

        JLabel titulo = new JLabel("Futebol Bet");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        UiTheme.styleLabel(titulo, false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centro.add(titulo, gbc);

        JLabel sub = new JLabel("Entre com seu e-mail e senha");
        UiTheme.styleLabel(sub, true);
        gbc.gridy = 1;
        centro.add(sub, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel lEmail = new JLabel("E-mail");
        UiTheme.styleLabel(lEmail, false);
        centro.add(lEmail, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(22);
        UiTheme.styleTextField(emailField);
        centro.add(emailField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel lSenha = new JLabel("Senha");
        UiTheme.styleLabel(lSenha, false);
        centro.add(lSenha, gbc);

        gbc.gridx = 1;
        senhaField = new JPasswordField(22);
        UiTheme.stylePasswordField(senhaField);
        centro.add(senhaField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel botoes = new JPanel(new GridLayout(1, 2, 16, 0));
        UiTheme.applyPanel(botoes);
        cadastrarButton = new JButton("Cadastrar-se");
        UiTheme.styleSecondaryButton(cadastrarButton);
        cadastrarButton.addActionListener(e -> abrirTelaCadastro());
        loginButton = new JButton("Entrar");
        UiTheme.stylePrimaryButton(loginButton);
        loginButton.addActionListener(this::realizarLogin);
        botoes.add(cadastrarButton);
        botoes.add(loginButton);
        centro.add(botoes, gbc);

        add(centro, BorderLayout.CENTER);
    }

    private void realizarLogin(ActionEvent e) {
        String email = emailField.getText();
        String senha = new String(senhaField.getPassword());

        Usuario usuarioLogado = authService.login(usuarioRepository.obterTodos(), email, senha);

        if (usuarioLogado != null) {
            JOptionPane.showMessageDialog(this, "Bem-vindo(a), " + usuarioLogado.getNome() + "!");
            new MenuPrincipalUI(usuarioLogado);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirTelaCadastro() {
        new CadastroUsuarioUI();
    }
}
