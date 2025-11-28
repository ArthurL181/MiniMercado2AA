package minimarket.modelo.A2Mercado.GUI;
import minimarket.modelo.A2Mercado.GeracaoRelatorioProdutos;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class MenuGerenteFrame extends JFrame {
    public MenuGerenteFrame() {
        setTitle("Mini Mercado 2A - Menu Gerente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        class MenuPrincipal {

            public void exibirMenuRelatorios() {
                Scanner scanner = new Scanner(System.in);

                while (true) {
                    System.out.println("\n=== GERAR RELATÓRIOS ===");
                    System.out.println("1. Relatório de Produtos");
                    System.out.println("2. Relatório de Vendas");
                    System.out.println("3. Relatório de Estoque Baixo");
                    System.out.println("4. Voltar ao Menu Principal");
                    System.out.print("Escolha uma opção: ");

                    int opcao = scanner.nextInt();

                    switch (opcao) {
                        case 1:
                            GeracaoRelatorioProdutos.gerarRelatorioProdutos();
                            break;

                        case 2:
                            GeracaoRelatorioProdutos.gerarRelatorioVendas();
                            break;

                        case 3:
                            System.out.print("Defina o limite mínimo de estoque: ");
                            int limite = scanner.nextInt();
                            GeracaoRelatorioProdutos.gerarRelatorioEstoqueBaixo(limite);
                            break;

                        case 4:
                            return;

                        default:
                            System.out.println(" Opção inválida!");
                    }
                }
            }
        }

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