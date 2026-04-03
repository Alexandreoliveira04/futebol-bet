package br.com.futebolbet.ui;

import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClassificacaoUI extends JFrame {
    private JTable tabelaClassificacao;
    private DefaultTableModel modeloTabela;

    public ClassificacaoUI(Grupo grupo) {
        setTitle("Classificação do Grupo: " + grupo.getNome());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titulo = new JLabel("Classificação - " + grupo.getNome(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        principal.add(titulo, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Posição", "Nome", "Pontos"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaClassificacao = new JTable(modeloTabela);
        tabelaClassificacao.setRowHeight(25);
        tabelaClassificacao.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabelaClassificacao.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabelaClassificacao.getColumnModel().getColumn(2).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tabelaClassificacao);
        principal.add(scrollPane, BorderLayout.CENTER);

        // Preencher tabela
        java.util.List<Participante> classificacao = grupo.getClassificacao();
        int posicao = 1;
        for (Participante p : classificacao) {
            Object[] linha = {posicao++, p.getNome(), p.getPontos()};
            modeloTabela.addRow(linha);
        }

        add(principal);
        setVisible(true);
    }
}
