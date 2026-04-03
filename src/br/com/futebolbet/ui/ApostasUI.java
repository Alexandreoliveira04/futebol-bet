package br.com.futebolbet.ui;

import br.com.futebolbet.models.Aposta;
import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.repository.ApostaRepository;
import br.com.futebolbet.repository.PartidaRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

public class ApostasUI extends JFrame {
    private JComboBox<Partida> comboPartidas;
    private JComboBox<Integer> comboResultado;
    private JSpinner spinnerGolsCasa;
    private JSpinner spinnerGolsFora;
    private JButton botaoApostar;
    private JLabel labelStatus;

    private Grupo grupo;
    private Participante participanteAtual;
    private PartidaRepository partidaRepository;
    private ApostaRepository apostaRepository;

    public ApostasUI(Grupo grupo, Participante participante) {
        this.grupo = grupo;
        this.participanteAtual = participante;
        this.partidaRepository = PartidaRepository.getInstance();
        this.apostaRepository = ApostaRepository.getInstance();

        setTitle("Registrar Apostas - " + participante.getNome());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel();
        principal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Partidas
        gbc.gridx = 0;
        gbc.gridy = 0;
        principal.add(new JLabel("Partida:"), gbc);

        gbc.gridx = 1;
        Partida[] partidas = partidaRepository.obterTodas().toArray(new Partida[0]);
        comboPartidas = new JComboBox<>(partidas);
        principal.add(comboPartidas, gbc);

        // Resultado Esperado
        gbc.gridx = 0;
        gbc.gridy = 1;
        principal.add(new JLabel("Resultado Esperado:"), gbc);

        gbc.gridx = 1;
        comboResultado = new JComboBox<>(new String[]{"Casa", "Empate", "Fora"});
        principal.add(comboResultado, gbc);

        // Gols Casa
        gbc.gridx = 0;
        gbc.gridy = 2;
        principal.add(new JLabel("Gols Casa:"), gbc);

        gbc.gridx = 1;
        spinnerGolsCasa = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        principal.add(spinnerGolsCasa, gbc);

        // Gols Fora
        gbc.gridx = 0;
        gbc.gridy = 3;
        principal.add(new JLabel("Gols Fora:"), gbc);

        gbc.gridx = 1;
        spinnerGolsFora = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        principal.add(spinnerGolsFora, gbc);

        // Botão Apostar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        botaoApostar = new JButton("Registrar Aposta");
        botaoApostar.addActionListener(this::registrarAposta);
        principal.add(botaoApostar, gbc);

        // Label Status
        gbc.gridy = 5;
        labelStatus = new JLabel("Pronto para apostar");
        principal.add(labelStatus, gbc);

        add(principal);
        setVisible(true);
    }

    private void registrarAposta(ActionEvent e) {
        try {
            Partida partida = (Partida) comboPartidas.getSelectedItem();
            Integer resultado = comboResultado.getSelectedIndex();
            Integer golsCasa = (Integer) spinnerGolsCasa.getValue();
            Integer golsFora = (Integer) spinnerGolsFora.getValue();

            if (partida == null) {
                labelStatus.setText("Erro: Nenhuma partida selecionada!");
                return;
            }

            // Validar se não é menos de 20 minutos antes da partida
            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime limite = partida.getDataHora().minusMinutes(20);

            if (agora.isAfter(limite)) {
                labelStatus.setText("Erro: Apostas só até 20 minutos antes!");
                return;
            }

            Aposta aposta = new Aposta(participanteAtual, partida, resultado, golsCasa);
            apostaRepository.adicionar(aposta);
            grupo.adicionarAposta(aposta);

            labelStatus.setText("Aposta registrada com sucesso!");
            JOptionPane.showMessageDialog(this, "Aposta registrada!");
        } catch (Exception ex) {
            labelStatus.setText("Erro: " + ex.getMessage());
        }
    }
}
