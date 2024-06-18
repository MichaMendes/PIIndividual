package org.example.darkstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Darkstore {

    public static Integer pegarDarkStore(Connection connMySQL, Connection connSQLServer, int idMaquina) {
        int idDarkStore = 0;
        String sql = "select fkDarkStore from maquina where idMaquina = ?";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            stmtMySQL.setInt(1, idMaquina);
            stmtSQLServer.setInt(1, idMaquina);

            ResultSet rMySQL = stmtMySQL.executeQuery();
            ResultSet rSQLServer = stmtSQLServer.executeQuery();

            if (rMySQL.next()) {
                idDarkStore = rMySQL.getInt(1);
            } else if (rSQLServer.next()) {
                idDarkStore = rSQLServer.getInt(1);
            } else {
                throw new SQLException("No fkDarkStore found for idMaquina: " + idMaquina);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idDarkStore;
    }

    public static Integer pegarMetricaIdeal(Connection connMySQL, Connection connSQLServer, int darkstore) throws SQLException {
        int idMetricaIdeal = 0;
        String sql = "select idMetricaIdeal from metrica_ideal where fkDarkStore = ?";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            stmtMySQL.setInt(1, darkstore);
            stmtSQLServer.setInt(1, darkstore);

            ResultSet rMySQL = stmtMySQL.executeQuery();
            ResultSet rSQLServer = stmtSQLServer.executeQuery();

            if (rMySQL.next()) {
                idMetricaIdeal = rMySQL.getInt(1);
            } else if (rSQLServer.next()) {
                idMetricaIdeal = rSQLServer.getInt(1);
            } else {
                throw new SQLException("No idMetricaIdeal found for darkstore: " + darkstore);
            }
        }
        return idMetricaIdeal;
    }
}
