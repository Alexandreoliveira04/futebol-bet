package br.com.futebolbet.ui;

import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.service.GrupoService;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminGruposUI extends JPanel implements AtualizavelInterface {

    private JTextField txtNomeGrupo;
    private JButton btnCriarGrupo;
    private JList<String> listGrupos;
    private DefaultListModel<String> listModelGrupos;

    private GrupoService grupoService;

    public AdminGruposUI() {
        this.grupoService = new GrupoService();

        UiTheme.applyPanel(this);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Criar novo grupo (máx. 5 no sistema)");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 14f));
        UiTheme.styleLabel(lblTitulo, false);
        add(lblTitulo, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel n = new JLabel("Nome do grupo:");
        UiTheme.styleLabel(n, false);
        add(n, gbc);

        gbc.gridx = 1;
        txtNomeGrupo = new JTextField(15);
        UiTheme.styleTextField(txtNomeGrupo);
        add(txtNomeGrupo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        btnCriarGrupo = new JButton("Criar grupo");
        UiTheme.stylePrimaryButton(btnCriarGrupo);
        btnCriarGrupo.addActionListener(this::criarGrupo);
        add(btnCriarGrupo, gbc);

        gbc.gridy = 3;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0x00, 0x33, 0x55));
        add(sep, gbc);

        gbc.gridy = 4;
        JLabel lblLista = new JLabel("Grupos existentes");
        lblLista.setFont(lblLista.getFont().deriveFont(Font.BOLD, 14f));
        UiTheme.styleLabel(lblLista, false);
        add(lblLista, gbc);

        gbc.gridy = 5;
        listModelGrupos = new DefaultListModel<>();
        listGrupos = new JList<>(listModelGrupos);
        listGrupos.setVisibleRowCount(6);
        listGrupos.setBackground(UiTheme.BG_CARD);
        listGrupos.setForeground(UiTheme.FG_PRIMARY);
        JScrollPane scrollPane = new JScrollPane(listGrupos);
        UiTheme.styleScrollPane(scrollPane);
        add(scrollPane, gbc);

        atualizarListaGrupos();
    }

    @Override
    public void atualizarDados() {
        atualizarListaGrupos();
    }

    private void criarGrupo(ActionEvent e) {
        try {
            String nomeGrupo = txtNomeGrupo.getText().trim();
            if (nomeGrupo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite um nome para o grupo!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            grupoService.criarGrupo(nomeGrupo);

            JOptionPane.showMessageDialog(this, "Grupo '" + nomeGrupo + "' criado com sucesso!");
            txtNomeGrupo.setText("");
            atualizarListaGrupos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListaGrupos() {
        listModelGrupos.clear();
        for (Grupo g : grupoService.obterTodosGrupos()) {
            listModelGrupos.addElement(g.getNome() + " (" + g.getParticipantes().size() + "/5 participantes)");
        }
    }
}





