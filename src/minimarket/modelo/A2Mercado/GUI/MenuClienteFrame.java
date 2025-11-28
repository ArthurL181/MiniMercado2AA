package minimarket.modelo.A2Mercado.GUI;

import minimarket.modelo.A2Mercado.MenuRelatoriosFrame;
import minimarket.modelo.A2Mercado.GeracaoRelatorioProdutos;
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
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblBemVindo = new JLabel("Bem-vindo, " + nomeCliente + "!", JLabel.CENTER);
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnComprar = new JButton("🛒 Realizar Compra");
        JButton btnProdutos = new JButton("📦 Ver Produtos");
        JButton btnRelatorios = new JButton("📊 Relatórios");
        JButton btnVoltar = new JButton("↩ Voltar ao Login");

        // Ação do botão Comprar
        // Ação do botão Produtos - CORREÇÃO
        btnProdutos.addActionListener(e -> {
            try {
                new CatalogoClienteFrame().setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir catálogo: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        // Ação do botão Produtos
        btnProdutos.addActionListener(e -> {
            new CatalogoClienteFrame().setVisible(true);
            this.dispose();
        });

        // Ação do botão Relatórios (NOVO)
        btnRelatorios.addActionListener(e -> {
            new MenuRelatoriosFrame().setVisible(true);
            this.dispose();
        });

        // Ação do botão Voltar
        btnVoltar.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        panel.add(lblBemVindo);
        panel.add(btnComprar);
        panel.add(btnProdutos);
        panel.add(btnRelatorios);
        panel.add(btnVoltar);

        add(panel);
    }
}