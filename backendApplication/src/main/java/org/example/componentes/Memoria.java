package org.example.componentes;

import org.example.connection.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class Memoria {
    public static Double conversor() {
        long ramBytes = Componentes.memoriaRamUso();
        double ramNumb = ramBytes / Math.pow(1024, 3);
        DecimalFormat df = new DecimalFormat("#0.00");
        String ramString = df.format(ramNumb).replace(",", ".");
        return Double.parseDouble(ramString);
    }

    public static void cadastrarMemoriaRam(Connection connMySQL, Connection connSQLServer, Integer idMaquina) {
        String sql = "INSERT INTO componente (nome, Maquina_idMaquina) VALUES (?, ?)";

        try {
            int respostaMySQL = Conexao.executeUpdate(connMySQL, sql, "Memoria Ram", idMaquina);
            int respostaSQLServer = Conexao.executeUpdate(connSQLServer, sql, "Memoria Ram", idMaquina);

            if (respostaMySQL > 0 && respostaSQLServer > 0) {
                Processador.cadastrarProcessador(connMySQL, connSQLServer, idMaquina);
            } else {
                System.out.println("Erro ao cadastrar componente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean verificarMemoriaRAM(Connection connMySQL, Connection connSQLServer, int idMaquina) {
        String sql = "SELECT * FROM componente WHERE Maquina_idMaquina = ?";
        boolean verificar = false;

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            stmtMySQL.setInt(1, idMaquina);
            stmtSQLServer.setInt(1, idMaquina);

            ResultSet rsMySQL = stmtMySQL.executeQuery();
            ResultSet rsSQLServer = stmtSQLServer.executeQuery();

            verificar = rsMySQL.next() && rsSQLServer.next();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar a memória RAM", e);
        }

        return verificar;
    }
}
