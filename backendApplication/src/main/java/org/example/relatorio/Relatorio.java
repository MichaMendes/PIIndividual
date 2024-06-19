package org.example.relatorio;

import com.github.britooo.looca.api.core.Looca;
import org.example.componentes.Maquina;
import org.example.componentes.Processo;
import org.example.componentes.Processo.ProcessoMemoria;
import org.example.componentes.Rede;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Relatorio {
    public static String gerarRelatorio() {
        Looca looca = new Looca();
        String hostname = looca.getRede().getParametros().getHostName();

        String filePath = "relatorio.txt";
        String identificadorMaquina = Maquina.extrairIdentificadorMaquina();
        List<ProcessoMemoria> top10Processos = Processo.extrairMemoriaVirtual();
        String IP = Rede.pegarIP();
        System.out.println("===> " + hostname);
        try (PrintWriter ps = new PrintWriter(filePath)) {
            ps.printf("Identificador da maquina: %s%n%n", identificadorMaquina);
            ps.printf("Hostname: %s%n%n", hostname);
            ps.printf("IP atual da maquina: %s%n%n", IP);
            ps.println("Top 10 Processos que estão consumindo mais:");

            for (ProcessoMemoria processo : top10Processos) {
                ps.println(processo.toString());
            }

            return new File(".").getCanonicalPath() + File.separator + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
