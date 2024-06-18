package org.example.connection;

import java.sql.*;

public class ConnectionMYSQL extends Conexao {

    private static final String URL_MYSQL = "jdbc:mysql://44.194.8.163/sisguard";
    private static final String USERNAME_MYSQL = "root";
    private static final String PASSWORD_MYSQL = "urubu100";

    @Override
    protected Connection getConexaoEspecifica() throws SQLException {
        return DriverManager.getConnection(URL_MYSQL, USERNAME_MYSQL, PASSWORD_MYSQL);
    }
}