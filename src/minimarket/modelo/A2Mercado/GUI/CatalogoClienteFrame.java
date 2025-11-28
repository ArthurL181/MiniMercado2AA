package minimarket.modelo.A2Mercado.GUI;

import minimarket.modelo.A2Mercado.DataBase.ProdutoDAO;
import minimarket.modelo.A2Mercado.Molde.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CatalogoClienteFrame extends JFrame {
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    public CatalogoClienteFrame() {
        initUI();
        carregarProdutos();
    }

    private void initUI() {
        setTitle("Catálogo de Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Modelo da tabela
        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Nome", "Preço", "Estoque"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaProdutos = new JTable(modeloTabela);

        // CORREÇÃO: Criar JScrollPane corretamente
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos); // Linha 45 corrigida

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> voltar());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Catálogo de Produtos Disponíveis", JLabel.CENTER), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER); // Usar scrollPane aqui
        panel.add(btnVoltar, BorderLayout.SOUTH);

        add(panel);
    }

    private void carregarProdutos() {
        try {
            ProdutoDAO produtoDAO = new ProdutoDAO();
            List<Produto> produtos = produtoDAO.listarComEstoque();

            modeloTabela.setRowCount(0); // Limpar tabela

            for (Produto produto : produtos) {
                modeloTabela.addRow(new Object[]{
                        produto.getId(),
                        produto.getNome(),
                        String.format("R$ %.2f", produto.getPreco()),
                        produto.getQuantidade()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltar() {
        new MenuClienteFrame("Cliente").setVisible(true);
        this.dispose();
    }
}