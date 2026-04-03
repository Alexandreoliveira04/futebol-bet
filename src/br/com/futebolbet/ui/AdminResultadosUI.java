package br.com.futebolbet.ui;

import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Resultado;
import br.com.futebolbet.repository.ApostaRepository;
import br.com.futebolbet.repository.PartidaRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminResultadosUI extends JFrame {
    private JComboBox<Partida> comboPartidas;
    private JSpinner spinnerGolsCasa;
    private JSpinner spinnerGolsFora;
    private JButton botaoRegistrar;
    private JLabel labelStatus;

    private PartidaRepository partidaRepository;
    private ApostaRepository apostaRepository;

    public AdminResultadosUI() {
        this.partidaRepository = PartidaRepository.getInstance();
        this.apostaRepository = ApostaRepository.getInstance();

        setTitle("Registrar Resultados - Administrador");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel();
        principal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Registrar Resultado da Partida");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        principal.add(titulo, gbc);
        gbc.gridwidth = 1;

        // Partidas
        gbc.gridx = 0;
        gbc.gridy = 1;
        principal.add(new JLabel("Selecione a Partida:"), gbc);

        gbc.gridx = 1;
        Partida[] partidas = partidaRepository.obterTodas().toArray(new Partida[0]);
        comboPartidas = new JComboBox<>(partidas);
        principal.add(comboPartidas, gbc);

        // Gols Casa
        gbc.gridx = 0;
        gbc.gridy = 2;
        principal.add(new JLabel("Gols - " + (partidasNaoVazia() ? ((Partida) comboPartidas.getSelectedItem()).getClubeCasa().getNome() : "Casa") + ":"), gbc);

        gbc.gridx = 1;
        spinnerGolsCasa = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        principal.add(spinnerGolsCasa, gbc);

        // Gols Fora
        gbc.gridx = 0;
        gbc.gridy = 3;
        principal.add(new JLabel("Gols - " + (partidasNaoVazia() ? ((Partida) comboPartidas.getSelectedItem()).getClubeFora().getNome() : "Fora") + ":"), gbc);

        gbc.gridx = 1;
        spinnerGolsFora = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        principal.add(spinnerGolsFora, gbc);

        // Botão Registrar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        botaoRegistrar = new JButton("Registrar Resultado");
        botaoRegistrar.addActionListener(this::registrarResultado);
        principal.add(botaoRegistrar, gbc);

        // Label Status
        gbc.gridy = 5;
        labelStatus = new JLabel("Pronto para registrar resultados");
        principal.add(labelStatus, gbc);

        add(principal);
        setVisible(true);
    }

    private boolean partidasNaoVazia() {
        return comboPartidas.getItemCount() > 0;
    }

    private void registrarResultado(ActionEvent e) {
        try {
            Partida partida = (Partida) comboPartidas.getSelectedItem();

            if (partida == null) {
                labelStatus.setText("Erro: Nenhuma partida selecionada!");
                return;
            }

            Integer golsCasa = (Integer) spinnerGolsCasa.getValue();
            Integer golsFora = (Integer) spinnerGolsFora.getValue();

            Resultado resultado = new Resultado(golsCasa, golsFora);
            partida.setResultado(resultado);

            // Calcular pontos de todas as apostas dessa partida
            var apostasPartida = apostaRepository.obterPorPartida(partida);
            for (var aposta : apostasPartida) {
                aposta.calcularPontos(resultado);
                aposta.getParticipante().adicionarPontos(aposta.getPontos());
            }

            labelStatus.setText("Resultado registrado! Pontos calculados.");
            JOptionPane.showMessageDialog(this, "Resultado registrado com sucesso!");
        } catch (Exception ex) {
            labelStatus.setText("Erro: " + ex.getMessage());
        }
    }
}
