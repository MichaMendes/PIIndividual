package org.example.stop;

import org.example.componentes.Processo;
import org.example.connection.ConnectionMYSQL;
import org.example.connection.ConnectionSQLSERVER;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StopProcesso {

    public static void validarDesativarProcesso(Connection connMySQL, Connection connSQLServer, int idMaquina) throws SQLException {
        String sql = "SELECT * FROM processos WHERE desativar = ? and fkMaquina = ?";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            stmtMySQL.setString(1, "SIM");
            stmtMySQL.setInt(2, idMaquina);
            ResultSet respostaMySQL = stmtMySQL.executeQuery();

            stmtSQLServer.setString(1, "SIM");
            stmtSQLServer.setInt(2, idMaquina);
            ResultSet respostaSQLServer = stmtSQLServer.executeQuery();

            if (respostaMySQL.next() || respostaSQLServer.next()) {
                try {
                    PidProcesso.extrairPid(connMySQL,connSQLServer, idMaquina);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Erro ao extrair PID: " + e.getMessage());
                }
            } else {
                System.out.println("Nenhum processo para desativar");
                Processo.cadastrarProcesso(connMySQL,connSQLServer ,idMaquina);
            }
        }
    }


    public static void desativarProcesso(Connection connMySQL, Connection connSQLServer, int pid, int idMaquina) {
        try {
            // Comando para matar o processo (exemplo para Windows)
            String command = "taskkill /F /PID " + pid;

            // Para Linux/Unix use:
            // String command = "kill -9 " + pid;

            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();

            reativarProcesso(connMySQL, connSQLServer, idMaquina, pid);
        } catch (IOException | InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }


    public static void reativarProcesso(Connection connMySQL, Connection connSQLServer, int idMaquina, int pid) throws SQLException {
        String sql = "DELETE FROM processos WHERE fkMaquina = ? AND pid = ?";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            stmtMySQL.setInt(1, idMaquina);
            stmtMySQL.setInt(2, pid);
            int respostaMySQL = stmtMySQL.executeUpdate();

            stmtSQLServer.setInt(1, idMaquina);
            stmtSQLServer.setInt(2, pid);
            int respostaSQLServer = stmtSQLServer.executeUpdate();

            if (respostaMySQL > 0 && respostaSQLServer > 0) {
                System.out.println("Processo reativado em ambas as conexões!");
                Processo.cadastrarProcesso(connMySQL,connSQLServer,idMaquina);
            } else {
                System.out.println("Nenhum processo para desativar");
            }
        }
    }


}
