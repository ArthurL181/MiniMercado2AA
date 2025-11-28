package minimarket.modelo.A2Mercado.GUI;

import minimarket.modelo.A2Mercado.DataBase.ProdutoDAO;
import minimarket.modelo.A2Mercado.Molde.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoFrame extends JFrame {
    private ProdutoDAO produtoDAO;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    public ProdutoFrame() {
        produtoDAO = new ProdutoDAO();
        initUI();
        carregarProdutos();
    }

    private void initUI() {
        setTitle("Gerenciar Produtos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Tabela
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Descrição", "Preço", "Estoque"}, 0);
        tabelaProdutos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnVoltar = new JButton("Voltar");

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnVoltar.addActionListener(e -> {
            new MenuGerenteFrame().setVisible(true);
            this.dispose();
        });

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnVoltar);
        panel.add(panelBotoes, BorderLayout.SOUTH);

        add(panel);
    }

    private void carregarProdutos() {
        modeloTabela.setRowCount(0);
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[]{p.getId(), p.getNome(), p.getDescricao(), p.getPreco(), p.getQuantidade()});
        }
    }

    private void adicionarProduto() {
        JTextField txtNome = new JTextField();
        JTextField txtDescricao = new JTextField();
        JTextField txtPreco = new JTextField();
        JTextField txtQuantidade = new JTextField();

        Object[] fields = {
                "Nome:", txtNome,
                "Descrição:", txtDescricao,
                "Preço:", txtPreco,
                "Quantidade:", txtQuantidade
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Adicionar Produto", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Produto produto = new Produto();
                produto.setNome(txtNome.getText());
                produto.setDescricao(txtDescricao.getText());
                produto.setPreco(new BigDecimal(txtPreco.getText()));
                produto.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

                if (produtoDAO.inserirProduto(produto)) {
                    JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
                    carregarProdutos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar!");
            return;
        }

        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        Produto produto = produtoDAO.buscarPorId(id);

        if (produto != null) {
            JTextField txtNome = new JTextField(produto.getNome());
            JTextField txtDescricao = new JTextField(produto.getDescricao());
            JTextField txtPreco = new JTextField(produto.getPreco().toString());
            JTextField txtQuantidade = new JTextField(String.valueOf(produto.getQuantidade()));

            Object[] fields = {
                    "Nome:", txtNome,
                    "Descrição:", txtDescricao,
                    "Preço:", txtPreco,
                    "Quantidade:", txtQuantidade
            };

            int result = JOptionPane.showConfirmDialog(this, fields, "Editar Produto", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    produto.setNome(txtNome.getText());
                    produto.setDescricao(txtDescricao.getText());
                    produto.setPreco(new BigDecimal(txtPreco.getText()));
                    produto.setQuantidade(Integer.parseInt(txtQuantidade.getText()));

                    if (produtoDAO.atualizarProduto(produto)) {
                        JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                        carregarProdutos();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void excluirProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir!");
            return;
        }

        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        String nome = (String) modeloTabela.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o produto: " + nome + "?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (produtoDAO.excluirProduto(id)) {
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                carregarProdutos();
            }
        }
    }
}
