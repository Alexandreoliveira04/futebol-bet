package br.com.futebolbet.ui;

import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Resultado;
import br.com.futebolbet.repository.PartidaRepository;
import br.com.futebolbet.service.ApostaService;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminResultadosUI extends JPanel implements AtualizavelInterface {

    private JComboBox<Partida> comboPartidas;
    private JSpinner spinnerGolsCasa;
    private JSpinner spinnerGolsFora;
    private JButton botaoRegistrar;
    private JLabel labelStatus;

    private PartidaRepository partidaRepository;
    private ApostaService apostaService;

    public AdminResultadosUI() {
        this.partidaRepository = PartidaRepository.getInstance();
        this.apostaService = new ApostaService();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel principal = new JPanel();
        UiTheme.applyPanel(principal);
        principal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Registrar resultado da partida");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        UiTheme.styleLabel(titulo, false);
        principal.add(titulo, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel l1 = new JLabel("Partida:");
        UiTheme.styleLabel(l1, false);
        principal.add(l1, gbc);

        gbc.gridx = 1;
        comboPartidas = new JComboBox<>();
        UiTheme.styleCombo(comboPartidas);
        principal.add(comboPartidas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel l2 = new JLabel("Gols mandante (casa):");
        UiTheme.styleLabel(l2, false);
        principal.add(l2, gbc);

        gbc.gridx = 1;
        spinnerGolsCasa = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        UiTheme.styleSpinner(spinnerGolsCasa);
        principal.add(spinnerGolsCasa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel l3 = new JLabel("Gols visitante (fora):");
        UiTheme.styleLabel(l3, false);
        principal.add(l3, gbc);

        gbc.gridx = 1;
        spinnerGolsFora = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        UiTheme.styleSpinner(spinnerGolsFora);
        principal.add(spinnerGolsFora, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        botaoRegistrar = new JButton("Registrar resultado");
        UiTheme.stylePrimaryButton(botaoRegistrar);
        botaoRegistrar.addActionListener(this::registrarResultado);
        principal.add(botaoRegistrar, gbc);

        gbc.gridy = 5;
        labelStatus = new JLabel("Pronto para registrar resultados.");
        UiTheme.styleLabel(labelStatus, true);
        principal.add(labelStatus, gbc);

        add(principal, BorderLayout.CENTER);
        atualizarDados();
    }

    @Override
    public void atualizarDados() {
        Partida sel = (Partida) comboPartidas.getSelectedItem();
        String ref = sel != null ? sel.toString() : null;
        comboPartidas.removeAllItems();
        for (Partida p : partidaRepository.obterTodas()) {
            comboPartidas.addItem(p);
        }
        if (ref != null) {
            for (int i = 0; i < comboPartidas.getItemCount(); i++) {
                Partida p = comboPartidas.getItemAt(i);
                if (p != null && ref.equals(p.toString())) {
                    comboPartidas.setSelectedIndex(i);
                    return;
                }
            }
        }
    }

    private void registrarResultado(ActionEvent e) {
        try {
            Partida partida = (Partida) comboPartidas.getSelectedItem();

            if (partida == null) {
                labelStatus.setText("Nenhuma partida selecionada.");
                return;
            }

            Integer golsCasa = (Integer) spinnerGolsCasa.getValue();
            Integer golsFora = (Integer) spinnerGolsFora.getValue();

            Resultado resultado = new Resultado(golsCasa, golsFora);
            partida.setResultado(resultado);

            apostaService.processarResultadosPartida(partida);

            labelStatus.setText("Resultado registrado. Pontos calculados.");
            JOptionPane.showMessageDialog(this, "Resultado registrado com sucesso!");
        } catch (Exception ex) {
            labelStatus.setText("Erro: " + ex.getMessage());
        }
    }
}
