package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSQLSERVER extends Conexao {

    private static final String URL_SQLSERVER = "jdbc:sqlserver://52.200.17.70;databaseName=sisguard;encrypt=false;trustServerCertificate=true";
    private static final String USERNAME_SQLSERVER = "sa";
    private static final String PASSWORD_SQLSERVER = "Aluno123!";

    @Override
    protected Connection getConexaoEspecifica() throws SQLException {
        return DriverManager.getConnection(URL_SQLSERVER, USERNAME_SQLSERVER, PASSWORD_SQLSERVER);
    }
}
