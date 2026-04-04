package br.com.futebolbet.ui;

import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.service.GrupoService;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ParticipanteGruposUI extends JPanel implements AtualizavelInterface {

    private JComboBox<String> comboGrupos;
    private JButton btnIngressar;

    private final GrupoService grupoService;
    private final Participante participanteLogado;
    private final Runnable aposIngressar;

    public ParticipanteGruposUI(Participante participante) {
        this(participante, null);
    }

    public ParticipanteGruposUI(Participante participante, Runnable aposIngressar) {
        this.participanteLogado = participante;
        this.grupoService = new GrupoService();
        this.aposIngressar = aposIngressar;

        UiTheme.applyPanel(this);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel("Selecione um grupo para ingressar:");
        UiTheme.styleLabel(titulo, false);
        add(titulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        comboGrupos = new JComboBox<>();
        UiTheme.styleCombo(comboGrupos);
        atualizarComboGrupos();
        add(comboGrupos, gbc);

        gbc.gridx = 1;
        btnIngressar = new JButton("Ingressar");
        UiTheme.stylePrimaryButton(btnIngressar);
        btnIngressar.addActionListener(this::ingressarGrupo);
        add(btnIngressar, gbc);
    }

    @Override
    public void atualizarDados() {
        atualizarComboGrupos();
    }

    private void ingressarGrupo(ActionEvent e) {
        try {
            String nomeSelecionado = (String) comboGrupos.getSelectedItem();
            if (nomeSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Nenhum grupo selecionado!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Grupo grupo = grupoService.obterTodosGrupos().stream()
                    .filter(g -> g.getNome().equals(nomeSelecionado))
                    .findFirst()
                    .orElse(null);

            if (grupo != null) {
                grupoService.ingressarNoGrupo(grupo, participanteLogado);
                JOptionPane.showMessageDialog(this, "Você ingressou no grupo '" + grupo.getNome() + "' com sucesso!");
                if (aposIngressar != null) {
                    aposIngressar.run();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao ingressar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarComboGrupos() {
        comboGrupos.removeAllItems();
        for (Grupo g : grupoService.obterTodosGrupos()) {
            comboGrupos.addItem(g.getNome());
        }
    }
}
