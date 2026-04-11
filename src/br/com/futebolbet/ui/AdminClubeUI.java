package br.com.futebolbet.ui;

import br.com.futebolbet.models.Clube;
import br.com.futebolbet.repository.ClubeRepository;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminClubeUI extends JPanel implements AtualizavelInterface {

    private JTextField txtNomeClube;
    private JButton btnCadastrarClube;
    private DefaultListModel<Clube> listModel;
    private JList<Clube> listaClubes;

    private final ClubeRepository clubeRepository;

    public AdminClubeUI() {
        this.clubeRepository = ClubeRepository.getInstance();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JPanel principal = new JPanel(new GridBagLayout());
        UiTheme.applyPanel(principal);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Cadastrar novo clube");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 14f));
        UiTheme.styleLabel(lblTitulo, false);
        principal.add(lblTitulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel n1 = new JLabel("Nome do clube:");
        UiTheme.styleLabel(n1, false);
        principal.add(n1, gbc);

        gbc.gridx = 1;
        txtNomeClube = new JTextField(18);
        UiTheme.styleTextField(txtNomeClube);
        principal.add(txtNomeClube, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        btnCadastrarClube = new JButton("Salvar clube");
        UiTheme.stylePrimaryButton(btnCadastrarClube);
        btnCadastrarClube.addActionListener(this::cadastrarClube);
        principal.add(btnCadastrarClube, gbc);

        gbc.gridy = 3;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x00, 0x33, 0x55));
        principal.add(sep, gbc);

        gbc.gridy = 4;
        JLabel lblListaTitulo = new JLabel("Clubes cadastrados:");
        lblListaTitulo.setFont(lblListaTitulo.getFont().deriveFont(Font.BOLD, 13f));
        UiTheme.styleLabel(lblListaTitulo, false);
        principal.add(lblListaTitulo, gbc);

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        listModel = new DefaultListModel<>();
        listaClubes = new JList<>(listModel);
        listaClubes.setBackground(UiTheme.BG_CARD);
        listaClubes.setForeground(UiTheme.FG_PRIMARY);
        listaClubes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaClubes.setVisibleRowCount(-1);
        listaClubes.setEnabled(false);
        JScrollPane scroll = new JScrollPane(listaClubes);
        UiTheme.styleScrollPane(scroll);
        principal.add(scroll, gbc);

        add(principal, BorderLayout.CENTER);
        atualizarLista();
    }

    @Override
    public void atualizarDados() {
        atualizarLista();
    }

    private void cadastrarClube(ActionEvent e) {
        String nome = txtNomeClube.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome do clube!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        clubeRepository.adicionar(new Clube(nome));
        JOptionPane.showMessageDialog(this, "Clube cadastrado com sucesso!");
        txtNomeClube.setText("");
        atualizarLista();
    }

    private void atualizarLista() {
        listModel.clear();
        for (Clube clube : clubeRepository.obterTodos()) {
            listModel.addElement(clube);
        }
    }
}
