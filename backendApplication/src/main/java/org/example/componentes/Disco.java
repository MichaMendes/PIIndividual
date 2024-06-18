package org.example.componentes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Disco {
    public static void cadastrarDisco(Connection connMySQL, Connection connSQLServer, Integer idMaquina) {
        String sql = "INSERT INTO componente (nome, Maquina_idMaquina) VALUES (?, ?)";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            stmtMySQL.setString(1, "Disco");
            stmtMySQL.setInt(2, idMaquina);
            stmtSQLServer.setString(1, "Disco");
            stmtSQLServer.setInt(2, idMaquina);

            int rsMySQL = stmtMySQL.executeUpdate();
            int rsSQLServer = stmtSQLServer.executeUpdate();

            if (rsMySQL > 0 && rsSQLServer > 0) {
                Memoria.cadastrarMemoriaRam(connMySQL, connSQLServer, idMaquina);
            } else {
                System.out.println("Erro ao cadastrar componente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
