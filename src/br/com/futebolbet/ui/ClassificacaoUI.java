package br.com.futebolbet.ui;

import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.service.GrupoService;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClassificacaoUI extends JPanel implements AtualizavelInterface {

    private final JTable tabelaClassificacao;
    private final DefaultTableModel modeloTabela;
    private final JComboBox<Grupo> comboGrupos;
    private final GrupoService grupoService;
    private final Participante filtroParticipante;

    public ClassificacaoUI() {
        this(null);
    }

    public ClassificacaoUI(Participante participante) {
        this.filtroParticipante = participante;
        this.grupoService = new GrupoService();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelSelecao = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        UiTheme.applyPanel(painelSelecao);
        JLabel hint = new JLabel("Grupo:");
        UiTheme.styleLabel(hint, false);
        painelSelecao.add(hint);

        comboGrupos = new JComboBox<>();
        UiTheme.styleCombo(comboGrupos);
        comboGrupos.setRenderer(new DefaultListCellRenderer() {
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
        comboGrupos.addActionListener(e -> atualizarTabela());
        painelSelecao.add(comboGrupos);

        add(painelSelecao, BorderLayout.NORTH);

        String[] colunas = {"Posição", "Nome", "Pontos"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaClassificacao = new JTable(modeloTabela);
        tabelaClassificacao.setRowHeight(30);
        tabelaClassificacao.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabelaClassificacao.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        UiTheme.styleTable(tabelaClassificacao);

        JScrollPane scrollPane = new JScrollPane(tabelaClassificacao);
        UiTheme.styleScrollPane(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        atualizarDados();
    }

    @Override
    public void atualizarDados() {
        Grupo sel = (Grupo) comboGrupos.getSelectedItem();
        String nomeSel = sel != null ? sel.getNome() : null;

        comboGrupos.removeAllItems();
        List<Grupo> grupos = filtroParticipante == null
                ? grupoService.obterTodosGrupos()
                : grupoService.obterGruposDoParticipante(filtroParticipante);
        for (Grupo g : grupos) {
            comboGrupos.addItem(g);
        }
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

        if (grupoSelecionado != null) {
            List<Participante> classificacao = grupoSelecionado.getClassificacao();
            int posicao = 1;
            for (Participante p : classificacao) {
                Object[] linha = {posicao++, p.getNome(), p.getPontos()};
                modeloTabela.addRow(linha);
            }
        }
    }
}
