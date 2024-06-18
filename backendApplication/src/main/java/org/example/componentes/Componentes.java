package org.example.componentes;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processos.Processo;
import java.nio.file.FileStore;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.List;

public class Componentes {
    static Looca looca = new Looca();


    public static String processador() {
        return looca.getProcessador().toString();
    }
    public static Long memoriaTamanhoTotal() {
        return looca.getGrupoDeDiscos().getTamanhoTotal();
    }
    public static Long memoriaDisponivel() {
        Path path = Paths.get("/");
        double disponivel = 0.0;
        try {
            FileStore fileStore = Files.getFileStore(path);

            long freeSpace = fileStore.getUnallocatedSpace();

            disponivel = freeSpace / (1024.0 * 1024.0 * 1024.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return (long) disponivel;
    }
    public static Integer memoriaQtd() {
        return looca.getGrupoDeDiscos().getQuantidadeDeDiscos();
    }

    public static Long memoriaRamUso() {
        return looca.getMemoria().getEmUso();
    }

    public static String hostName() {
        return looca.getRede().getParametros().getHostName();
    }

    public static Memoria memoriaRamUsoRegistro() {
        return looca.getMemoria();
    }


    public static List<Processo> processos() {
        return looca.getGrupoDeProcessos().getProcessos();
    }

    public static Looca getLooca() {
        return looca;
    }

}

