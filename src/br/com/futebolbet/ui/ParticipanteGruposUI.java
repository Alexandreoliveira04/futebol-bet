package br.com.futebolbet.ui;

import br.com.futebolbet.controller.GrupoController;
import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ParticipanteGruposUI extends JPanel implements AtualizavelInterface {

    private JTextField txtNomeGrupo;
    private JComboBox<String> comboGrupos;
    private JLabel labelStatus;

    private final GrupoController grupoController;
    private final Participante participanteLogado;
    private final Runnable aposIngressar;

    public ParticipanteGruposUI(Participante participante) {
        this(participante, null);
    }

    public ParticipanteGruposUI(Participante participante, Runnable aposIngressar) {
        this.participanteLogado = participante;
        this.grupoController = new GrupoController();
        this.aposIngressar = aposIngressar;

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Grupos"), BorderLayout.NORTH);
        add(criarPainelCriar(), BorderLayout.CENTER);
        add(criarPainelIngressar(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelCriar() {
        JPanel card = new JPanel(new GridBagLayout());
        UiTheme.applyPanelCard(card);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel lblSub = new JLabel("Criar novo grupo");
        lblSub.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        lblSub.setForeground(UiTheme.FG_PRIMARY);
        card.add(lblSub, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lbl = new JLabel("Nome do grupo");
        UiTheme.styleLabel(lbl, false);
        card.add(lbl, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 12, 0);
        txtNomeGrupo = new JTextField();
        UiTheme.styleTextField(txtNomeGrupo);
        txtNomeGrupo.addActionListener(e -> criarGrupo());
        card.add(txtNomeGrupo, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 8, 0);
        JButton btnCriar = new JButton("Criar grupo");
        UiTheme.stylePrimaryButton(btnCriar);
        btnCriar.addActionListener(e -> criarGrupo());
        card.add(btnCriar, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        card.add(labelStatus, gbc);

        return card;
    }

    private JPanel criarPainelIngressar() {
        JPanel card = new JPanel(new GridBagLayout());
        UiTheme.applyPanelCard(card);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        JLabel lblSub = new JLabel("Ingressar em grupo existente");
        lblSub.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        lblSub.setForeground(UiTheme.FG_PRIMARY);
        card.add(lblSub, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 8);
        comboGrupos = new JComboBox<>();
        UiTheme.styleCombo(comboGrupos);
        atualizarComboGrupos();
        card.add(comboGrupos, gbc);

        gbc.gridx = 1; gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton btnIngressar = new JButton("Ingressar");
        UiTheme.stylePrimaryButton(btnIngressar);
        btnIngressar.addActionListener(e -> ingressarGrupo());
        card.add(btnIngressar, gbc);

        return card;
    }

    @Override
    public void atualizarDados() {
        atualizarComboGrupos();
    }

    private void criarGrupo() {
        try {
            grupoController.criarGrupo(txtNomeGrupo.getText());
            UiTheme.setLabelSuccess(labelStatus, "Grupo criado!");
            String nome = txtNomeGrupo.getText().trim();
            txtNomeGrupo.setText("");
            atualizarComboGrupos();
            for (int i = 0; i < comboGrupos.getItemCount(); i++) {
                if (nome.equals(comboGrupos.getItemAt(i))) { comboGrupos.setSelectedIndex(i); break; }
            }
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }

    private void ingressarGrupo() {
        try {
            String nomeSelecionado = (String) comboGrupos.getSelectedItem();
            if (nomeSelecionado == null || nomeSelecionado.isEmpty()) {
                UiTheme.setLabelError(labelStatus, "Nenhum grupo selecionado.");
                return;
            }
            grupoController.ingressarNoGrupo(nomeSelecionado, participanteLogado);
            UiTheme.setLabelSuccess(labelStatus, "Ingressou no grupo '" + nomeSelecionado + "'!");
            if (aposIngressar != null) aposIngressar.run();
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }

    private void atualizarComboGrupos() {
        comboGrupos.removeAllItems();
        for (Grupo g : grupoController.listarGrupos()) {
            comboGrupos.addItem(g.getNome());
        }
    }
}
