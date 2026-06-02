package br.com.futebolbet.repository;

import br.com.futebolbet.models.Campeonato;
import br.com.futebolbet.models.Clube;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampeonatoRepository {

    private static CampeonatoRepository instance;
    private final List<Campeonato> campeonatos;

    private CampeonatoRepository() {
        this.campeonatos = new ArrayList<>();
        carregarDoBanco();
    }

    public static CampeonatoRepository getInstance() {
        if (instance == null) {
            instance = new CampeonatoRepository();
        }
        return instance;
    }

    private void carregarDoBanco() {
        String sqlCamp = "SELECT id, nome FROM campeonatos ORDER BY id";
        String sqlClubes = "SELECT clube_id FROM campeonato_clube WHERE campeonato_id = ?";
        ClubeRepository clubeRepo = ClubeRepository.getInstance();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCamp)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                List<Clube> clubesDoCampeonato = new ArrayList<>();
                try (PreparedStatement ps = conn.prepareStatement(sqlClubes)) {
                    ps.setInt(1, id);
                    try (ResultSet rsClubes = ps.executeQuery()) {
                        while (rsClubes.next()) {
                            Clube c = clubeRepo.obterPorId(rsClubes.getInt("clube_id"));
                            if (c != null) clubesDoCampeonato.add(c);
                        }
                    }
                }

                Campeonato campeonato = new Campeonato(nome, clubesDoCampeonato);
                campeonato.setId(id);
                campeonatos.add(campeonato);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar campeonatos: " + e.getMessage(), e);
        }
    }

    public void adicionar(Campeonato campeonato) {
        if (campeonato.getClubes().size() > 8) return;

        String sqlCamp = "INSERT INTO campeonatos (nome) VALUES (?)";
        String sqlRel = "INSERT INTO campeonato_clube (campeonato_id, clube_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int geradoId;
                try (PreparedStatement ps = conn.prepareStatement(sqlCamp, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, campeonato.getNome());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        geradoId = keys.getInt(1);
                    }
                }
                campeonato.setId(geradoId);

                try (PreparedStatement ps = conn.prepareStatement(sqlRel)) {
                    for (Clube clube : campeonato.getClubes()) {
                        ps.setInt(1, geradoId);
                        ps.setInt(2, clube.getId());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                conn.commit();
                campeonatos.add(campeonato);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar campeonato: " + e.getMessage(), e);
        }
    }

    public List<Campeonato> obterTodos() {
        return new ArrayList<>(campeonatos);
    }

    public Campeonato obterPorNome(String nome) {
        for (Campeonato c : campeonatos) {
            if (c.getNome().equals(nome)) return c;
        }
        return null;
    }

    public Campeonato obterPorId(int id) {
        for (Campeonato c : campeonatos) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public void remover(Campeonato campeonato) {
        campeonatos.remove(campeonato);
    }

    public void limpar() {
        campeonatos.clear();
    }
}
