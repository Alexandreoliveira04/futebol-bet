package br.com.futebolbet.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void init() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("PRAGMA foreign_keys = ON");

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS usuarios (" +
                "  id     INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  nome   TEXT    NOT NULL," +
                "  email  TEXT    NOT NULL UNIQUE," +
                "  senha  TEXT    NOT NULL," +
                "  tipo   TEXT    NOT NULL," +
                "  pontos INTEGER NOT NULL DEFAULT 0" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS clubes (" +
                "  id   INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  nome TEXT    NOT NULL" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS campeonatos (" +
                "  id   INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  nome TEXT    NOT NULL UNIQUE" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS campeonato_clube (" +
                "  campeonato_id INTEGER NOT NULL," +
                "  clube_id      INTEGER NOT NULL," +
                "  PRIMARY KEY (campeonato_id, clube_id)" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS partidas (" +
                "  id            INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  campeonato_id INTEGER NOT NULL," +
                "  clube_casa_id INTEGER NOT NULL," +
                "  clube_fora_id INTEGER NOT NULL," +
                "  data_hora     TEXT    NOT NULL," +
                "  gols_casa     INTEGER," +
                "  gols_fora     INTEGER" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS grupos (" +
                "  id   INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  nome TEXT    NOT NULL UNIQUE" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS grupo_participante (" +
                "  grupo_id        INTEGER NOT NULL," +
                "  participante_id INTEGER NOT NULL," +
                "  PRIMARY KEY (grupo_id, participante_id)" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS apostas (" +
                "  id                 INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  participante_id    INTEGER NOT NULL," +
                "  partida_id         INTEGER NOT NULL," +
                "  grupo_id           INTEGER NOT NULL," +
                "  tipo_resultado     TEXT    NOT NULL," +
                "  gols_casa_esperado INTEGER NOT NULL," +
                "  gols_fora_esperado INTEGER NOT NULL," +
                "  pontos             INTEGER NOT NULL DEFAULT 0" +
                ")"
            );

        } catch (SQLException e) {
            throw new RuntimeException("Falha ao inicializar banco de dados: " + e.getMessage(), e);
        }
    }
}
