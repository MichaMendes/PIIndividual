package org.example.stop;

import org.example.connection.ConnectionMYSQL;
import org.example.connection.ConnectionSQLSERVER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PidProcesso {
    public static void extrairPid(Connection connMySQL, Connection connSQLServer, int idMaquina) throws SQLException {
        String sql = "SELECT * FROM processos WHERE desativar = ? AND fkMaquina = ?";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            stmtMySQL.setString(1, "SIM");
            stmtMySQL.setInt(2, idMaquina);

            stmtSQLServer.setString(1, "SIM");
            stmtSQLServer.setInt(2, idMaquina);

            ResultSet respostaMySQL = stmtMySQL.executeQuery();
            ResultSet respostaSQLServer = stmtSQLServer.executeQuery();

            if (respostaMySQL.next()) {
                String pid = respostaMySQL.getString("pid");
                StopProcesso.desativarProcesso(connMySQL, connSQLServer, Integer.parseInt(pid), idMaquina);
            } else if (respostaSQLServer.next()) {
                String pid = respostaSQLServer.getString("pid");
                StopProcesso.desativarProcesso(connMySQL, connSQLServer, Integer.parseInt(pid), idMaquina);
            } else {
                System.out.println("Nenhum processo para desativar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


