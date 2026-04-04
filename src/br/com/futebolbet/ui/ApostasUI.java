package br.com.futebolbet.ui;

import br.com.futebolbet.enums.TipoResultado;
import br.com.futebolbet.models.Aposta;
import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.repository.PartidaRepository;
import br.com.futebolbet.service.ApostaService;
import br.com.futebolbet.service.GrupoService;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ApostasUI extends JPanel implements AtualizavelInterface {

    private JComboBox<Grupo> comboGrupo;
    private JComboBox<Partida> comboPartidas;
    private JComboBox<TipoResultado> comboResultado;
    private JSpinner spinnerGolsCasa;
    private JSpinner spinnerGolsFora;
    private JButton botaoApostar;
    private JLabel labelStatus;

    private final Participante participanteAtual;
    private final PartidaRepository partidaRepository;
    private final ApostaService apostaService;
    private final GrupoService grupoService;

    public ApostasUI(Participante participante) {
        this.participanteAtual = participante;
        this.partidaRepository = PartidaRepository.getInstance();
        this.apostaService = new ApostaService();
        this.grupoService = new GrupoService();

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
        JLabel lGrupo = new JLabel("Grupo:");
        UiTheme.styleLabel(lGrupo, false);
        principal.add(lGrupo, gbc);

        gbc.gridx = 1;
        comboGrupo = new JComboBox<>();
        UiTheme.styleCombo(comboGrupo);
        comboGrupo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Grupo) {
                    setText(((Grupo) value).getNome());
                }
                c.setForeground(UiTheme.FG_PRIMARY);
                c.setBackground(isSelected ? UiTheme.ACCENT_BLUE : UiTheme.BG_CARD);
                return c;
            }
        });
        principal.add(comboGrupo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lPartida = new JLabel("Partida:");
        UiTheme.styleLabel(lPartida, false);
        principal.add(lPartida, gbc);

        gbc.gridx = 1;
        comboPartidas = new JComboBox<>();
        UiTheme.styleCombo(comboPartidas);
        principal.add(comboPartidas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lRes = new JLabel("Resultado esperado:");
        UiTheme.styleLabel(lRes, false);
        principal.add(lRes, gbc);

        gbc.gridx = 1;
        comboResultado = new JComboBox<>(TipoResultado.values());
        UiTheme.styleCombo(comboResultado);
        comboResultado.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TipoResultado) {
                    setText(((TipoResultado) value).getDescricao());
                }
                c.setForeground(UiTheme.FG_PRIMARY);
                c.setBackground(isSelected ? UiTheme.ACCENT_BLUE : UiTheme.BG_CARD);
                return c;
            }
        });
        principal.add(comboResultado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lGc = new JLabel("Gols casa:");
        UiTheme.styleLabel(lGc, false);
        principal.add(lGc, gbc);

        gbc.gridx = 1;
        spinnerGolsCasa = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        UiTheme.styleSpinner(spinnerGolsCasa);
        principal.add(spinnerGolsCasa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lGf = new JLabel("Gols fora:");
        UiTheme.styleLabel(lGf, false);
        principal.add(lGf, gbc);

        gbc.gridx = 1;
        spinnerGolsFora = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        UiTheme.styleSpinner(spinnerGolsFora);
        principal.add(spinnerGolsFora, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        botaoApostar = new JButton("Registrar aposta");
        UiTheme.stylePrimaryButton(botaoApostar);
        botaoApostar.addActionListener(this::registrarAposta);
        principal.add(botaoApostar, gbc);

        gbc.gridy = 6;
        labelStatus = new JLabel(" ");
        UiTheme.styleLabel(labelStatus, true);
        principal.add(labelStatus, gbc);

        add(principal, BorderLayout.CENTER);

        atualizarDados();
    }

    @Override
    public void atualizarDados() {
        recarregarGrupos();
        recarregarPartidas();
    }

    public void recarregarGrupos() {
        Grupo gSel = (Grupo) comboGrupo.getSelectedItem();
        String nomeGrupo = gSel != null ? gSel.getNome() : null;
        comboGrupo.removeAllItems();
        for (Grupo g : grupoService.obterGruposDoParticipante(participanteAtual)) {
            comboGrupo.addItem(g);
        }
        if (nomeGrupo != null) {
            for (int i = 0; i < comboGrupo.getItemCount(); i++) {
                Grupo g = comboGrupo.getItemAt(i);
                if (g != null && nomeGrupo.equals(g.getNome())) {
                    comboGrupo.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (comboGrupo.getItemCount() == 0) {
            labelStatus.setText("Ingressa em um grupo na aba Grupos para apostar.");
        } else {
            labelStatus.setText("Pronto para apostar.");
        }
    }

    private void recarregarPartidas() {
        Partida pSel = (Partida) comboPartidas.getSelectedItem();
        String ref = pSel != null ? pSel.toString() : null;
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

    private void registrarAposta(ActionEvent e) {
        try {
            Grupo grupo = (Grupo) comboGrupo.getSelectedItem();
            Partida partida = (Partida) comboPartidas.getSelectedItem();
            TipoResultado resultado = (TipoResultado) comboResultado.getSelectedItem();
            Integer golsCasa = (Integer) spinnerGolsCasa.getValue();
            Integer golsFora = (Integer) spinnerGolsFora.getValue();

            if (grupo == null) {
                labelStatus.setText("Selecione um grupo ou ingresse em um grupo antes.");
                return;
            }

            if (partida == null) {
                labelStatus.setText("Nenhuma partida disponível.");
                return;
            }

            if (resultado == null) {
                labelStatus.setText("Selecione o resultado esperado.");
                return;
            }

            Aposta aposta = new Aposta(participanteAtual, partida, resultado, golsCasa, golsFora);

            apostaService.registrarAposta(aposta);

            grupo.adicionarAposta(aposta);

            labelStatus.setText("Aposta registrada com sucesso.");
            JOptionPane.showMessageDialog(this, "Aposta registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            labelStatus.setText("Erro: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }
}
