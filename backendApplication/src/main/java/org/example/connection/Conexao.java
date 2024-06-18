package org.example.connection;

import com.github.britooo.looca.api.core.Looca;
import org.example.log.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Conexao {
    protected static Connection conexaoMySQL = null;
    protected static Connection conexaoSQLServer = null;

    private static Looca looca = new Looca();
    private static String sistemaOperacional;
    private static Integer arquitetura;
    private static String hostname;
    private static String data;
    private static String logLevel;
    private static Integer statusCode;
    private static String idMaquina;
    private static String mensagem;
    private static String stackTrace;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            conexaoMySQL = new ConnectionMYSQL().getConexaoEspecifica();
            conexaoSQLServer = new ConnectionSQLSERVER().getConexaoEspecifica();
        } catch (ClassNotFoundException | SQLException e) {
            handleSQLException(e);
        }
    }

    protected abstract Connection getConexaoEspecifica() throws SQLException;

    public Connection getConexao() throws SQLException {
        return getConexaoEspecifica();
    }
    public static Connection getConexaoMySQL() {
        return conexaoMySQL;
    }

    public static Connection getConexaoSQLServer() {
        return conexaoSQLServer;
    }

    private static void handleSQLException(Exception ex) {
        System.out.println("Erro ao conectar ao banco de dados: " + ex.getMessage());
        sistemaOperacional = looca.getSistema().getSistemaOperacional();
        arquitetura = looca.getSistema().getArquitetura();
        hostname = looca.getRede().getParametros().getHostName();
        data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(new Date());
        logLevel = "ERROR";
        statusCode = 503;
        idMaquina = "";
        mensagem = "Erro ao conectar ao banco de dados: " + ex.getMessage();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        stackTrace = sw.toString().replace("\n", "").replace("\r", "").replace("\t", "");
        Log errorbanco = new Log(sistemaOperacional, arquitetura, hostname, data, logLevel, statusCode, idMaquina, mensagem, stackTrace);
        System.out.println(errorbanco.toString().replace("idMaquina: null\n", "").replace("hostname: null\n", "").replace("\t", ""));

            String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String nomeArquivoLog = String.format(".\\log-%s.txt", dataAtual);

            try (FileWriter writer = new FileWriter(nomeArquivoLog, true)) {
                writer.write(errorbanco.toString().replace("idMaquina: null\n", "").replace("hostname: null\n", "").replace("\t", ""));
            } catch (IOException u) {
                System.out.println("Erro ao salvar log: " + u.getMessage());
            }
    }

    public static int executeUpdate(Connection connection, String sql, Object... parameters) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            stmt.setObject(i + 1, parameters[i]);
        }
        return stmt.executeUpdate();
    }

    public static ResultSet executeQuery(Connection connection, String sql, Object... parameters) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        for (int i = 0; i < parameters.length; i++) {
            stmt.setObject(i + 1, parameters[i]);
        }
        return stmt.executeQuery();
    }
}
