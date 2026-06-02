package br.com.futebolbet.repository;

import br.com.futebolbet.models.Clube;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubeRepository {

    private static ClubeRepository instance;
    private final List<Clube> clubes;

    private ClubeRepository() {
        this.clubes = new ArrayList<>();
        carregarDoBanco();
    }

    public static ClubeRepository getInstance() {
        if (instance == null) {
            instance = new ClubeRepository();
        }
        return instance;
    }

    private void carregarDoBanco() {
        String sql = "SELECT id, nome FROM clubes ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Clube clube = new Clube(rs.getString("nome"));
                clube.setId(rs.getInt("id"));
                clubes.add(clube);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar clubes: " + e.getMessage(), e);
        }
    }

    public void adicionar(Clube clube) {
        String sql = "INSERT INTO clubes (nome) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, clube.getNome());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    clube.setId(keys.getInt(1));
                }
            }
            clubes.add(clube);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar clube: " + e.getMessage(), e);
        }
    }

    public List<Clube> obterTodos() {
        return new ArrayList<>(clubes);
    }

    public Clube obterPorId(int id) {
        for (Clube c : clubes) {
            if (c.getId() == id) return c;
        }
        return null;
    }
}
