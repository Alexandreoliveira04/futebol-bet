package br.com.futebolbet.repository;

import br.com.futebolbet.models.Campeonato;
import br.com.futebolbet.models.Clube;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Resultado;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartidaRepository {

    private static PartidaRepository instance;
    private final List<Partida> partidas;

    private PartidaRepository() {
        this.partidas = new ArrayList<>();
        carregarDoBanco();
    }

    public static PartidaRepository getInstance() {
        if (instance == null) {
            instance = new PartidaRepository();
        }
        return instance;
    }

    private void carregarDoBanco() {
        String sql = "SELECT id, campeonato_id, clube_casa_id, clube_fora_id, " +
                     "       data_hora, gols_casa, gols_fora " +
                     "FROM partidas ORDER BY id";

        CampeonatoRepository campRepo = CampeonatoRepository.getInstance();
        ClubeRepository clubeRepo = ClubeRepository.getInstance();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Campeonato campeonato = campRepo.obterPorId(rs.getInt("campeonato_id"));
                Clube clubeCasa = clubeRepo.obterPorId(rs.getInt("clube_casa_id"));
                Clube clubeFora = clubeRepo.obterPorId(rs.getInt("clube_fora_id"));
                LocalDateTime dataHora = LocalDateTime.parse(rs.getString("data_hora"));

                Partida partida = new Partida(campeonato, clubeCasa, clubeFora, dataHora);
                partida.setId(rs.getInt("id"));

                Object golsCasaObj = rs.getObject("gols_casa");
                Object golsForaObj = rs.getObject("gols_fora");
                if (golsCasaObj != null && golsForaObj != null) {
                    partida.setResultado(new Resultado(
                            ((Number) golsCasaObj).intValue(),
                            ((Number) golsForaObj).intValue()
                    ));
                }

                partidas.add(partida);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar partidas: " + e.getMessage(), e);
        }
    }

    public void adicionar(Partida partida) {
        String sql = "INSERT INTO partidas (campeonato_id, clube_casa_id, clube_fora_id, data_hora) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, partida.getCampeonato().getId());
            ps.setInt(2, partida.getClubeCasa().getId());
            ps.setInt(3, partida.getClubeFora().getId());
            ps.setString(4, partida.getDataHora().toString());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    partida.setId(keys.getInt(1));
                }
            }
            partidas.add(partida);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar partida: " + e.getMessage(), e);
        }
    }

    public void registrarResultado(Partida partida) {
        String sql = "UPDATE partidas SET gols_casa = ?, gols_fora = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Resultado r = partida.getResultado();
            ps.setInt(1, r.getGolsCasa());
            ps.setInt(2, r.getGolsFora());
            ps.setInt(3, partida.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar resultado: " + e.getMessage(), e);
        }
    }

    public List<Partida> obterTodas() {
        return new ArrayList<>(partidas);
    }

    public Partida obterPorId(int id) {
        for (Partida p : partidas) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public void remover(Partida partida) {
        partidas.remove(partida);
    }

    public void limpar() {
        partidas.clear();
    }
}
