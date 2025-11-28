package minimarket.modelo.A2Mercado.GUI;

import javax.swing.*;
import java.awt.*;

public class MenuGerenteFrame extends JFrame {
    public MenuGerenteFrame() {
        setTitle("Mini Mercado 2A - Menu Gerente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnProdutos = new JButton("📦 Gerenciar Produtos");
        JButton btnClientes = new JButton("👥 Gerenciar Clientes");
        JButton btnVendas = new JButton("💰 Realizar Venda");
        JButton btnRelatorios = new JButton("📊 Relatórios");
        JButton btnHistorico = new JButton("📋 Histórico de Vendas");
        JButton btnSair = new JButton("🚪 Sair");

        btnProdutos.addActionListener(e -> {
            new ProdutoFrame().setVisible(true);
            this.dispose();
        });

        btnClientes.addActionListener(e -> {
            new ClienteFrame().setVisible(true);
            this.dispose();
        });

        btnVendas.addActionListener(e -> {
            new VendaFrame("Gerente").setVisible(true);
            this.dispose();
        });

        btnRelatorios.addActionListener(e -> {
            new RelatorioFrame().setVisible(true);
            this.dispose();
        });

        btnHistorico.addActionListener(e -> {
            new HistoricoVendasFrame().setVisible(true);
            this.dispose();
        });

        btnSair.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        panel.add(btnProdutos);
        panel.add(btnClientes);
        panel.add(btnVendas);
        panel.add(btnRelatorios);
        panel.add(btnHistorico);
        panel.add(btnSair);

        add(panel);
    }
}