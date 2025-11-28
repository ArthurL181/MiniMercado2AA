package minimarket.modelo.A2Mercado;

import minimarket.modelo.A2Mercado.DataBase.*;
import minimarket.modelo.A2Mercado.GUI.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            minimarket.modelo.A2Mercado.DataBase.DatabaseConnection.initDatabase();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}