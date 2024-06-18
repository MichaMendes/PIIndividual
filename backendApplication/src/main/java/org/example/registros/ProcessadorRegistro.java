package org.example.registros;

import org.example.componentes.Componentes;

public class ProcessadorRegistro {

    public static String extrairCPU() {
        String linhasTeste = Componentes.processador();

        String[] linhas = linhasTeste.split("\\r?\\n");

        for (String linha : linhas) {
            if (linha.contains("Em Uso:")) {
                String[] partes = linha.split(":");

                if (partes.length > 1) {
                    String valorEmUso = partes[1].trim();
                    return valorEmUso;
                } else {
                    System.out.println("Formato incorreto para 'ID:'");
                }
            }
        }

        System.out.println("'ID:' não encontrado");
        return "";
    }
    }
