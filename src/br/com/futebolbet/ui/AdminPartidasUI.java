package br.com.futebolbet.ui;

import br.com.futebolbet.controller.PartidaController;
import br.com.futebolbet.models.Campeonato;
import br.com.futebolbet.models.Clube;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AdminPartidasUI extends JPanel implements AtualizavelInterface {

    private JComboBox<Campeonato> comboCampeonato;
    private JComboBox<Clube> comboCasa;
    private JComboBox<Clube> comboFora;
    private JSpinner spinnerDataHora;
    private JLabel labelStatus;

    private final PartidaController partidaController;

    public AdminPartidasUI() {
        this.partidaController = new PartidaController();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Agendar Partida"), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);

        atualizarDados();
    }

    private JPanel criarFormulario() {
        JPanel card = new JPanel(new GridBagLayout());
        UiTheme.applyPanelCard(card);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Object[][] campos = {
            {"Campeonato", null},
            {"Clube mandante (casa)", null},
            {"Clube visitante (fora)", null},
            {"Data e horario", null}
        };

        int row = 0;
        for (int i = 0; i < campos.length; i++) {
            gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
            gbc.insets = new Insets(0, 0, 4, 0);
            JLabel lbl = new JLabel((String) campos[i][0]);
            UiTheme.styleLabel(lbl, false);
            card.add(lbl, gbc);

            gbc.gridy = row++; gbc.insets = new Insets(0, 0, 14, 0);
            if (i == 0) {
                comboCampeonato = new JComboBox<>();
                UiTheme.styleCombo(comboCampeonato);
                card.add(comboCampeonato, gbc);
            } else if (i == 1) {
                comboCasa = new JComboBox<>();
                UiTheme.styleCombo(comboCasa);
                card.add(comboCasa, gbc);
            } else if (i == 2) {
                comboFora = new JComboBox<>();
                UiTheme.styleCombo(comboFora);
                card.add(comboFora, gbc);
            } else {
                SpinnerDateModel dateModel = new SpinnerDateModel();
                spinnerDataHora = new JSpinner(dateModel);
                spinnerDataHora.setEditor(new JSpinner.DateEditor(spinnerDataHora, "dd/MM/yyyy HH:mm"));
                spinnerDataHora.setValue(new Date());
                UiTheme.styleSpinner(spinnerDataHora);
                card.add(spinnerDataHora, gbc);
            }
        }

        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 8, 0);
        JButton btnAgendar = new JButton("Confirmar agendamento");
        UiTheme.stylePrimaryButton(btnAgendar);
        btnAgendar.addActionListener(e -> agendarPartida());
        card.add(btnAgendar, gbc);

        gbc.gridy = row; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        card.add(labelStatus, gbc);

        return card;
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
        for (Campeonato c : partidaController.listarCampeonatos()) comboCampeonato.addItem(c);
        selecionarPorNomeCampeonato(nomeCamp);

        comboCasa.removeAllItems();
        comboFora.removeAllItems();
        for (Clube cl : partidaController.listarClubes()) {
            comboCasa.addItem(cl);
            comboFora.addItem(cl);
        }
        selecionarPorNomeClube(comboCasa, nomeCasa);
        selecionarPorNomeClube(comboFora, nomeFora);
    }

    private void selecionarPorNomeCampeonato(String nome) {
        if (nome == null) return;
        for (int i = 0; i < comboCampeonato.getItemCount(); i++) {
            Campeonato c = comboCampeonato.getItemAt(i);
            if (c != null && nome.equals(c.getNome())) { comboCampeonato.setSelectedIndex(i); return; }
        }
    }

    private void selecionarPorNomeClube(JComboBox<Clube> combo, String nome) {
        if (nome == null) return;
        for (int i = 0; i < combo.getItemCount(); i++) {
            Clube cl = combo.getItemAt(i);
            if (cl != null && nome.equals(cl.getNome())) { combo.setSelectedIndex(i); return; }
        }
    }

    private void agendarPartida() {
        try {
            Campeonato campeonato = (Campeonato) comboCampeonato.getSelectedItem();
            Clube clubeCasa = (Clube) comboCasa.getSelectedItem();
            Clube clubeFora = (Clube) comboFora.getSelectedItem();
            Date dataSel = (Date) spinnerDataHora.getValue();
            LocalDateTime dataHora = dataSel.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            partidaController.agendarPartida(campeonato, clubeCasa, clubeFora, dataHora);
            UiTheme.setLabelSuccess(labelStatus, "Partida agendada com sucesso!");
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }
}
