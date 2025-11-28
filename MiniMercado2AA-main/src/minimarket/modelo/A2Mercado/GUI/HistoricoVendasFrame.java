package minimarket.modelo.A2Mercado.GUI;

import minimarket.modelo.A2Mercado.DataBase.VendaDAO;
import minimarket.modelo.A2Mercado.Molde.Venda;
import minimarket.modelo.A2Mercado.Molde.ItemVenda;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoricoVendasFrame extends JFrame {
    private VendaDAO vendaDAO;
    private JTable tabelaVendas;
    private DefaultTableModel modeloTabela;

    public HistoricoVendasFrame() {
        vendaDAO = new VendaDAO();
        initUI();
        carregarVendas();
    }

    private void initUI() {
        setTitle("Histórico de Vendas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Tabela de vendas
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Data", "Cliente", "Total", "Pagamento"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável
            }
        };
        tabelaVendas = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botão para ver detalhes
        JButton btnDetalhes = new JButton("Ver Detalhes da Venda");
        btnDetalhes.addActionListener(e -> verDetalhesVenda());

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(e -> {
            new MenuGerenteFrame().setVisible(true);
            this.dispose();
        });

        JPanel panelBotoes = new JPanel(new FlowLayout());
        panelBotoes.add(btnDetalhes);
        panelBotoes.add(btnVoltar);
        panel.add(panelBotoes, BorderLayout.SOUTH);

        add(panel);
    }

    private void carregarVendas() {
        modeloTabela.setRowCount(0);
        List<Venda> vendas = vendaDAO.listarTodasVendas();
        for (Venda v : vendas) {
            modeloTabela.addRow(new Object[]{
                    v.getId(),
                    v.getDataVenda(),
                    v.getNomeCliente() != null ? v.getNomeCliente() : "Cliente não identificado",
                    "R$ " + v.getTotal(),
                    v.getFormaPagamento()
            });
        }
    }

    private void verDetalhesVenda() {
        int selectedRow = tabelaVendas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para ver os detalhes!");
            return;
        }

        int vendaId = (int) modeloTabela.getValueAt(selectedRow, 0);
        List<ItemVenda> itens = vendaDAO.listarItensVenda(vendaId);

        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Itens da Venda #").append(vendaId).append("\n\n");

        for (ItemVenda item : itens) {
            detalhes.append("• ").append(item.getNomeProduto())
                    .append(" - ").append(item.getQuantidade()).append(" un x R$ ")
                    .append(item.getPrecoUnitario()).append(" = R$ ")
                    .append(item.getSubtotal()).append("\n");
        }

        JOptionPane.showMessageDialog(this, detalhes.toString(), "Detalhes da Venda", JOptionPane.INFORMATION_MESSAGE);
    }

    public record LoginFrame() {
    }
}