package minimarket.modelo.A2Mercado.GUI;

import minimarket.modelo.A2Mercado.Molde.Cliente;
import minimarket.modelo.A2Mercado.DataBase.ClienteDAO;
import minimarket.modelo.A2Mercado.DataBase.ProdutoDAO;
import minimarket.modelo.A2Mercado.DataBase.VendaDAO; // ← IMPORT CORRETO
import minimarket.modelo.A2Mercado.Molde.ItemVenda;
import minimarket.modelo.A2Mercado.Molde.Produto;
import minimarket.modelo.A2Mercado.Molde.Venda;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendaFrame extends JFrame {
    private String usuario;
    private DefaultTableModel modeloCarrinho;
    private List<ItemVenda> carrinho;
    private JLabel lblTotal;
    private BigDecimal totalVenda;
    private JComboBox<Produto> comboProdutos;
    private JComboBox<Cliente> comboClientes;

    public VendaFrame(String usuario) {
        this.usuario = usuario;
        this.carrinho = new ArrayList<>();
        this.totalVenda = BigDecimal.ZERO;

        setTitle("Realizar Venda - " + usuario);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel superior - Cliente e Produto
        JPanel panelSuperior = new JPanel(new GridLayout(2, 2, 5, 5));

        panelSuperior.add(new JLabel("Cliente:"));
        comboClientes = new JComboBox<>();
        panelSuperior.add(comboClientes);

        panelSuperior.add(new JLabel("Produto:"));
        comboProdutos = new JComboBox<>();
        panelSuperior.add(comboProdutos);

        // Painel quantidade e adicionar
        JPanel panelQuantidade = new JPanel(new FlowLayout());
        panelQuantidade.add(new JLabel("Quantidade:"));
        JSpinner spinnerQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        panelQuantidade.add(spinnerQuantidade);

        JButton btnAdicionar = new JButton("Adicionar ao Carrinho");
        btnAdicionar.addActionListener(e -> adicionarAoCarrinho(
                (Produto) comboProdutos.getSelectedItem(),
                (Integer) spinnerQuantidade.getValue()
        ));
        panelQuantidade.add(btnAdicionar);

        // Tabela do carrinho
        modeloCarrinho = new DefaultTableModel(
                new String[]{"Produto", "Quantidade", "Preço Unit.", "Subtotal"}, 0
        );
        JTable tabelaCarrinho = new JTable(modeloCarrinho);
        JScrollPane scrollCarrinho = new JScrollPane(tabelaCarrinho);

        // Painel inferior - Total e botões
        JPanel panelInferior = new JPanel(new BorderLayout());

        JPanel panelTotal = new JPanel(new FlowLayout());
        panelTotal.add(new JLabel("Total da Venda:"));
        lblTotal = new JLabel("R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(Color.BLUE);
        panelTotal.add(lblTotal);

        JPanel panelBotoes = new JPanel(new FlowLayout());
        JButton btnFinalizar = new JButton("Finalizar Venda");
        JButton btnRemover = new JButton("Remover Item");
        JButton btnLimpar = new JButton("Limpar Carrinho");
        JButton btnVoltar = new JButton("Voltar");

        btnFinalizar.addActionListener(e -> finalizarVenda());
        btnRemover.addActionListener(e -> removerItem(tabelaCarrinho.getSelectedRow()));
        btnLimpar.addActionListener(e -> limparCarrinho());
        btnVoltar.addActionListener(e -> voltar());

        panelBotoes.add(btnFinalizar);
        panelBotoes.add(btnRemover);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnVoltar);

        panelInferior.add(panelTotal, BorderLayout.NORTH);
        panelInferior.add(panelBotoes, BorderLayout.SOUTH);

        // Montagem do layout
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelQuantidade, BorderLayout.CENTER);
        panel.add(scrollCarrinho, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        add(panel);
    }

    private void carregarDados() {
        // Carregar produtos
        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.listarComEstoque();
        for (Produto p : produtos) {
            comboProdutos.addItem(p);
        }

        // Carregar clientes (apenas para gerente)
        if (usuario.equals("Gerente")) {
            ClienteDAO clienteDAO = new ClienteDAO();
            List<Cliente> clientes = clienteDAO.listarTodos();
            for (Cliente c : clientes) {
                comboClientes.addItem(c);
            }
        } else {
            // Para cliente, criar um cliente temporário
            Cliente clienteTemp = new Cliente(usuario, "", "", "000.000.000-00");
            comboClientes.addItem(clienteTemp);
        }
    }

    private void adicionarAoCarrinho(Produto produto, int quantidade) {
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!");
            return;
        }

        if (quantidade > produto.getQuantidade()) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade indisponível! Estoque: " + produto.getQuantidade());
            return;
        }

        // Verificar se produto já está no carrinho
        for (ItemVenda item : carrinho) {
            if (item.getProdutoId() == produto.getId()) {
                int novaQuantidade = item.getQuantidade() + quantidade;
                if (novaQuantidade > produto.getQuantidade()) {
                    JOptionPane.showMessageDialog(this,
                            "Quantidade total excede estoque! Estoque: " + produto.getQuantidade());
                    return;
                }
                item.setQuantidade(novaQuantidade);
                atualizarTabelaCarrinho();
                return;
            }
        }

        // Adicionar novo item
        ItemVenda novoItem = new ItemVenda();
        novoItem.setProdutoId(produto.getId());
        novoItem.setQuantidade(quantidade);
        novoItem.setPrecoUnitario(produto.getPreco());
        novoItem.setNomeProduto(produto.getNome());

        carrinho.add(novoItem);
        atualizarTabelaCarrinho();
    }

    private void atualizarTabelaCarrinho() {
        modeloCarrinho.setRowCount(0);
        totalVenda = BigDecimal.ZERO;

        for (ItemVenda item : carrinho) {
            BigDecimal subtotal = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
            modeloCarrinho.addRow(new Object[]{
                    item.getNomeProduto(),
                    item.getQuantidade(),
                    "R$ " + item.getPrecoUnitario(),
                    "R$ " + subtotal
            });
            totalVenda = totalVenda.add(subtotal);
        }

        lblTotal.setText("R$ " + totalVenda);
    }

    private void removerItem(int linha) {
        if (linha >= 0 && linha < carrinho.size()) {
            carrinho.remove(linha);
            atualizarTabelaCarrinho();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover!");
        }
    }

    private void limparCarrinho() {
        carrinho.clear();
        atualizarTabelaCarrinho();
    }

    private void finalizarVenda() {
        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Carrinho vazio!");
            return;
        }

        // Selecionar forma de pagamento
        String[] opcoesPagamento = {"Dinheiro", "Cartão de Crédito", "Cartão de Débito", "PIX"};
        String formaPagamento = (String) JOptionPane.showInputDialog(
                this, "Selecione a forma de pagamento:", "Forma de Pagamento",
                JOptionPane.QUESTION_MESSAGE, null, opcoesPagamento, opcoesPagamento[0]
        );

        if (formaPagamento == null) return;

        // Confirmar venda
        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirmar venda?\nTotal: R$ " + totalVenda + "\nForma de pagamento: " + formaPagamento,
                "Confirmar Venda", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Registrar venda no banco
                Venda venda = new Venda();
                Cliente clienteSelecionado = (Cliente) comboClientes.getSelectedItem();
                venda.setClienteId(clienteSelecionado.getId());
                venda.setTotal(totalVenda);
                venda.setFormaPagamento(formaPagamento);

                VendaDAO vendaDAO = new VendaDAO(); // ← CORREÇÃO AQUI
                ProdutoDAO produtoDAO = new ProdutoDAO();

                if (vendaDAO.registrarVenda(venda, carrinho)) {
                    // Atualizar estoque
                    for (ItemVenda item : carrinho) {
                        produtoDAO.atualizarEstoque(item.getProdutoId(), item.getQuantidade());
                    }

                    JOptionPane.showMessageDialog(this,
                            "Venda realizada com sucesso!\nTotal: R$ " + totalVenda);
                    limparCarrinho();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao registrar venda!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao processar venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void voltar() {
        if (usuario.equals("Gerente")) {
            new MenuGerenteFrame().setVisible(true);
        } else {
            new MenuClienteFrame(usuario).setVisible(true);
        }
        this.dispose();
    }
}