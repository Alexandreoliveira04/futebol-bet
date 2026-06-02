package br.com.futebolbet.ui;

import br.com.futebolbet.controller.ApostaController;
import br.com.futebolbet.enums.TipoResultado;
import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ApostasUI extends JPanel implements AtualizavelInterface {

    private JComboBox<Grupo> comboGrupo;
    private JComboBox<Partida> comboPartidas;
    private JComboBox<TipoResultado> comboResultado;
    private JSpinner spinnerGolsCasa;
    private JSpinner spinnerGolsFora;
    private JLabel labelStatus;

    private final Participante participanteAtual;
    private final ApostaController apostaController;

    public ApostasUI(Participante participante) {
        this.participanteAtual = participante;
        this.apostaController = new ApostaController();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Registrar Aposta"), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);

        atualizarDados();
    }

    private JPanel criarFormulario() {
        JPanel card = new JPanel(new GridBagLayout());
        UiTheme.applyPanelCard(card);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        row = addLabelCombo(card, gbc, row, "Grupo", null);
        comboGrupo = new JComboBox<>();
        UiTheme.styleCombo(comboGrupo);
        comboGrupo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i,
                    boolean sel, boolean focus) {
                Component c = super.getListCellRendererComponent(l, v, i, sel, focus);
                if (v instanceof Grupo) setText(((Grupo) v).getNome());
                c.setForeground(UiTheme.FG_PRIMARY);
                c.setBackground(sel ? UiTheme.ACCENT_BLUE : UiTheme.BG_CARD);
                return c;
            }
        });
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 14, 0);
        card.add(comboGrupo, gbc);

        row = addLabelCombo(card, gbc, row, "Partida", null);
        comboPartidas = new JComboBox<>();
        UiTheme.styleCombo(comboPartidas);
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 14, 0);
        card.add(comboPartidas, gbc);

        row = addLabelCombo(card, gbc, row, "Resultado esperado", null);
        comboResultado = new JComboBox<>(TipoResultado.values());
        UiTheme.styleCombo(comboResultado);
        comboResultado.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> l, Object v, int i,
                    boolean sel, boolean focus) {
                Component c = super.getListCellRendererComponent(l, v, i, sel, focus);
                if (v instanceof TipoResultado) setText(((TipoResultado) v).getDescricao());
                c.setForeground(UiTheme.FG_PRIMARY);
                c.setBackground(sel ? UiTheme.ACCENT_BLUE : UiTheme.BG_CARD);
                return c;
            }
        });
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 16, 0);
        card.add(comboResultado, gbc);

        JPanel placarPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        placarPanel.setOpaque(false);

        JPanel panelCasa = new JPanel(new GridLayout(2, 1, 0, 4));
        panelCasa.setOpaque(false);
        JLabel lCasa = new JLabel("Gols casa");
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
        JLabel lFora = new JLabel("Gols fora");
        UiTheme.styleLabel(lFora, false);
        spinnerGolsFora = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        UiTheme.styleSpinner(spinnerGolsFora);
        panelFora.add(lFora);
        panelFora.add(spinnerGolsFora);

        placarPanel.add(panelCasa);
        placarPanel.add(lX);
        placarPanel.add(panelFora);

        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 18, 0);
        card.add(placarPanel, gbc);

        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 10, 0);
        JButton btnApostar = new JButton("Registrar aposta");
        UiTheme.stylePrimaryButton(btnApostar);
        btnApostar.addActionListener(e -> registrarAposta());
        card.add(btnApostar, gbc);

        gbc.gridy = row; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        card.add(labelStatus, gbc);

        return card;
    }

    private int addLabelCombo(JPanel card, GridBagConstraints gbc, int row,
                              String texto, Object ignored) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel lbl = new JLabel(texto);
        UiTheme.styleLabel(lbl, false);
        card.add(lbl, gbc);
        return row + 1;
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
        for (Grupo g : apostaController.listarGruposDoParticipante(participanteAtual)) {
            comboGrupo.addItem(g);
        }
        if (nomeGrupo != null) {
            for (int i = 0; i < comboGrupo.getItemCount(); i++) {
                Grupo g = comboGrupo.getItemAt(i);
                if (g != null && nomeGrupo.equals(g.getNome())) { comboGrupo.setSelectedIndex(i); break; }
            }
        }
        if (comboGrupo.getItemCount() == 0) {
            UiTheme.setLabelNeutral(labelStatus, "Ingresse em um grupo na aba Grupos para apostar.");
        } else {
            UiTheme.setLabelNeutral(labelStatus, " ");
        }
    }

    private void recarregarPartidas() {
        Partida pSel = (Partida) comboPartidas.getSelectedItem();
        String ref = pSel != null ? pSel.toString() : null;
        comboPartidas.removeAllItems();
        for (Partida p : apostaController.listarPartidas()) comboPartidas.addItem(p);
        if (ref != null) {
            for (int i = 0; i < comboPartidas.getItemCount(); i++) {
                Partida p = comboPartidas.getItemAt(i);
                if (p != null && ref.equals(p.toString())) { comboPartidas.setSelectedIndex(i); return; }
            }
        }
    }

    private void registrarAposta() {
        try {
            Grupo grupo = (Grupo) comboGrupo.getSelectedItem();
            Partida partida = (Partida) comboPartidas.getSelectedItem();
            TipoResultado resultado = (TipoResultado) comboResultado.getSelectedItem();
            int golsCasa = (Integer) spinnerGolsCasa.getValue();
            int golsFora = (Integer) spinnerGolsFora.getValue();

            apostaController.registrarAposta(grupo, partida, resultado,
                    golsCasa, golsFora, participanteAtual);

            UiTheme.setLabelSuccess(labelStatus, "Aposta registrada com sucesso!");
            JOptionPane.showMessageDialog(this, "Aposta registrada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }
}
