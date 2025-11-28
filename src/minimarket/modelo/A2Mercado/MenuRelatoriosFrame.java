package minimarket.modelo.A2Mercado;
import minimarket.modelo.A2Mercado.GUI.*;
import minimarket.modelo.A2Mercado.GeracaoRelatorioProdutos;
import javax.swing.*;
import java.awt.*;

public class MenuRelatoriosFrame extends JFrame {

    public MenuRelatoriosFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Mini Mercado 2A - Relatórios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("GERAR RELATÓRIOS", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnRelatorioProdutos = new JButton("📦 Relatório de Produtos");
        JButton btnRelatorioVendas = new JButton("💰 Relatório de Vendas");
        JButton btnRelatorioEstoque = new JButton("⚠️ Relatório de Estoque Baixo");
        JButton btnVoltar = new JButton("↩ Voltar");

        // Ação do botão Relatório de Produtos
        btnRelatorioProdutos.addActionListener(e -> {
            GeracaoRelatorioProdutos.gerarRelatorioProdutos();
            JOptionPane.showMessageDialog(this,
                    "Relatório de produtos gerado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Ação do botão Relatório de Vendas
        btnRelatorioVendas.addActionListener(e -> {
            GeracaoRelatorioProdutos.gerarRelatorioVendas();
            JOptionPane.showMessageDialog(this,
                    "Relatório de vendas gerado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Ação do botão Relatório de Estoque Baixo
        btnRelatorioEstoque.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this,
                    "Defina o limite mínimo de estoque:",
                    "Estoque Baixo",
                    JOptionPane.QUESTION_MESSAGE);

            if (input != null && !input.trim().isEmpty()) {
                try {
                    int limite = Integer.parseInt(input.trim());
                    GeracaoRelatorioProdutos.gerarRelatorioEstoqueBaixo(limite);
                    JOptionPane.showMessageDialog(this,
                            "Relatório de estoque baixo gerado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Por favor, digite um número válido!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ação do botão Voltar
        btnVoltar.addActionListener(e -> {
            new MenuClienteFrame("Cliente").setVisible(true); // Ajuste conforme necessário
            this.dispose();
        });

        panel.add(lblTitulo);
        panel.add(btnRelatorioProdutos);
        panel.add(btnRelatorioVendas);
        panel.add(btnRelatorioEstoque);
        panel.add(btnVoltar);

        add(panel);
    }
}