package org.example.componentes;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Rede {
    private static String IP = "";


    public static String pegarIP() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            IP = ipAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return IP;
    }

}
