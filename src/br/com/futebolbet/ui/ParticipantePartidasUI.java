package br.com.futebolbet.ui;

import br.com.futebolbet.controller.PartidaController;
import br.com.futebolbet.models.Campeonato;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Resultado;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ParticipantePartidasUI extends JPanel implements AtualizavelInterface {

    private static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final JComboBox<Campeonato> comboCampeonato;
    private final JTable tabela;
    private final javax.swing.table.DefaultTableModel modelo;
    private final PartidaController partidaController;

    public ParticipantePartidasUI() {
        this.partidaController = new PartidaController();

        UiTheme.applyPanel(this);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(8, 8, 8, 8));

        add(UiTheme.createSectionHeader("Partidas do Campeonato"), BorderLayout.NORTH);

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        UiTheme.applyPanel(topo);
        topo.setBorder(new EmptyBorder(0, 0, 4, 0));
        JLabel lbl = new JLabel("Campeonato:");
        UiTheme.styleLabel(lbl, false);
        topo.add(lbl);

        comboCampeonato = new JComboBox<>();
        comboCampeonato.setPreferredSize(new Dimension(220, 34));
        UiTheme.styleCombo(comboCampeonato);
        comboCampeonato.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Campeonato) setText(((Campeonato) value).getNome());
                c.setForeground(UiTheme.FG_PRIMARY);
                c.setBackground(isSelected ? UiTheme.ACCENT_BLUE : UiTheme.BG_CARD);
                return c;
            }
        });
        comboCampeonato.addActionListener(e -> atualizarTabela());
        topo.add(comboCampeonato);
        add(topo, BorderLayout.CENTER);

        String[] cols = {"Data / Hora", "Mandante", "Visitante", "Placar"};
        modelo = new javax.swing.table.DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        UiTheme.styleTable(tabela);

        JScrollPane scroll = new JScrollPane(tabela);
        UiTheme.styleScrollPane(scroll);
        add(scroll, BorderLayout.SOUTH);
        scroll.setPreferredSize(new Dimension(0, 320));

        atualizarDados();
    }

    @Override
    public void atualizarDados() {
        Campeonato sel = (Campeonato) comboCampeonato.getSelectedItem();
        String nomeSel = sel != null ? sel.getNome() : null;
        comboCampeonato.removeAllItems();
        for (Campeonato c : partidaController.listarCampeonatos()) comboCampeonato.addItem(c);
        if (nomeSel != null) {
            for (int i = 0; i < comboCampeonato.getItemCount(); i++) {
                Campeonato c = comboCampeonato.getItemAt(i);
                if (c != null && nomeSel.equals(c.getNome())) {
                    comboCampeonato.setSelectedIndex(i);
                    atualizarTabela();
                    return;
                }
            }
        }
        atualizarTabela();
    }

    private void atualizarTabela() {
        modelo.setRowCount(0);
        Campeonato c = (Campeonato) comboCampeonato.getSelectedItem();
        if (c == null) return;

        List<Partida> lista = partidaController.listarPartidas().stream()
                .filter(p -> p.getCampeonato() != null
                        && p.getCampeonato().getNome().equals(c.getNome()))
                .sorted((a, b) -> a.getDataHora().compareTo(b.getDataHora()))
                .collect(Collectors.toList());

        for (Partida p : lista) {
            modelo.addRow(new Object[]{
                p.getDataHora().format(DATA_HORA),
                p.getClubeCasa().getNome(),
                p.getClubeFora().getNome(),
                formatPlacar(p.getResultado())
            });
        }
    }

    private static String formatPlacar(Resultado r) {
        if (r == null) return "—";
        return r.getGolsCasa() + " x " + r.getGolsFora();
    }
}
