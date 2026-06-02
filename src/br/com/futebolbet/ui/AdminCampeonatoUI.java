package br.com.futebolbet.ui;

import br.com.futebolbet.controller.CampeonatoController;
import br.com.futebolbet.models.Clube;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminCampeonatoUI extends JPanel implements AtualizavelInterface {

    private JTextField txtNomeCampeonato;
    private JList<Clube> listClubes;
    private DefaultListModel<Clube> listModelClubes;
    private Set<Clube> clubesSelecionados;
    private JLabel labelStatus;
    private JLabel lblContagem;

    private final CampeonatoController campeonatoController;

    public AdminCampeonatoUI() {
        this.campeonatoController = new CampeonatoController();
        this.clubesSelecionados = new HashSet<>();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Criar Campeonato"), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);

        atualizarListaClubes();
    }

    private JPanel criarFormulario() {
        JPanel card = new JPanel(new GridBagLayout());
        UiTheme.applyPanelCard(card);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lNome = new JLabel("Nome do campeonato");
        UiTheme.styleLabel(lNome, false);
        card.add(lNome, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 14, 0);
        txtNomeCampeonato = new JTextField();
        UiTheme.styleTextField(txtNomeCampeonato);
        card.add(txtNomeCampeonato, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 4, 0);
        JPanel linhaHint = new JPanel(new BorderLayout());
        linhaHint.setOpaque(false);
        JLabel hint = new JLabel("Selecione os clubes (clique para marcar/desmarcar):");
        UiTheme.styleLabel(hint, true);
        linhaHint.add(hint, BorderLayout.WEST);
        lblContagem = new JLabel("0 / 8 selecionados");
        UiTheme.styleLabel(lblContagem, true);
        linhaHint.add(lblContagem, BorderLayout.EAST);
        card.add(linhaHint, gbc);

        gbc.gridy = 3; gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; gbc.insets = new Insets(0, 0, 14, 0);
        listModelClubes = new DefaultListModel<>();
        listClubes = new JList<>(listModelClubes);
        listClubes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listClubes.setCellRenderer(new CheckBoxClubeRenderer());
        listClubes.setBackground(UiTheme.BG_CARD);
        listClubes.setForeground(UiTheme.FG_PRIMARY);
        listClubes.setFixedCellHeight(32);
        listClubes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = listClubes.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Clube clube = listModelClubes.getElementAt(index);
                    if (clubesSelecionados.contains(clube)) {
                        clubesSelecionados.remove(clube);
                    } else {
                        if (clubesSelecionados.size() < 8) clubesSelecionados.add(clube);
                    }
                    lblContagem.setText(clubesSelecionados.size() + " / 8 selecionados");
                    listClubes.repaint();
                }
            }
        });
        JScrollPane scroll = new JScrollPane(listClubes);
        UiTheme.styleScrollPane(scroll);
        scroll.setPreferredSize(new Dimension(0, 160));
        card.add(scroll, gbc);

        gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0; gbc.insets = new Insets(0, 0, 8, 0);
        JButton btnCriar = new JButton("Criar campeonato");
        UiTheme.stylePrimaryButton(btnCriar);
        btnCriar.addActionListener(e -> criarCampeonato());
        card.add(btnCriar, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        card.add(labelStatus, gbc);

        return card;
    }

    @Override
    public void atualizarDados() {
        atualizarListaClubes();
    }

    private void criarCampeonato() {
        try {
            String nome = txtNomeCampeonato.getText().trim();
            List<Clube> selecionados = new ArrayList<>(clubesSelecionados);
            campeonatoController.criarCampeonato(nome, selecionados);
            UiTheme.setLabelSuccess(labelStatus, "Campeonato '" + nome + "' criado com sucesso!");
            txtNomeCampeonato.setText("");
            clubesSelecionados.clear();
            lblContagem.setText("0 / 8 selecionados");
            listClubes.repaint();
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }

    private void atualizarListaClubes() {
        listModelClubes.clear();
        clubesSelecionados.clear();
        lblContagem.setText("0 / 8 selecionados");
        for (Clube clube : campeonatoController.listarClubes()) {
            listModelClubes.addElement(clube);
        }
        listClubes.repaint();
    }

    private class CheckBoxClubeRenderer implements ListCellRenderer<Clube> {
        private final JCheckBox checkBox = new JCheckBox();

        @Override
        public Component getListCellRendererComponent(JList<? extends Clube> list,
                Clube value, int index, boolean isSelected, boolean cellHasFocus) {
            checkBox.setText(value != null ? value.toString() : "");
            checkBox.setSelected(clubesSelecionados.contains(value));
            checkBox.setBackground(index % 2 == 0 ? UiTheme.BG_CARD : UiTheme.BG_CARD_ALT);
            checkBox.setForeground(UiTheme.FG_PRIMARY);
            checkBox.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
            checkBox.setOpaque(true);
            checkBox.setBorder(new EmptyBorder(4, 10, 4, 10));
            return checkBox;
        }
    }
}
