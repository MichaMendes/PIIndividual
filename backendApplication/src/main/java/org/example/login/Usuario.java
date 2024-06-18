package org.example.login;

import org.example.componentes.Maquina;
import org.example.connection.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {


        public boolean validarUser(String email, String senha) {
            boolean usuarioValidado = false;

            // Validar na conexão do SQL Server
            usuarioValidado = validarUsuarioNaNuvem(email, senha, Conexao.getConexaoSQLServer());

            // Se não conseguiu validar na nuvem, tenta validar na conexão do MySQL
            if (!usuarioValidado) {
                usuarioValidado = validarUsuarioNaNuvem(email, senha, Conexao.getConexaoMySQL());
            }

            return usuarioValidado;
        }

        private boolean validarUsuarioNaNuvem(String email, String senha, Connection conn) {
            if (conn == null) {
                System.err.println("Erro: Conexão com a nuvem é nula.");
                return false;
            }

            String sql = "SELECT * FROM empresa WHERE email = ? AND senha = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, senha);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("""
                            𝙇𝙤𝙜𝙞𝙣 𝙧𝙚𝙖𝙡𝙞𝙯𝙖𝙙𝙤 𝙘𝙤𝙢 𝙨𝙪𝙘𝙚𝙨𝙨𝙤!
                            """);
                    Connection connMySQL = Conexao.getConexaoMySQL();
                    Connection connSQLServer = Conexao.getConexaoSQLServer();
                    Maquina.validarMaquina(connMySQL, connSQLServer);
                    return true;
                } else {
                    System.out.println("""
                            𝙐𝙨𝙪𝙖𝙧𝙞𝙤 𝙣𝙖𝙤 𝙚𝙣𝙘𝙤𝙣𝙩𝙧𝙖𝙣𝙙𝙤 𝙤𝙪 𝙨𝙚𝙣𝙝𝙖 𝙞𝙣𝙫𝙖𝙡𝙞𝙙𝙖!
                            """);
                    return false;
                }
            } catch (SQLException e) {
                System.err.println("Erro ao validar o usuário na nuvem: " + e.getMessage());
                return false;
            }
        }
    }
