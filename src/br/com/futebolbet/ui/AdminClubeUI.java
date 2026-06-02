package br.com.futebolbet.ui;

import br.com.futebolbet.controller.ClubeController;
import br.com.futebolbet.models.Clube;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminClubeUI extends JPanel implements AtualizavelInterface {

    private JTextField txtNomeClube;
    private DefaultListModel<String> listModel;
    private JLabel labelStatus;

    private final ClubeController clubeController;

    public AdminClubeUI() {
        this.clubeController = new ClubeController();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Cadastrar Clube"), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarPainelLista(), BorderLayout.SOUTH);

        atualizarLista();
    }

    private JPanel criarFormulario() {
        JPanel card = new JPanel(new GridBagLayout());
        UiTheme.applyPanelCard(card);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lbl = new JLabel("Nome do clube");
        UiTheme.styleLabel(lbl, false);
        card.add(lbl, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 14, 0);
        txtNomeClube = new JTextField();
        UiTheme.styleTextField(txtNomeClube);
        txtNomeClube.addActionListener(e -> cadastrarClube());
        card.add(txtNomeClube, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0);
        JButton btnSalvar = new JButton("Salvar clube");
        UiTheme.stylePrimaryButton(btnSalvar);
        btnSalvar.addActionListener(e -> cadastrarClube());
        card.add(btnSalvar, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        card.add(labelStatus, gbc);

        return card;
    }

    private JPanel criarPainelLista() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        UiTheme.applyPanel(painel);

        painel.add(UiTheme.createSectionHeader("Clubes Cadastrados"), BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        JList<String> listaClubes = new JList<>(listModel);
        listaClubes.setBackground(UiTheme.BG_CARD);
        listaClubes.setForeground(UiTheme.FG_PRIMARY);
        listaClubes.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
        listaClubes.setFixedCellHeight(32);
        listaClubes.setEnabled(false);

        JScrollPane scroll = new JScrollPane(listaClubes);
        UiTheme.styleScrollPane(scroll);
        scroll.setPreferredSize(new Dimension(0, 180));
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    @Override
    public void atualizarDados() {
        atualizarLista();
    }

    private void cadastrarClube() {
        try {
            clubeController.cadastrarClube(txtNomeClube.getText());
            UiTheme.setLabelSuccess(labelStatus, "Clube cadastrado com sucesso.");
            txtNomeClube.setText("");
            atualizarLista();
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }

    private void atualizarLista() {
        listModel.clear();
        for (Clube c : clubeController.listarClubes()) {
            listModel.addElement(c.getNome());
        }
    }
}
