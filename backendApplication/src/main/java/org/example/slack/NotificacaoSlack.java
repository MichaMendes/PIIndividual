package org.example.slack;

import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.example.componentes.Componentes;
import org.example.connection.Conexao;
import org.example.darkstore.Darkstore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificacaoSlack {

    private static String mensagem;

    private static Double alertaPadrao;
    private static Double criticoPadrao;
    private static Double alertaRAM;
    private static Double alertaCPU;
    private static Double alertaDisco;
    private static Double criticoRAM;
    private static Double criticoCPU;
    private static Double criticoDisco;
    private static boolean esperarRAM = false;
    private static boolean esperarCPU = false;
    private static boolean esperarDisco = false;

    private static String name = Componentes.hostName();

    static int idDark = 1;

    static {
        try {
            Connection connMysql = Conexao.getConexaoMySQL();
            Connection connSQLServer = Conexao.getConexaoSQLServer();

            String sql = "SELECT alertaPadrao, criticaPadrao, alertaRAM, alertaCPU, alertaDisco, criticoRAM, criticoCPU, criticoDisco FROM metrica_ideal WHERE fkDarkStore = ?";
            PreparedStatement stmt = connMysql.prepareStatement(sql);
            PreparedStatement stmt2 = connSQLServer.prepareStatement(sql);
            stmt.setInt(1, idDark);
            stmt2.setInt(1, idDark);
            ResultSet rs = stmt.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();

            if (rs.next() || rs2.next()) {
                alertaPadrao = rs.getDouble("alertaPadrao");
                criticoPadrao = rs.getDouble("criticaPadrao");
                alertaRAM = rs.getDouble("alertaRAM");
                alertaCPU = rs.getDouble("alertaCPU");
                alertaDisco = rs.getDouble("alertaDisco");
                criticoRAM = rs.getDouble("criticoRAM");
                criticoCPU = rs.getDouble("criticoCPU");
                criticoDisco = rs.getDouble("criticoDisco");
            } else {
                throw new RuntimeException("Não foi possível encontrar métricas para a darkstore com o ID: " + idDark);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static Slack createSlackInstance() {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, (chain, authType) -> true);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLContext(builder.build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            return Slack.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean esperar5Minutos(Boolean esperar) throws SQLException {
        if (!esperar) {
            enviarMensagem(mensagem);
            System.out.println(mensagem + " caso o problema persistir voltaremos em 5 minutos");

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
            scheduler.schedule(() -> {
                if (mensagem.contains("RAM")) {
                    esperarRAM = false;
                } else if (mensagem.contains("CPU")) {
                    esperarCPU = false;
                } else if (mensagem.contains("Disco")) {
                    esperarDisco = false;
                }
            }, 5, TimeUnit.MINUTES);
        }
        return esperar;
    }

    public static void enviarMensagem(String mensagem) {
        String webhookUrl = "https://hooks.slack.com/services/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";
        String username = name;
        String channel = obterCanalDoBancoDeDados();
        Slack slack = createSlackInstance();

        Payload payload = Payload.builder()
                .channel(channel)
                .username(username)
                .text(mensagem)
                .build();
        try {
            WebhookResponse response = slack.send(webhookUrl, payload);
            if (response.getCode() != 200) {
                System.err.println("Erro ao enviar mensagem para o Slack. Código de resposta: " + response.getCode() + ", Mensagem: " + response.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Erro ao enviar mensagem para o Slack: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem para o Slack: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void verificarDiscoSlack(double discoUso) throws SQLException {
        if (alertaDisco != null && criticoDisco != null) {
            if (discoUso >= alertaDisco && discoUso < criticoDisco) {
                mensagem = "Disco em alerta, fique de olho !";
                esperar5Minutos(esperarDisco);
                esperarDisco = true;

            } else if (discoUso >= criticoDisco) {
                mensagem = "Disco em estado crítico, fique de olho !";
                esperar5Minutos(esperarDisco);
                esperarDisco = true;
            }
        } else {
            if (alertaPadrao != null && criticoPadrao != null) {
                if (discoUso >= alertaPadrao && discoUso < criticoPadrao) {
                    mensagem = "Disco em alerta";
                    esperar5Minutos(esperarDisco);
                    esperarDisco = true;

                } else if (discoUso >= criticoPadrao) {
                    mensagem = "Disco em estado crítico";
                    esperar5Minutos(esperarDisco);
                    esperarDisco = true;
                }
            } else {
                mensagem = "Os valores de alerta padrão e crítico padrão não foram encontrados!";
            }
        }
    }



    public static void verificarCPUSlack(double cpuUso) throws SQLException {
        if (alertaCPU != null && criticoCPU != null) {
            if (cpuUso >= alertaCPU && cpuUso < criticoCPU) {
                mensagem = "CPU em alerta, fique de olho !";
                esperar5Minutos(esperarCPU);
                esperarCPU = true;

            } else if (cpuUso >= criticoCPU) {
                mensagem = "CPU em estado crítico, fique de olho !";
                esperar5Minutos(esperarCPU);
                esperarCPU = true;
            }
        } else {
            if (alertaPadrao != null && criticoPadrao != null) {
                if (cpuUso >= alertaPadrao && cpuUso < criticoPadrao) {
                    mensagem = "CPU em alerta";
                    esperar5Minutos(esperarCPU);
                    esperarCPU = true;

                } else if (cpuUso >= criticoPadrao) {
                    mensagem = "CPU em estado crítico";
                    esperar5Minutos(esperarCPU);
                    esperarCPU = true;
                }
            } else {
                mensagem = "Os valores de alerta padrão e crítico padrão não foram encontrados!";
            }
        }
    }


    public static void verificarRAMSlack(double ramUso) throws SQLException {
        if (alertaRAM != null && criticoRAM != null) {
            if (ramUso >= alertaRAM && ramUso < criticoRAM) {
                mensagem = "memória RAM em alerta, fique de olho !";
                esperar5Minutos(esperarRAM);
                esperarRAM = true;

            } else if (ramUso >= criticoRAM) {
                mensagem = "memória RAM em estado crítico, fique de olho !";
                esperar5Minutos(esperarRAM);
                esperarRAM = true;
            }
        } else {
            if (alertaPadrao != null && criticoPadrao != null) {
                if (ramUso >= alertaPadrao && ramUso < criticoPadrao) {
                    mensagem = "memória RAM em alerta";
                    esperar5Minutos(esperarRAM);
                    esperarRAM = true;

                } else if (ramUso >= criticoPadrao) {
                    mensagem = "memória RAM em estado crítico";
                    esperar5Minutos(esperarRAM);
                    esperarRAM = true;
                }
            } else {
                mensagem = "Os valores de alerta padrão e crítico padrão não foram encontrados!";
            }
        }
    }


    private static String obterCanalDoBancoDeDados() {
        String canal = "";
        String sql = "SELECT canalSlack FROM darkstore WHERE fkEmpresa = 1";

        try  {
            Connection connMysql = Conexao.getConexaoMySQL();
            Connection connSQLServer = Conexao.getConexaoSQLServer();

            PreparedStatement stmtMysql = connMysql.prepareStatement(sql);
            PreparedStatement stmtSQLServer = connSQLServer.prepareStatement(sql);

            ResultSet rs = stmtMysql.executeQuery();
            ResultSet rs2 = stmtSQLServer.executeQuery();
            if (rs.next() || rs2.next()) {
                canal = rs.getString("canalSlack");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter o canal do Slack do banco de dados: " + e.getMessage());
        }
        return canal;
    }
}
