package br.com.futebolbet.ui;

import br.com.futebolbet.controller.GrupoController;
import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminGruposUI extends JPanel implements AtualizavelInterface {

    private JTextField txtNomeGrupo;
    private DefaultListModel<String> listModelGrupos;
    private JLabel labelStatus;

    private final GrupoController grupoController;

    public AdminGruposUI() {
        this.grupoController = new GrupoController();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Gerenciar Grupos"), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarPainelLista(), BorderLayout.SOUTH);

        atualizarListaGrupos();
    }

    private JPanel criarFormulario() {
        JPanel card = new JPanel(new GridBagLayout());
        UiTheme.applyPanelCard(card);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lbl = new JLabel("Nome do grupo (maximo 5 grupos no sistema)");
        UiTheme.styleLabel(lbl, false);
        card.add(lbl, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 14, 0);
        txtNomeGrupo = new JTextField();
        UiTheme.styleTextField(txtNomeGrupo);
        txtNomeGrupo.addActionListener(e -> criarGrupo());
        card.add(txtNomeGrupo, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0);
        JButton btnCriar = new JButton("Criar grupo");
        UiTheme.stylePrimaryButton(btnCriar);
        btnCriar.addActionListener(e -> criarGrupo());
        card.add(btnCriar, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
        UiTheme.styleStatusLabel(labelStatus);
        card.add(labelStatus, gbc);

        return card;
    }

    private JPanel criarPainelLista() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        UiTheme.applyPanel(painel);

        painel.add(UiTheme.createSectionHeader("Grupos Existentes"), BorderLayout.NORTH);

        listModelGrupos = new DefaultListModel<>();
        JList<String> listGrupos = new JList<>(listModelGrupos);
        listGrupos.setBackground(UiTheme.BG_CARD);
        listGrupos.setForeground(UiTheme.FG_PRIMARY);
        listGrupos.setFont(new Font(Font.DIALOG, Font.PLAIN, 13));
        listGrupos.setFixedCellHeight(34);
        listGrupos.setEnabled(false);

        JScrollPane scroll = new JScrollPane(listGrupos);
        UiTheme.styleScrollPane(scroll);
        scroll.setPreferredSize(new Dimension(0, 180));
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    @Override
    public void atualizarDados() {
        atualizarListaGrupos();
    }

    private void criarGrupo() {
        try {
            grupoController.criarGrupo(txtNomeGrupo.getText());
            UiTheme.setLabelSuccess(labelStatus, "Grupo criado com sucesso!");
            txtNomeGrupo.setText("");
            atualizarListaGrupos();
        } catch (Exception ex) {
            UiTheme.setLabelError(labelStatus, ex.getMessage());
        }
    }

    private void atualizarListaGrupos() {
        listModelGrupos.clear();
        for (Grupo g : grupoController.listarGrupos()) {
            listModelGrupos.addElement(
                g.getNome() + "  —  " + g.getParticipantes().size() + " / 5 participantes"
            );
        }
    }
}
