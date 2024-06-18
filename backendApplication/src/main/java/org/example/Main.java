package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.login.Usuario;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Usuario usuario = new Usuario();
        Looca looca = new Looca();

//        System.setProperty("javax.net.ssl.trustStore", "NONE");
//        System.setProperty("javax.net.ssl.trustStorePassword", "");
        System.setProperty("slack.sdk.ssl_verify", "false");

        System.out.println("\n" +
                "███████ ███████      ██  █████      ██████  ███████ ███    ███       ██    ██ ██ ███    ██ ██████   ██████  \n" +
                "██      ██           ██ ██   ██     ██   ██ ██      ████  ████       ██    ██ ██ ████   ██ ██   ██ ██    ██ \n" +
                "███████ █████        ██ ███████     ██████  █████   ██ ████ ██ █████ ██    ██ ██ ██ ██  ██ ██   ██ ██    ██ \n" +
                "     ██ ██      ██   ██ ██   ██     ██   ██ ██      ██  ██  ██        ██  ██  ██ ██  ██ ██ ██   ██ ██    ██ \n" +
                "███████ ███████  █████  ██   ██     ██████  ███████ ██      ██         ████   ██ ██   ████ ██████   ██████  \n" +
                "                                                                                                            \n" +
                "                                                                                                            ");
        Scanner perguntaUser = new Scanner(System.in);
        System.out.println("Qual seria o e-mail?");
        String email = perguntaUser.nextLine();
        System.out.println("Qual seria a senha?");
        String senha = perguntaUser.nextLine();


        String respostaBanco = String.valueOf(usuario.validarUser(email, senha));
    }
}
