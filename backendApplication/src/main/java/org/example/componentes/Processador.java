package org.example.componentes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Processador {
    public static void cadastrarProcessador(Connection connMySQL, Connection connSQLServer, Integer idMaquina) {
        String sql = "INSERT INTO componente (nome, Maquina_idMaquina) VALUES (?, ?)";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            stmtMySQL.setString(1, "CPU");
            stmtMySQL.setInt(2, idMaquina);
            stmtSQLServer.setString(1, "CPU");
            stmtSQLServer.setInt(2, idMaquina);

            int rsMySQL = stmtMySQL.executeUpdate();
            int rsSQLServer = stmtSQLServer.executeUpdate();

            if (rsMySQL > 0 && rsSQLServer > 0) {
                Processo.cadastrarProcesso(connMySQL, connSQLServer, idMaquina);
            } else {
                System.out.println("Erro ao cadastrar componente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
