package br.com.futebolbet.ui;

import br.com.futebolbet.models.Participante;
import br.com.futebolbet.repository.UsuarioRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CadastroUsuarioUI extends JFrame {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmaSenha;
    private JButton btnCadastrar;
    private JButton btnCancelar;

    private UsuarioRepository usuarioRepository;

    public CadastroUsuarioUI() {
        this.usuarioRepository = UsuarioRepository.getInstance();

        setTitle("Cadastro de Participante - Futebol Bet");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        principal.add(new JLabel("Nome Completo:"), gbc);

        gbc.gridx = 1;
        txtNome = new JTextField(15);
        principal.add(txtNome, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        principal.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        principal.add(txtEmail, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 2;
        principal.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        txtSenha = new JPasswordField(15);
        principal.add(txtSenha, gbc);

        // Confirmar Senha
        gbc.gridx = 0; gbc.gridy = 3;
        principal.add(new JLabel("Confirmar Senha:"), gbc);

        gbc.gridx = 1;
        txtConfirmaSenha = new JPasswordField(15);
        principal.add(txtConfirmaSenha, gbc);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(this::realizarCadastro);

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnCadastrar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        principal.add(painelBotoes, gbc);

        add(principal);
        setVisible(true);
    }

    private void realizarCadastro(ActionEvent e) {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String confirmaSenha = new String(txtConfirmaSenha.getPassword());

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!senha.equals(confirmaSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean emailExiste = usuarioRepository.obterTodos().stream()
                .anyMatch(u -> u.getEmail().equals(email));

        if (emailExiste) {
            JOptionPane.showMessageDialog(this, "Este email já está cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Participante novoParticipante = new Participante(nome, email, senha);

        usuarioRepository.adicionar(novoParticipante);

        JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso! Agora você pode fazer o login.");
        dispose();
    }
}