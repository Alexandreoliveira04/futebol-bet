package br.com.futebolbet.repository;

import br.com.futebolbet.enums.TipoResultado;
import br.com.futebolbet.models.Aposta;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApostaRepository {

    private static ApostaRepository instance;
    private final List<Aposta> apostas;

    private ApostaRepository() {
        this.apostas = new ArrayList<>();
        carregarDoBanco();
    }

    public static ApostaRepository getInstance() {
        if (instance == null) {
            instance = new ApostaRepository();
        }
        return instance;
    }

    private void carregarDoBanco() {
        String sql = "SELECT id, participante_id, partida_id, grupo_id, " +
                     "       tipo_resultado, gols_casa_esperado, gols_fora_esperado, pontos " +
                     "FROM apostas ORDER BY id";

        UsuarioRepository usuarioRepo = UsuarioRepository.getInstance();
        PartidaRepository partidaRepo = PartidaRepository.getInstance();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = usuarioRepo.obterPorId(rs.getInt("participante_id"));
                Partida partida = partidaRepo.obterPorId(rs.getInt("partida_id"));

                if (!(u instanceof Participante) || partida == null) continue;

                Participante participante = (Participante) u;
                TipoResultado tipo = TipoResultado.valueOf(rs.getString("tipo_resultado"));

                Aposta aposta = new Aposta(participante, partida, tipo,
                        rs.getInt("gols_casa_esperado"),
                        rs.getInt("gols_fora_esperado"));
                aposta.setId(rs.getInt("id"));
                aposta.setGrupoId(rs.getInt("grupo_id"));
                aposta.setPontos(rs.getInt("pontos"));
                apostas.add(aposta);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar apostas: " + e.getMessage(), e);
        }
    }

    public void adicionar(Aposta aposta) {
        String sql = "INSERT INTO apostas (participante_id, partida_id, grupo_id, " +
                     "tipo_resultado, gols_casa_esperado, gols_fora_esperado, pontos) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, aposta.getParticipante().getId());
            ps.setInt(2, aposta.getPartida().getId());
            ps.setInt(3, aposta.getGrupoId());
            ps.setString(4, aposta.getResultadoEsperado().name());
            ps.setInt(5, aposta.getGolsCasaEsperado());
            ps.setInt(6, aposta.getGolsForaEsperado());
            ps.setInt(7, aposta.getPontos() != null ? aposta.getPontos() : 0);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    aposta.setId(keys.getInt(1));
                }
            }
            apostas.add(aposta);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar aposta: " + e.getMessage(), e);
        }
    }

    public void atualizarPontos(Aposta aposta) {
        String sql = "UPDATE apostas SET pontos = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, aposta.getPontos() != null ? aposta.getPontos() : 0);
            ps.setInt(2, aposta.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pontos da aposta: " + e.getMessage(), e);
        }
    }

    public List<Aposta> obterTodas() {
        return new ArrayList<>(apostas);
    }

    public List<Aposta> obterPorParticipante(Participante participante) {
        List<Aposta> resultado = new ArrayList<>();
        for (Aposta a : apostas) {
            if (a.getParticipante().equals(participante)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public List<Aposta> obterPorPartida(Partida partida) {
        List<Aposta> resultado = new ArrayList<>();
        for (Aposta a : apostas) {
            if (a.getPartida().equals(partida)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public void remover(Aposta aposta) {
        apostas.remove(aposta);
    }

    public void limpar() {
        apostas.clear();
    }
}
