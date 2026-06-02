package br.com.futebolbet.ui;

import br.com.futebolbet.enums.DashboardAba;
import br.com.futebolbet.models.Administrador;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.models.Usuario;
import br.com.futebolbet.ui.theme.UiTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MenuPrincipalUI extends JFrame {

    private final Usuario usuarioLogado;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel painelCartoes = new JPanel(cardLayout);
    private final List<JButton> botoesAbas = new ArrayList<>();
    private final Map<DashboardAba, JComponent> conteudoPorAba = new EnumMap<>(DashboardAba.class);

    private DashboardAba abaAtual;
    private ApostasUI apostasParticipante;

    public MenuPrincipalUI(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        UiTheme.applyDarkOptionPaneDefaults();

        setTitle("Futebol Bet — " + usuarioLogado.getNome());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(950, 620));
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UiTheme.applyRoot(this);

        add(criarCabecalho(), BorderLayout.NORTH);

        JPanel centroComAbas = new JPanel(new BorderLayout());
        UiTheme.applyPanel(centroComAbas);

        JPanel barraAbas = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        UiTheme.applyPanel(barraAbas);
        barraAbas.setBorder(UiTheme.tabBarBorder());

        if (usuarioLogado instanceof Administrador) {
            montarAbasAdmin(barraAbas);
        } else if (usuarioLogado instanceof Participante) {
            montarAbasParticipante(barraAbas, (Participante) usuarioLogado);
        }

        centroComAbas.add(barraAbas, BorderLayout.NORTH);
        centroComAbas.add(painelCartoes, BorderLayout.CENTER);
        add(centroComAbas, BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(UiTheme.BG_CARD);
        cab.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UiTheme.ACCENT_BORDER),
                new EmptyBorder(12, 20, 12, 20)));

        JPanel esquerda = new JPanel(new GridLayout(2, 1, 0, 2));
        esquerda.setBackground(UiTheme.BG_CARD);
        JLabel lblApp = new JLabel("FUTEBOL BET");
        lblApp.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
        lblApp.setForeground(UiTheme.ACCENT_GREEN);
        JLabel lblUser = new JLabel(tipoUsuarioLabel() + "  —  " + usuarioLogado.getNome());
        lblUser.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        lblUser.setForeground(UiTheme.FG_MUTED);
        esquerda.add(lblApp);
        esquerda.add(lblUser);
        cab.add(esquerda, BorderLayout.WEST);

        JButton btnAtualizar = new JButton("Atualizar dados");
        UiTheme.styleSecondaryButton(btnAtualizar);
        btnAtualizar.setToolTipText("Recarrega os dados da aba atual");
        btnAtualizar.addActionListener(e -> atualizarAbaVisivel());
        cab.add(btnAtualizar, BorderLayout.EAST);

        return cab;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        rodape.setBackground(UiTheme.BG_CARD);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UiTheme.ACCENT_BORDER));
        JButton btnSair = new JButton("Sair");
        UiTheme.styleSecondaryButton(btnSair);
        btnSair.addActionListener(e -> {
            new LoginUi().setVisible(true);
            dispose();
        });
        rodape.add(btnSair);
        return rodape;
    }

    private void atualizarAbaVisivel() {
        if (abaAtual == null) return;
        JComponent c = conteudoPorAba.get(abaAtual);
        if (c instanceof AtualizavelInterface) {
            ((AtualizavelInterface) c).atualizarDados();
        }
    }

    private String tipoUsuarioLabel() {
        if (usuarioLogado instanceof Administrador) return "Administrador";
        if (usuarioLogado instanceof Participante) return "Participante";
        return "Usuario";
    }

    private void montarAbasAdmin(JPanel barraAbas) {
        registrarCartao(DashboardAba.ADM_CLUBES, "Clubes", new AdminClubeUI(), barraAbas);
        registrarCartao(DashboardAba.ADM_CAMPEONATOS, "Campeonatos", new AdminCampeonatoUI(), barraAbas);
        registrarCartao(DashboardAba.ADM_PARTIDAS, "Agendar partidas", new AdminPartidasUI(), barraAbas);
        registrarCartao(DashboardAba.ADM_RESULTADOS, "Registrar resultados", new AdminResultadosUI(), barraAbas);
        registrarCartao(DashboardAba.ADM_GRUPOS, "Gerenciar grupos", new AdminGruposUI(), barraAbas);
        registrarCartao(DashboardAba.ADM_CLASSIFICACAO, "Classificacao", new ClassificacaoUI(), barraAbas);
        selecionarAba(DashboardAba.ADM_CLUBES);
    }

    private void montarAbasParticipante(JPanel barraAbas, Participante participante) {
        registrarCartao(DashboardAba.PAR_PARTIDAS, "Partidas", new ParticipantePartidasUI(), barraAbas);
        registrarCartao(DashboardAba.PAR_CLASSIFICACAO, "Classificacao", new ClassificacaoUI(participante), barraAbas);

        apostasParticipante = new ApostasUI(participante);
        ParticipanteGruposUI grupos = new ParticipanteGruposUI(participante, () -> {
            apostasParticipante.atualizarDados();
            selecionarAba(DashboardAba.PAR_APOSTAS);
        });

        registrarCartao(DashboardAba.PAR_GRUPOS, "Grupos", grupos, barraAbas);
        registrarCartao(DashboardAba.PAR_APOSTAS, "Apostas", apostasParticipante, barraAbas);
        selecionarAba(DashboardAba.PAR_PARTIDAS);
    }

    private void registrarCartao(DashboardAba aba, String rotulo, JComponent conteudo, JPanel barraAbas) {
        conteudoPorAba.put(aba, conteudo);

        JPanel envelope = new JPanel(new BorderLayout());
        UiTheme.applyPanel(envelope);
        envelope.setBorder(new EmptyBorder(12, 16, 16, 16));
        envelope.add(conteudo, BorderLayout.CENTER);
        painelCartoes.add(envelope, aba.getCardId());

        JButton botaoAba = new JButton(rotulo);
        botaoAba.setActionCommand(aba.getCardId());
        UiTheme.stylePill(botaoAba, false);
        botaoAba.addActionListener(e -> {
            for (DashboardAba a : DashboardAba.values()) {
                if (a.getCardId().equals(e.getActionCommand())) {
                    selecionarAba(a);
                    return;
                }
            }
        });
        botoesAbas.add(botaoAba);
        barraAbas.add(botaoAba);
    }

    private void selecionarAba(DashboardAba aba) {
        this.abaAtual = aba;
        cardLayout.show(painelCartoes, aba.getCardId());
        for (JButton b : botoesAbas) {
            UiTheme.stylePill(b, aba.getCardId().equals(b.getActionCommand()));
        }
        if (aba == DashboardAba.PAR_APOSTAS && apostasParticipante != null) {
            apostasParticipante.atualizarDados();
        }
        atualizarAbaVisivel();
    }
}
