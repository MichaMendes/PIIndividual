package org.example.telaJFRAME;

import org.example.login.Usuario;

import javax.swing.*;
import java.sql.SQLException;

public class Tela extends JFrame {
    public Tela() throws SQLException {
        super("Minha Tela");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                String email = JOptionPane.showInputDialog("Qual é o e-mail?");
                String senha = JOptionPane.showInputDialog("Qual é a senha?");

                Usuario usuario = new Usuario();
                String respostaBanco = String.valueOf(usuario.validarUser(email, senha));

                JOptionPane.showMessageDialog(null, respostaBanco);
            }
}
