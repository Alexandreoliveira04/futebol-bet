package br.com.futebolbet.ui;

import br.com.futebolbet.models.Clube;
import br.com.futebolbet.repository.ClubeRepository;
import br.com.futebolbet.service.CampeonatoService;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private JButton btnCriarCampeonato;

    private final ClubeRepository clubeRepository;
    private final CampeonatoService campeonatoService;

    public AdminCampeonatoUI() {
        this.clubeRepository = ClubeRepository.getInstance();
        this.campeonatoService = new CampeonatoService();
        this.clubesSelecionados = new HashSet<>();

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
        JLabel lblTitulo = new JLabel("Criar campeonato (máx. 8 clubes)");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 14f));
        UiTheme.styleLabel(lblTitulo, false);
        principal.add(lblTitulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel n2 = new JLabel("Nome do campeonato:");
        UiTheme.styleLabel(n2, false);
        principal.add(n2, gbc);

        gbc.gridx = 1;
        txtNomeCampeonato = new JTextField(18);
        UiTheme.styleTextField(txtNomeCampeonato);
        principal.add(txtNomeCampeonato, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel hint = new JLabel("Selecione os clubes (clique para marcar/desmarcar):");
        UiTheme.styleLabel(hint, true);
        principal.add(hint, gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        listModelClubes = new DefaultListModel<>();
        listClubes = new JList<>(listModelClubes);
        listClubes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listClubes.setCellRenderer(new CheckBoxClubeRenderer());
        listClubes.setBackground(UiTheme.BG_CARD);
        listClubes.setForeground(UiTheme.FG_PRIMARY);
        listClubes.setVisibleRowCount(-1);
        listClubes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = listClubes.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Clube clube = listModelClubes.getElementAt(index);
                    if (clubesSelecionados.contains(clube)) {
                        clubesSelecionados.remove(clube);
                    } else {
                        clubesSelecionados.add(clube);
                    }
                    listClubes.repaint();
                }
            }
        });
        principal.add(listClubes, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        btnCriarCampeonato = new JButton("Criar campeonato");
        UiTheme.stylePrimaryButton(btnCriarCampeonato);
        btnCriarCampeonato.addActionListener(this::criarCampeonato);
        principal.add(btnCriarCampeonato, gbc);

        add(principal, BorderLayout.CENTER);
        atualizarListaClubes();
    }

    @Override
    public void atualizarDados() {
        atualizarListaClubes();
    }

    private void criarCampeonato(ActionEvent e) {
        try {
            String nomeCampeonato = txtNomeCampeonato.getText().trim();
            List<Clube> selecionados = new ArrayList<>(clubesSelecionados);
            campeonatoService.criarCampeonato(nomeCampeonato, selecionados);
            JOptionPane.showMessageDialog(this, "Campeonato '" + nomeCampeonato + "' criado com sucesso!");
            txtNomeCampeonato.setText("");
            clubesSelecionados.clear();
            listClubes.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListaClubes() {
        listModelClubes.clear();
        clubesSelecionados.clear();
        for (Clube clube : clubeRepository.obterTodos()) {
            listModelClubes.addElement(clube);
        }
        listClubes.repaint();
    }

    private class CheckBoxClubeRenderer implements ListCellRenderer<Clube> {
        private final JCheckBox checkBox = new JCheckBox();

        @Override
        public Component getListCellRendererComponent(JList<? extends Clube> list,
                                                      Clube value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            checkBox.setText(value != null ? value.toString() : "");
            checkBox.setSelected(clubesSelecionados.contains(value));
            checkBox.setBackground(UiTheme.BG_CARD);
            checkBox.setForeground(UiTheme.FG_PRIMARY);
            checkBox.setOpaque(true);
            checkBox.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            return checkBox;
        }
    }
}





