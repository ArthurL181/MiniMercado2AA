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
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transação

            // Inserir venda
            String sqlVenda = "INSERT INTO vendas (cliente_id, total, forma_pagamento, data_venda) VALUES (?, ?, ?, NOW())";
            PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            stmtVenda.setInt(1, venda.getClienteId());
            stmtVenda.setBigDecimal(2, venda.getTotal());
            stmtVenda.setString(3, venda.getFormaPagamento());

            int affectedRows = stmtVenda.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            // Obter ID da venda
            ResultSet generatedKeys = stmtVenda.getGeneratedKeys();
            int vendaId = 0;
            if (generatedKeys.next()) {
                vendaId = generatedKeys.getInt(1);
            }

            // Inserir itens
            String sqlItem = "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtItem = conn.prepareStatement(sqlItem);

            for (ItemVenda item : itens) {
                stmtItem.setInt(1, vendaId);
                stmtItem.setInt(2, item.getProdutoId());
                stmtItem.setInt(3, item.getQuantidade());
                stmtItem.setBigDecimal(4, item.getPrecoUnitario());
                stmtItem.addBatch();
            }

            stmtItem.executeBatch();
            conn.commit(); // Confirmar transação
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
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