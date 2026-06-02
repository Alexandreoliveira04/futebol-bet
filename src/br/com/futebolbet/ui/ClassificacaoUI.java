package br.com.futebolbet.ui;

import br.com.futebolbet.controller.ClassificacaoController;
import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClassificacaoUI extends JPanel implements AtualizavelInterface {

    private final JTable tabelaClassificacao;
    private final DefaultTableModel modeloTabela;
    private final JComboBox<Grupo> comboGrupos;
    private final ClassificacaoController classificacaoController;
    private final Participante filtroParticipante;

    public ClassificacaoUI() {
        this(null);
    }

    public ClassificacaoUI(Participante participante) {
        this.filtroParticipante = participante;
        this.classificacaoController = new ClassificacaoController();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Classificacao por Grupo"), BorderLayout.NORTH);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        UiTheme.applyPanel(topo);
        topo.setBorder(new EmptyBorder(0, 0, 4, 0));
        JLabel lbl = new JLabel("Grupo:");
        UiTheme.styleLabel(lbl, false);
        topo.add(lbl);

        comboGrupos = new JComboBox<>();
        comboGrupos.setPreferredSize(new Dimension(220, 34));
        UiTheme.styleCombo(comboGrupos);
        comboGrupos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Grupo) setText(((Grupo) value).getNome());
                c.setForeground(UiTheme.FG_PRIMARY);
                c.setBackground(isSelected ? UiTheme.ACCENT_BLUE : UiTheme.BG_CARD);
                return c;
            }
        });
        comboGrupos.addActionListener(e -> atualizarTabela());
        topo.add(comboGrupos);
        add(topo, BorderLayout.CENTER);

        String[] colunas = {"Pos.", "Participante", "Pontos"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaClassificacao = new JTable(modeloTabela);
        UiTheme.styleTable(tabelaClassificacao);

        tabelaClassificacao.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaClassificacao.getColumnModel().getColumn(0).setMaxWidth(70);
        tabelaClassificacao.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabelaClassificacao.getColumnModel().getColumn(2).setMaxWidth(100);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabelaClassificacao.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabelaClassificacao.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(tabelaClassificacao);
        UiTheme.styleScrollPane(scroll);
        scroll.setPreferredSize(new Dimension(0, 280));
        add(scroll, BorderLayout.SOUTH);

        atualizarDados();
    }

    @Override
    public void atualizarDados() {
        Grupo sel = (Grupo) comboGrupos.getSelectedItem();
        String nomeSel = sel != null ? sel.getNome() : null;

        comboGrupos.removeAllItems();
        List<Grupo> grupos = filtroParticipante == null
                ? classificacaoController.listarGrupos()
                : classificacaoController.listarGruposDoParticipante(filtroParticipante);
        for (Grupo g : grupos) comboGrupos.addItem(g);

        if (nomeSel != null) {
            for (int i = 0; i < comboGrupos.getItemCount(); i++) {
                Grupo g = comboGrupos.getItemAt(i);
                if (g != null && nomeSel.equals(g.getNome())) {
                    comboGrupos.setSelectedIndex(i);
                    return;
                }
            }
        }
        atualizarTabela();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        Grupo grupoSelecionado = (Grupo) comboGrupos.getSelectedItem();
        if (grupoSelecionado == null) return;

        List<Participante> classificacao = classificacaoController.obterClassificacao(grupoSelecionado);
        int posicao = 1;
        for (Participante p : classificacao) {
            String pos = posicao == 1 ? "1 o" : posicao == 2 ? "2 o" : posicao == 3 ? "3 o" : String.valueOf(posicao);
            modeloTabela.addRow(new Object[]{pos, p.getNome(), p.getPontos() + " pts"});
            posicao++;
        }
    }
}
