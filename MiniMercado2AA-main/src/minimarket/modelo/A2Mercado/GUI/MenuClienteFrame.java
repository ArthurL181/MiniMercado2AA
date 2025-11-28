package minimarket.modelo.A2Mercado.GUI;

import javax.swing.*;
import java.awt.*;

public class MenuClienteFrame extends JFrame {
    private String nomeCliente;

    public MenuClienteFrame(String nomeCliente) {
        this.nomeCliente = nomeCliente;
        initUI();
    }

    private void initUI() {
        setTitle("Mini Mercado 2A - Bem-vindo, " + nomeCliente);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblBemVindo = new JLabel("Bem-vindo, " + nomeCliente + "!", JLabel.CENTER);
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnComprar = new JButton("🛒 Realizar Compra");
        JButton btnProdutos = new JButton("📦 Ver Produtos");
        JButton btnVoltar = new JButton("↩ Voltar ao Login");

        btnComprar.addActionListener(e -> {
            new VendaFrame(nomeCliente).setVisible(true);
            this.dispose();
        });

        btnProdutos.addActionListener(e -> {
            new CatalogoClienteFrame().setVisible(true);
            this.dispose();
        });

        btnVoltar.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        panel.add(lblBemVindo);
        panel.add(btnComprar);
        panel.add(btnProdutos);
        panel.add(btnVoltar);

        add(panel);
    }
}