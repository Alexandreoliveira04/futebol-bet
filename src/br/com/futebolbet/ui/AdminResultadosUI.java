package br.com.futebolbet.ui;

import br.com.futebolbet.controller.ResultadoController;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminResultadosUI extends JPanel implements AtualizavelInterface {

    private JComboBox<Partida> comboPartidas;
    private JSpinner spinnerGolsCasa;
    private JSpinner spinnerGolsFora;
    private JLabel labelStatus;

    private final ResultadoController resultadoController;

    public AdminResultadosUI() {
        this.resultadoController = new ResultadoController();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Registrar Resultado"), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);

        atualizarDados();
    }

    private JPanel criarFormulario() {
        JPanel card = new JPanel(new GridBagLayout());
        UiTheme.applyPanelCard(card);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lPartida = new JLabel("Partida");
        UiTheme.styleLabel(lPartida, false);
        card.add(lPartida, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 20, 0);
        comboPartidas = new JComboBox<>();
        UiTheme.styleCombo(comboPartidas);
        card.add(comboPartidas, gbc);

        JPanel placarPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        placarPanel.setOpaque(false);

        JPanel panelCasa = new JPanel(new GridLayout(2, 1, 0, 4));
        panelCasa.setOpaque(false);
        JLabel lCasa = new JLabel("Gols mandante (casa)");
        UiTheme.styleLabel(lCasa, false);
        spinnerGolsCasa = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        UiTheme.styleSpinner(spinnerGolsCasa);
        panelCasa.add(lCasa);
        panelCasa.add(spinnerGolsCasa);

        JLabel lX = new JLabel("X", SwingConstants.CENTER);
        lX.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
        lX.setForeground(UiTheme.ACCENT_GREEN);

        JPanel panelFora = new JPanel(new GridLayout(2, 1, 0, 4));
        panelFora.setOpaque(false);
        JLabel lFora = new JLabel("Gols visitante (fora)");
        UiTheme.styleLabel(lFora, false);
        spinnerGolsFora = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        UiTheme.styleSpinner(spinnerGolsFora);
        panelFora.add(lFora);
        panelFora.add(spinnerGolsFora);

        placarPanel.add(panelCasa);
        placarPanel.add(lX);
        placarPanel.add(panelFora);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 20, 0);
        card.add(placarPanel, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 10, 0);
        JButton btnRegistrar = new JButton("Registrar resultado");
        UiTheme.stylePrimaryButton(btnRegistrar);
        btnRegistrar.addActionListener(e -> registrarResultado());
        card.add(btnRegistrar, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel("Pronto para registrar resultados.");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        card.add(labelStatus, gbc);

        return card;
    }

    @Override
    public void atualizarDados() {
        Partida sel = (Partida) comboPartidas.getSelectedItem();
        String ref = sel != null ? sel.toString() : null;
        comboPartidas.removeAllItems();
        for (Partida p : resultadoController.listarPartidas()) comboPartidas.addItem(p);
        if (ref != null) {
            for (int i = 0; i < comboPartidas.getItemCount(); i++) {
                Partida p = comboPartidas.getItemAt(i);
                if (p != null && ref.equals(p.toString())) { comboPartidas.setSelectedIndex(i); return; }
            }
        }
    }

    private void registrarResultado() {
        try {
            Partida partida = (Partida) comboPartidas.getSelectedItem();
            int golsCasa = (Integer) spinnerGolsCasa.getValue();
            int golsFora = (Integer) spinnerGolsFora.getValue();
            resultadoController.registrarResultado(partida, golsCasa, golsFora);
            UiTheme.setLabelSuccess(labelStatus, "Resultado registrado. Pontos calculados!");
            JOptionPane.showMessageDialog(this, "Resultado registrado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }
}
