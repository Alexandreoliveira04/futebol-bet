package br.com.futebolbet.repository;

import br.com.futebolbet.models.Grupo;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoRepository {

    private static GrupoRepository instance;
    private final List<Grupo> grupos;

    private GrupoRepository() {
        this.grupos = new ArrayList<>();
        carregarDoBanco();
    }

    public static GrupoRepository getInstance() {
        if (instance == null) {
            instance = new GrupoRepository();
        }
        return instance;
    }

    private void carregarDoBanco() {
        String sqlGrupos = "SELECT id, nome FROM grupos ORDER BY id";
        String sqlMembros = "SELECT participante_id FROM grupo_participante WHERE grupo_id = ?";
        UsuarioRepository usuarioRepo = UsuarioRepository.getInstance();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlGrupos)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                Grupo grupo = new Grupo(rs.getString("nome"));
                grupo.setId(id);

                try (PreparedStatement ps = conn.prepareStatement(sqlMembros)) {
                    ps.setInt(1, id);
                    try (ResultSet rsMembros = ps.executeQuery()) {
                        while (rsMembros.next()) {
                            Usuario u = usuarioRepo.obterPorId(rsMembros.getInt("participante_id"));
                            if (u instanceof Participante) {
                                grupo.adicionarParticipante((Participante) u);
                            }
                        }
                    }
                }

                grupos.add(grupo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar grupos: " + e.getMessage(), e);
        }
    }

    public void adicionar(Grupo grupo) {
        String sql = "INSERT INTO grupos (nome) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, grupo.getNome());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    grupo.setId(keys.getInt(1));
                }
            }
            grupos.add(grupo);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar grupo: " + e.getMessage(), e);
        }
    }

    public void salvarMembro(Grupo grupo, Participante participante) {
        String sql = "INSERT OR IGNORE INTO grupo_participante (grupo_id, participante_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, grupo.getId());
            ps.setInt(2, participante.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar membro do grupo: " + e.getMessage(), e);
        }
    }

    public List<Grupo> obterTodos() {
        return new ArrayList<>(grupos);
    }

    public Grupo obterPorNome(String nome) {
        for (Grupo g : grupos) {
            if (g.getNome().equals(nome)) return g;
        }
        return null;
    }

    public Grupo obterPorId(int id) {
        for (Grupo g : grupos) {
            if (g.getId() == id) return g;
        }
        return null;
    }

    public void remover(Grupo grupo) {
        grupos.remove(grupo);
    }

    public void limpar() {
        grupos.clear();
    }
}
