package minimarket.modelo.A2Mercado.GUI;

import minimarket.modelo.A2Mercado.DataBase.ProdutoDAO;
import minimarket.modelo.A2Mercado.Molde.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RelatorioFrame extends JFrame {
    private JTabbedPane abas;

    public RelatorioFrame() {
        setTitle("Relatórios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        abas = new JTabbedPane();
        abas.addTab("📦 Estoque de Produtos", criarPainelEstoque());
        abas.addTab("📋 Catálogo de Produtos", criarPainelCatalogo());
        abas.addTab("📊 Vendas por Produto", criarPainelVendas());

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> {
            new MenuGerenteFrame().setVisible(true);
            this.dispose();
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(abas, BorderLayout.CENTER);
        panel.add(btnVoltar, BorderLayout.SOUTH);

        add(panel);
    }

    private JPanel criarPainelEstoque() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nome", "Descrição", "Estoque", "Preço"}, 0);
        JTable tabela = new JTable(modelo);

        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos = dao.listarTodos();

        for (Produto p : produtos) {
            modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getDescricao(), p.getQuantidade(), "R$ " + p.getPreco()});
        }

        JScrollPane scroll = new JScrollPane(tabela);
        panel.add(scroll, BorderLayout.CENTER);

        JLabel lblTotal = new JLabel("Total de produtos: " + produtos.size());
        panel.add(lblTotal, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel criarPainelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Nome", "Descrição", "Preço", "Disponível"}, 0);
        JTable tabela = new JTable(modelo);

        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos = dao.listarComEstoque();

        for (Produto p : produtos) {
            String disponivel = p.getQuantidade() > 0 ? "Sim" : "Não";
            modelo.addRow(new Object[]{p.getNome(), p.getDescricao(), "R$ " + p.getPreco(), disponivel});
        }

        JScrollPane scroll = new JScrollPane(tabela);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPainelVendas() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel lblInfo = new JLabel("Relatório de vendas por produto em desenvolvimento...", JLabel.CENTER);
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 14));
        panel.add(lblInfo, BorderLayout.CENTER);
        return panel;
    }
}