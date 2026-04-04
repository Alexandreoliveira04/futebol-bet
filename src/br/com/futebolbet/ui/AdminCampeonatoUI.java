package br.com.futebolbet.ui;

import br.com.futebolbet.models.Clube;
import br.com.futebolbet.repository.ClubeRepository;
import br.com.futebolbet.service.CampeonatoService;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AdminCampeonatoUI extends JPanel implements AtualizavelInterface {

    private JTextField txtNomeClube;
    private JButton btnCadastrarClube;

    private JTextField txtNomeCampeonato;
    private JList<Clube> listClubes;
    private DefaultListModel<Clube> listModelClubes;
    private JButton btnCriarCampeonato;

    private ClubeRepository clubeRepository;
    private CampeonatoService campeonatoService;

    public AdminCampeonatoUI() {
        this.clubeRepository = ClubeRepository.getInstance();
        this.campeonatoService = new CampeonatoService();

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
        JLabel lblTituloClube = new JLabel("1. Cadastrar novo clube");
        lblTituloClube.setFont(lblTituloClube.getFont().deriveFont(Font.BOLD, 14f));
        UiTheme.styleLabel(lblTituloClube, false);
        principal.add(lblTituloClube, gbc);

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
        JLabel lblTituloCamp = new JLabel("2. Criar campeonato (máx. 8 clubes)");
        lblTituloCamp.setFont(lblTituloCamp.getFont().deriveFont(Font.BOLD, 14f));
        UiTheme.styleLabel(lblTituloCamp, false);
        principal.add(lblTituloCamp, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JLabel n2 = new JLabel("Nome do campeonato:");
        UiTheme.styleLabel(n2, false);
        principal.add(n2, gbc);

        gbc.gridx = 1;
        txtNomeCampeonato = new JTextField(18);
        UiTheme.styleTextField(txtNomeCampeonato);
        principal.add(txtNomeCampeonato, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JLabel hint = new JLabel("Selecione os clubes (Ctrl + clique para vários):");
        UiTheme.styleLabel(hint, true);
        principal.add(hint, gbc);

        gbc.gridy = 7;
        listModelClubes = new DefaultListModel<>();
        listClubes = new JList<>(listModelClubes);
        listClubes.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listClubes.setVisibleRowCount(5);
        listClubes.setBackground(UiTheme.BG_CARD);
        listClubes.setForeground(UiTheme.FG_PRIMARY);
        JScrollPane scrollPane = new JScrollPane(listClubes);
        UiTheme.styleScrollPane(scrollPane);
        principal.add(scrollPane, gbc);

        gbc.gridy = 8;
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

    private void cadastrarClube(ActionEvent e) {
        String nome = txtNomeClube.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome do clube!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Clube novoClube = new Clube(nome);
        clubeRepository.adicionar(novoClube);

        JOptionPane.showMessageDialog(this, "Clube cadastrado com sucesso!");
        txtNomeClube.setText("");
        atualizarListaClubes();
    }

    private void criarCampeonato(ActionEvent e) {
        try {
            String nomeCampeonato = txtNomeCampeonato.getText().trim();

            List<Clube> clubesSelecionados = listClubes.getSelectedValuesList();

            campeonatoService.criarCampeonato(nomeCampeonato, clubesSelecionados);

            JOptionPane.showMessageDialog(this, "Campeonato '" + nomeCampeonato + "' criado com sucesso!");
            txtNomeCampeonato.setText("");
            listClubes.clearSelection();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListaClubes() {
        listModelClubes.clear();
        for (Clube clube : clubeRepository.obterTodos()) {
            listModelClubes.addElement(clube);
        }
    }
}
