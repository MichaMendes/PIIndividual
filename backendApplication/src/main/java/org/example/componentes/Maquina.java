package org.example.componentes;

import org.example.connection.Conexao;
import org.example.registros.RegistroTotal;
import org.example.relatorio.MainRelatorio;
import org.example.stop.StopProcesso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Maquina {

    public static String extrairIdentificadorMaquina() {
        String linhasTeste = Componentes.processador();
        String[] linhas = linhasTeste.split("\\r?\\n");

        for (String linha : linhas) {
            if (linha.contains("ID:")) {
                String[] partes = linha.split(":");
                if (partes.length > 1) {
                    return partes[1].trim();
                } else {
                    System.out.println("Formato incorreto para 'ID:'");
                }
            }
        }
        System.out.println("'ID:' não encontrado");
        return "";
    }

    private static int verificarOuCadastrarMaquina(Connection conn, String sqlSelect, String sqlInsert, String identificadorMaquina) throws SQLException {
        try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
            stmtSelect.setString(1, identificadorMaquina);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                return rs.getInt("idMaquina");
            } else {
                try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmtInsert.setString(1, identificadorMaquina);
                    int affectedRows = stmtInsert.executeUpdate();

                    if (affectedRows > 0) {
                        ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
                        System.out.println("Máquina cadastrada com sucesso!");
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }
        }
        return -1;
    }

    public static void validarMaquina(Connection connMySQL, Connection connSQLServer) {

        MainRelatorio.main(new String[]{});

        String identificadorMaquina = extrairIdentificadorMaquina();
        if (identificadorMaquina.isEmpty()) {
            System.err.println("Não foi possível extrair o identificador da máquina.");
            return;
        }

        String sqlSelect = "SELECT idMaquina FROM maquina WHERE numSerie = ?";
        String sqlInsert = "INSERT INTO maquina (numSerie, fkDarkStore) VALUES (?, 1)";

        try {
            int idMaquinaMySQL = verificarOuCadastrarMaquina(connMySQL, sqlSelect, sqlInsert, identificadorMaquina);
            int idMaquinaSQLServer = verificarOuCadastrarMaquina(connSQLServer, sqlSelect, sqlInsert, identificadorMaquina);

            if (idMaquinaMySQL != -1 && idMaquinaSQLServer != -1) {
                System.out.println("Iniciando registro...");
                if (Memoria.verificarMemoriaRAM(connMySQL, connSQLServer, idMaquinaMySQL)) {
                    Processo.cadastrarProcesso(connMySQL, connSQLServer, idMaquinaMySQL);
                } else {
                    Disco.cadastrarDisco(connMySQL, connSQLServer, idMaquinaMySQL);
                }
            } else {
                System.err.println("Erro ao validar máquina nos bancos de dados.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        try (Connection connMySQL = Conexao.getConexaoMySQL();
             Connection connSQLServer = Conexao.getConexaoSQLServer()) {
            validarMaquina(connMySQL, connSQLServer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
