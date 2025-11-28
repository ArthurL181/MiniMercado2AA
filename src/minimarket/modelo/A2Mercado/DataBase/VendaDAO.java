package minimarket.modelo.A2Mercado.DataBase;

import minimarket.modelo.A2Mercado.DataBase.DatabaseConnection.*;
import minimarket.modelo.A2Mercado.Molde.Venda;
import minimarket.modelo.A2Mercado.Molde.ItemVenda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public boolean registrarVenda(Venda venda, List<ItemVenda> itens) {
        Connection conn = null;
        try {
            conn = minimarket.modelo.A2Mercado.DataBase.DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Inserir venda
            String sqlVenda = "INSERT INTO vendas (cliente_id, total, forma_pagamento) VALUES (?, ?, ?)";
            PreparedStatement pstmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);

            pstmtVenda.setInt(1, venda.getClienteId());
            pstmtVenda.setBigDecimal(2, venda.getTotal());
            pstmtVenda.setString(3, venda.getFormaPagamento());
            pstmtVenda.executeUpdate();

            // Obter ID da venda inserida
            ResultSet rs = pstmtVenda.getGeneratedKeys();
            int vendaId = 0;
            if (rs.next()) {
                vendaId = rs.getInt(1);
            }

            // Inserir itens da venda
            String sqlItem = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmtItem = conn.prepareStatement(sqlItem);

            for (ItemVenda item : itens) {
                pstmtItem.setInt(1, vendaId);
                pstmtItem.setInt(2, item.getProdutoId());
                pstmtItem.setInt(3, item.getQuantidade());
                pstmtItem.setBigDecimal(4, item.getPrecoUnitario());
                pstmtItem.addBatch();
            }

            pstmtItem.executeBatch();
            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro no rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao registrar venda: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erro ao restaurar auto-commit: " + e.getMessage());
            }
        }
    }

    public List<Venda> listarTodasVendas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT v.*, c.nome as nome_cliente FROM vendas v " +
                "LEFT JOIN clientes c ON v.cliente_id = c.id " +
                "ORDER BY v.data_venda DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getInt("id"));
                venda.setClienteId(rs.getInt("cliente_id"));
                venda.setDataVenda(rs.getTimestamp("data_venda"));
                venda.setTotal(rs.getBigDecimal("total"));
                venda.setFormaPagamento(rs.getString("forma_pagamento"));
                venda.setNomeCliente(rs.getString("nome_cliente"));
                vendas.add(venda);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas: " + e.getMessage());
        }
        return vendas;
    }

    public List<ItemVenda> listarItensVenda(int vendaId) {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT iv.*, p.nome as nome_produto FROM itens_venda iv " +
                "JOIN produtos p ON iv.produto_id = p.id " +
                "WHERE iv.venda_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vendaId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ItemVenda item = new ItemVenda();
                item.setId(rs.getInt("id"));
                item.setVendaId(rs.getInt("venda_id"));
                item.setProdutoId(rs.getInt("produto_id"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
                item.setNomeProduto(rs.getString("nome_produto"));
                itens.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar itens da venda: " + e.getMessage());
        }
        return itens;
    }
}