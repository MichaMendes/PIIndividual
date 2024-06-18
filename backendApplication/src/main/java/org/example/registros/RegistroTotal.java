package org.example.registros;

import org.example.darkstore.Darkstore;
import org.example.slack.NotificacaoSlack;
import org.example.stop.StopProcesso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroTotal {

    public static void cadastrarRegistroDisco(Connection connMySQL, Connection connSQLServer, int idMaquina) {
        String sql = "INSERT INTO registro " +
                "(dado, fkComponente, componente_fkMaquina, componente_maquina_fkDarkstore, componente_maquina_fkMetrica_ideal) " +
                "VALUES(?,?,?,?,?)";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            String dadoDisco = DiscoRegistro.extrairDisco();
            dadoDisco = dadoDisco.replace(',', '.');
            int fkComponente = 1;
            int fkDarkstore = Darkstore.pegarDarkStore(connMySQL, connSQLServer, idMaquina);
            int fkMetricaIdeal = Darkstore.pegarMetricaIdeal(connMySQL, connSQLServer, fkDarkstore);

            stmtMySQL.setString(1, dadoDisco);
            stmtMySQL.setInt(2, fkComponente);
            stmtMySQL.setInt(3, idMaquina);
            stmtMySQL.setInt(4, fkDarkstore);
            stmtMySQL.setInt(5, fkMetricaIdeal);

            stmtSQLServer.setString(1, dadoDisco);
            stmtSQLServer.setInt(2, fkComponente);
            stmtSQLServer.setInt(3, idMaquina);
            stmtSQLServer.setInt(4, fkDarkstore);
            stmtSQLServer.setInt(5, fkMetricaIdeal);

            int rsMySQL = stmtMySQL.executeUpdate();
            int rsSQLServer = stmtSQLServer.executeUpdate();

            if (rsMySQL > 0 && rsSQLServer > 0) {
                NotificacaoSlack.verificarDiscoSlack(parseDoubleSafe(dadoDisco));
                cadastrarRegistroRam(connMySQL, connSQLServer, idMaquina);
            } else {
                System.out.println("Erro ao cadastrar componente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cadastrarRegistroRam(Connection connMySQL, Connection connSQLServer, int idMaquina) {
        String sql = "INSERT INTO registro " +
                "(dado, fkComponente, componente_fkMaquina, componente_maquina_fkDarkstore, componente_maquina_fkMetrica_ideal) " +
                "VALUES(?,?,?,?,?)";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            String dadoRam = RamRegistro.extrairRam();
            dadoRam = dadoRam.replace(',', '.');
            dadoRam = removeUnidade(dadoRam);
            int fkComponente = 2;
            int fkDarkstore = Darkstore.pegarDarkStore(connMySQL, connSQLServer, idMaquina);
            int fkMetricaIdeal = Darkstore.pegarMetricaIdeal(connMySQL, connSQLServer, fkDarkstore);

            stmtMySQL.setString(1, dadoRam);
            stmtMySQL.setInt(2, fkComponente);
            stmtMySQL.setInt(3, idMaquina);
            stmtMySQL.setInt(4, fkDarkstore);
            stmtMySQL.setInt(5, fkMetricaIdeal);

            stmtSQLServer.setString(1, dadoRam);
            stmtSQLServer.setInt(2, fkComponente);
            stmtSQLServer.setInt(3, idMaquina);
            stmtSQLServer.setInt(4, fkDarkstore);
            stmtSQLServer.setInt(5, fkMetricaIdeal);

            int rsMySQL = stmtMySQL.executeUpdate();
            int rsSQLServer = stmtSQLServer.executeUpdate();

            if (rsMySQL > 0 && rsSQLServer > 0) {
                NotificacaoSlack.verificarRAMSlack(parseDoubleSafe(dadoRam));
                cadastrarRegistroCPU(connMySQL, connSQLServer, idMaquina);
            } else {
                System.out.println("Erro ao cadastrar componente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cadastrarRegistroCPU(Connection connMySQL, Connection connSQLServer, int idMaquina) {
        String sql = "INSERT INTO registro " +
                "(dado, fkComponente, componente_fkMaquina, componente_maquina_fkDarkstore, componente_maquina_fkMetrica_ideal) " +
                "VALUES(?,?,?,?,?)";

        try (PreparedStatement stmtMySQL = connMySQL.prepareStatement(sql);
             PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql)) {

            String dadoCPU = ProcessadorRegistro.extrairCPU();
            dadoCPU = dadoCPU.replace(',', '.');
            dadoCPU = removeUnidade(dadoCPU);
            int fkComponente = 3;
            int fkDarkstore = Darkstore.pegarDarkStore(connMySQL, connSQLServer, idMaquina);
            int fkMetricaIdeal = Darkstore.pegarMetricaIdeal(connMySQL, connSQLServer, fkDarkstore);

            stmtMySQL.setString(1, dadoCPU);
            stmtMySQL.setInt(2, fkComponente);
            stmtMySQL.setInt(3, idMaquina);
            stmtMySQL.setInt(4, fkDarkstore);
            stmtMySQL.setInt(5, fkMetricaIdeal);

            stmtSQLServer.setString(1, dadoCPU);
            stmtSQLServer.setInt(2, fkComponente);
            stmtSQLServer.setInt(3, idMaquina);
            stmtSQLServer.setInt(4, fkDarkstore);
            stmtSQLServer.setInt(5, fkMetricaIdeal);

            int rsMySQL = stmtMySQL.executeUpdate();
            int rsSQLServer = stmtSQLServer.executeUpdate();

            if (rsMySQL > 0 && rsSQLServer > 0) {
                NotificacaoSlack.verificarCPUSlack(parseDoubleSafe(dadoCPU));
                StopProcesso.validarDesativarProcesso(connMySQL, connSQLServer, idMaquina);
            } else {
                System.out.println("Erro ao cadastrar componente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter string para double: " + value);
            return 0.0;
        }
    }

    private static String removeUnidade(String value) {
        return value.replaceAll("[^\\d.]", "");
    }
}
