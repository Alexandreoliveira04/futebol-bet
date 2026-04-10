package br.com.futebolbet.ui;

import br.com.futebolbet.models.Campeonato;
import br.com.futebolbet.models.Clube;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.repository.CampeonatoRepository;
import br.com.futebolbet.repository.ClubeRepository;
import br.com.futebolbet.repository.PartidaRepository;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AdminPartidasUI extends JPanel implements AtualizavelInterface {

    private JComboBox<Campeonato> comboCampeonato;
    private JComboBox<Clube> comboCasa;
    private JComboBox<Clube> comboFora;
    private JSpinner spinnerDataHora;
    private JButton btnAgendar;
    private JButton btnAtualizar;

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;
    private final CampeonatoRepository campeonatoRepository;

    public AdminPartidasUI() {
        this.clubeRepository = ClubeRepository.getInstance();
        this.partidaRepository = PartidaRepository.getInstance();
        this.campeonatoRepository = CampeonatoRepository.getInstance();

        UiTheme.applyPanel(this);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JPanel linhaTitulo = new JPanel(new BorderLayout(12, 0));
        UiTheme.applyPanel(linhaTitulo);
        JLabel lblTitulo = new JLabel("Agendar partida de campeonato");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        UiTheme.styleLabel(lblTitulo, false);
        linhaTitulo.add(lblTitulo, BorderLayout.WEST);
        btnAtualizar = new JButton("Atualizar listas");
        UiTheme.styleSecondaryButton(btnAtualizar);
        btnAtualizar.addActionListener(e -> atualizarDados());
        linhaTitulo.add(btnAtualizar, BorderLayout.EAST);
        add(linhaTitulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel l1 = new JLabel("Campeonato:");
        UiTheme.styleLabel(l1, false);
        add(l1, gbc);
        gbc.gridx = 1;
        comboCampeonato = new JComboBox<>();
        UiTheme.styleCombo(comboCampeonato);
        add(comboCampeonato, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel l2 = new JLabel("Clube mandante (casa):");
        UiTheme.styleLabel(l2, false);
        add(l2, gbc);
        gbc.gridx = 1;
        comboCasa = new JComboBox<>();
        UiTheme.styleCombo(comboCasa);
        add(comboCasa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel l3 = new JLabel("Clube visitante (fora):");
        UiTheme.styleLabel(l3, false);
        add(l3, gbc);
        gbc.gridx = 1;
        comboFora = new JComboBox<>();
        UiTheme.styleCombo(comboFora);
        add(comboFora, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel l4 = new JLabel("Data e horário:");
        UiTheme.styleLabel(l4, false);
        add(l4, gbc);
        gbc.gridx = 1;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        spinnerDataHora = new JSpinner(dateModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerDataHora, "dd/MM/yyyy HH:mm");
        spinnerDataHora.setEditor(timeEditor);
        spinnerDataHora.setValue(new Date());
        UiTheme.styleSpinner(spinnerDataHora);
        add(spinnerDataHora, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        btnAgendar = new JButton("Confirmar agendamento");
        UiTheme.stylePrimaryButton(btnAgendar);
        btnAgendar.addActionListener(this::agendarPartida);
        add(btnAgendar, gbc);

        atualizarDados();
    }

    @Override
    public void atualizarDados() {
        Campeonato cSel = (Campeonato) comboCampeonato.getSelectedItem();
        String nomeCamp = cSel != null ? cSel.getNome() : null;
        Clube casaSel = (Clube) comboCasa.getSelectedItem();
        Clube foraSel = (Clube) comboFora.getSelectedItem();
        String nomeCasa = casaSel != null ? casaSel.getNome() : null;
        String nomeFora = foraSel != null ? foraSel.getNome() : null;

        comboCampeonato.removeAllItems();
        for (Campeonato c : campeonatoRepository.obterTodos()) {
            comboCampeonato.addItem(c);
        }
        selecionarCampeonatoPorNome(nomeCamp);

        comboCasa.removeAllItems();
        comboFora.removeAllItems();
        for (Clube cl : clubeRepository.obterTodos()) {
            comboCasa.addItem(cl);
            comboFora.addItem(cl);
        }
        selecionarClubePorNome(comboCasa, nomeCasa);
        selecionarClubePorNome(comboFora, nomeFora);
    }

    private void selecionarCampeonatoPorNome(String nome) {
        if (nome == null) {
            return;
        }
        for (int i = 0; i < comboCampeonato.getItemCount(); i++) {
            Campeonato c = comboCampeonato.getItemAt(i);
            if (c != null && nome.equals(c.getNome())) {
                comboCampeonato.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selecionarClubePorNome(JComboBox<Clube> combo, String nome) {
        if (nome == null) {
            return;
        }
        for (int i = 0; i < combo.getItemCount(); i++) {
            Clube cl = combo.getItemAt(i);
            if (cl != null && nome.equals(cl.getNome())) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void agendarPartida(ActionEvent e) {
        Campeonato campeonato = (Campeonato) comboCampeonato.getSelectedItem();
        Clube clubeCasa = (Clube) comboCasa.getSelectedItem();
        Clube clubeFora = (Clube) comboFora.getSelectedItem();
        Date dataHoraSelecionada = (Date) spinnerDataHora.getValue();

        if (campeonato == null || clubeCasa == null || clubeFora == null) {
            JOptionPane.showMessageDialog(this, "Todos os campos de seleção são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (clubeCasa.equals(clubeFora)) {
            JOptionPane.showMessageDialog(this, "Confronto inválido: um clube não pode jogar contra si mesmo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime dataHora = dataHoraSelecionada.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Partida novaPartida = new Partida(campeonato, clubeCasa, clubeFora, dataHora);
        partidaRepository.adicionar(novaPartida);

        JOptionPane.showMessageDialog(this, "Partida agendada com sucesso para o campeonato: " + campeonato.getNome());
    }
}





