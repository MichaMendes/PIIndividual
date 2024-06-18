package org.example.log;

public class Log {
    private String sistemaOperacional;

    private Integer arquitetura;

    private String hostname;

    private String data;

    private String logLevel; //error warning

    private Integer statusCode; //404 exemplo

    private String idMaquina;

    private String mensagem;

    private String stackTrace;

    public Log(String sistemaOperacional, Integer arquitetura, String hostname, String data, String logLevel, Integer statusCode, String idMaquina, String mensagem, String stackTrace) {
        this.sistemaOperacional = sistemaOperacional;
        this.arquitetura = arquitetura;
        this.hostname = hostname;
        this.data = data;
        this.logLevel = logLevel;
        this.statusCode = statusCode;
        this.idMaquina = idMaquina;
        this.mensagem = mensagem;
        this.stackTrace = stackTrace;
    }



    @Override
    public String toString() {
        return """
                {
                sistema Operacional: %s
                arquitetura: %d
                hostname: %s
                Data: %s
                logLevel: %s
                statusCode: %d
                idMaquina: %s
                mensagem: %s
                stackTrace: %s
                }
                """. formatted(sistemaOperacional, arquitetura, hostname, data, logLevel, statusCode, idMaquina, mensagem, stackTrace);
    }
}