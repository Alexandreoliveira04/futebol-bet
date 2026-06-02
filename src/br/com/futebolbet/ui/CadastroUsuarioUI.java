package br.com.futebolbet.ui;

import br.com.futebolbet.controller.LoginController;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CadastroUsuarioUI extends JFrame {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmaSenha;
    private JLabel labelStatus;

    private final LoginController loginController;

    public CadastroUsuarioUI() {
        this.loginController = new LoginController();

        UiTheme.applyDarkOptionPaneDefaults();

        setTitle("Cadastro de Participante - Futebol Bet");
        setSize(420, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        UiTheme.applyRoot(this);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.ACCENT_BORDER),
                new EmptyBorder(16, 24, 14, 24)));
        JLabel titulo = new JLabel("Criar conta");
        titulo.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        titulo.setForeground(UiTheme.FG_PRIMARY);
        header.add(titulo, BorderLayout.WEST);
        JLabel sub = new JLabel("Preencha os dados para se cadastrar");
        sub.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        sub.setForeground(UiTheme.FG_MUTED);
        header.add(sub, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        UiTheme.applyPanel(form);
        form.setBorder(new EmptyBorder(24, 28, 20, 28));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        String[][] campos = {
            {"Nome completo", "nome"},
            {"E-mail", "email"},
            {"Senha", "senha"},
            {"Confirmar senha", "confirmar"}
        };

        int row = 0;
        for (String[] campo : campos) {
            gbc.gridx = 0; gbc.gridy = row++; gbc.insets = new Insets(0, 0, 4, 0);
            JLabel lbl = new JLabel(campo[0]);
            UiTheme.styleLabel(lbl, false);
            form.add(lbl, gbc);

            gbc.gridy = row++; gbc.insets = new Insets(0, 0, 14, 0);
            if ("senha".equals(campo[1])) {
                txtSenha = new JPasswordField();
                UiTheme.stylePasswordField(txtSenha);
                form.add(txtSenha, gbc);
            } else if ("confirmar".equals(campo[1])) {
                txtConfirmaSenha = new JPasswordField();
                UiTheme.stylePasswordField(txtConfirmaSenha);
                form.add(txtConfirmaSenha, gbc);
            } else if ("email".equals(campo[1])) {
                txtEmail = new JTextField();
                UiTheme.styleTextField(txtEmail);
                form.add(txtEmail, gbc);
            } else {
                txtNome = new JTextField();
                UiTheme.styleTextField(txtNome);
                form.add(txtNome, gbc);
            }
        }

        gbc.gridy = row++; gbc.insets = new Insets(4, 0, 8, 0);
        JButton btnCadastrar = new JButton("Criar conta");
        UiTheme.stylePrimaryButton(btnCadastrar);
        btnCadastrar.addActionListener(e -> realizarCadastro());
        form.add(btnCadastrar, gbc);

        gbc.gridy = row; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        form.add(labelStatus, gbc);

        add(form, BorderLayout.CENTER);
        setVisible(true);
    }

    private void realizarCadastro() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String confirma = new String(txtConfirmaSenha.getPassword());

        try {
            loginController.cadastrarParticipante(nome, email, senha, confirma);
            UiTheme.setLabelSuccess(labelStatus, "Cadastro realizado! Faca o login.");
            JOptionPane.showMessageDialog(this,
                    "Cadastro realizado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }
}
