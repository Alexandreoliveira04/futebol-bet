package br.com.futebolbet.repository;

import br.com.futebolbet.models.Administrador;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private static UsuarioRepository instance;
    private final List<Usuario> usuarios;

    private UsuarioRepository() {
        this.usuarios = new ArrayList<>();
        carregarDoBanco();
    }

    public static UsuarioRepository getInstance() {
        if (instance == null) {
            instance = new UsuarioRepository();
        }
        return instance;
    }

    private void carregarDoBanco() {
        String sql = "SELECT id, nome, email, senha, tipo, pontos FROM usuarios ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                String tipo = rs.getString("tipo");
                int pontos = rs.getInt("pontos");

                Usuario usuario;
                if ("ADMINISTRADOR".equals(tipo)) {
                    usuario = new Administrador(nome, email, senha);
                } else {
                    Participante p = new Participante(nome, email, senha);
                    p.definirPontos(pontos);
                    usuario = p;
                }
                usuario.setId(id);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao carregar usuários: " + e.getMessage(), e);
        }
    }

    public void adicionar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, tipo, pontos) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario instanceof Administrador ? "ADMINISTRADOR" : "PARTICIPANTE");
            ps.setInt(5, usuario instanceof Participante ? ((Participante) usuario).getPontos() : 0);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setId(keys.getInt(1));
                }
            }
            usuarios.add(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    public void atualizarPontos(Participante participante) {
        String sql = "UPDATE usuarios SET pontos = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, participante.getPontos());
            ps.setInt(2, participante.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pontos: " + e.getMessage(), e);
        }
    }

    public List<Usuario> obterTodos() {
        return new ArrayList<>(usuarios);
    }

    public Usuario obterPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) return u;
        }
        return null;
    }

    public Usuario obterPorId(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    public void remover(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public void limpar() {
        usuarios.clear();
    }
}
