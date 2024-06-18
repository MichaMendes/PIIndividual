package org.example.registros;

import org.example.componentes.Componentes;

public class RamRegistro {
    public static String extrairRam() {
        String linhasTeste = String.valueOf(Componentes.memoriaRamUsoRegistro());

        String[] linhas = linhasTeste.split("\\r?\\n");

        for (String linha : linhas) {
            if (linha.contains("Em uso:")) {
                String[] partes = linha.split(":");

                if (partes.length > 1) {
                    String valorEmUso = partes[1].trim();
                    return valorEmUso;
                } else {
                    System.out.println("Formato incorreto para 'ID:'");
                }
            }
        }

        System.out.println("'Ram:' não encontrada");
        return "";
    }
}
