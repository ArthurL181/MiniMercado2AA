package minimarket.modelo.A2Mercado.GUI;

import minimarket.modelo.A2Mercado.DataBase.ProdutoDAO;
import minimarket.modelo.A2Mercado.Molde.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CatalogoClienteFrame extends JFrame {

    public CatalogoClienteFrame() {
        initUI();
        carregarProdutos();
    }

    private void initUI() {
        setTitle("Catálogo de Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Tabela de produtos
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"Nome", "Descrição", "Preço", "Estoque"}, 0);
        JTable tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });

        panel.add(btnVoltar, BorderLayout.SOUTH);
        add(panel);
    }

    private void carregarProdutos() {
        ProdutoDAO dao = new ProdutoDAO();
        List<Produto> produtos = dao.listarComEstoque();

        DefaultTableModel modelo = (DefaultTableModel) ((JTable) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getView()).getModel();
        modelo.setRowCount(0);

        for (Produto p : produtos) {
            modelo.addRow(new Object[]{p.getNome(), p.getDescricao(), "R$ " + p.getPreco(), p.getQuantidade()});
        }
    }
}