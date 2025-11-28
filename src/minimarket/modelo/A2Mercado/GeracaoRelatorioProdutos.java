package minimarket.modelo.A2Mercado;
import minimarket.modelo.A2Mercado.DataBase.DatabaseConnection;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeracaoRelatorioProdutos {

    public static void gerarRelatorioProdutos() {
        String nomeArquivo = "relatorio_produtos_" + getDataHoraArquivo() + ".txt";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produtos ORDER BY nome");
             PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            // Cabeçalho do relatório
            writer.println("=".repeat(60));
            writer.println("           RELATÓRIO DE PRODUTOS");
            writer.println("=".repeat(60));
            writer.printf("%-5s %-20s %-10s %-10s%n", "ID", "NOME", "PREÇO", "ESTOQUE");
            writer.println("-".repeat(60));

            // Dados dos produtos
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");

                writer.printf("%-5d %-20s R$%-8.2f %-10d%n",
                        id, nome, preco, quantidade);
            }

            writer.println("-".repeat(60));
            writer.println("Relatório gerado em: " + getDataHoraFormatada());
            writer.println("Arquivo salvo como: " + nomeArquivo);

            System.out.println("✅ Relatório de produtos salvo em: " + nomeArquivo);

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório: " + e.getMessage());
        }
    }

    public static void gerarRelatorioVendas() {
        String nomeArquivo = "relatorio_vendas_" + getDataHoraArquivo() + ".txt";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT v.id, c.nome as cliente, v.data_venda, v.total, v.forma_pagamento " +
                             "FROM vendas v LEFT JOIN clientes c ON v.cliente_id = c.id " +
                             "ORDER BY v.data_venda DESC");
             PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            // Cabeçalho do relatório
            writer.println("=".repeat(80));
            writer.println("                   RELATÓRIO DE VENDAS");
            writer.println("=".repeat(80));
            writer.printf("%-5s %-20s %-20s %-12s %-15s%n",
                    "ID", "CLIENTE", "DATA", "TOTAL", "PAGAMENTO");
            writer.println("-".repeat(80));

            double totalGeral = 0;
            int totalVendas = 0;

            // Dados das vendas
            while (rs.next()) {
                int id = rs.getInt("id");
                String cliente = rs.getString("cliente");
                Timestamp dataVenda = rs.getTimestamp("data_venda");
                double total = rs.getDouble("total");
                String pagamento = rs.getString("forma_pagamento");

                String dataFormatada = dataVenda.toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

                writer.printf("%-5d %-20s %-20s R$%-10.2f %-15s%n",
                        id,
                        cliente != null ? cliente : "CONSUMIDOR",
                        dataFormatada,
                        total,
                        pagamento);

                totalGeral += total;
                totalVendas++;
            }

            writer.println("-".repeat(80));
            writer.printf("TOTAL DE VENDAS: %d%n", totalVendas);
            writer.printf("VALOR TOTAL: R$ %.2f%n", totalGeral);
            writer.println("-".repeat(80));
            writer.println("Relatório gerado em: " + getDataHoraFormatada());
            writer.println("Arquivo salvo como: " + nomeArquivo);

            System.out.println("✅ Relatório de vendas salvo em: " + nomeArquivo);

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório de vendas: " + e.getMessage());
        }
    }

    public static void gerarRelatorioEstoqueBaixo(int limiteMinimo) {
        String nomeArquivo = "relatorio_estoque_baixo_" + getDataHoraArquivo() + ".txt";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM produtos WHERE quantidade <= ? ORDER BY quantidade ASC");
             PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {

            pstmt.setInt(1, limiteMinimo);
            ResultSet rs = pstmt.executeQuery();

            // Cabeçalho do relatório
            writer.println("=".repeat(70));
            writer.println("           RELATÓRIO DE ESTOQUE BAIXO");
            writer.println("=".repeat(70));
            writer.printf("%-5s %-20s %-15s %-10s%n",
                    "ID", "PRODUTO", "ESTOQUE ATUAL", "STATUS");
            writer.println("-".repeat(70));

            int itensComEstoqueBaixo = 0;

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int quantidade = rs.getInt("quantidade");

                String status = quantidade == 0 ? "ESGOTADO" : "ESTOQUE BAIXO";

                writer.printf("%-5d %-20s %-15d %-10s%n",
                        id, nome, quantidade, status);

                itensComEstoqueBaixo++;
            }

            writer.println("-".repeat(70));
            writer.println("LIMITE MÍNIMO DEFINIDO: " + limiteMinimo + " unidades");
            writer.println("ITENS COM ESTOQUE BAIXO: " + itensComEstoqueBaixo);
            writer.println("Relatório gerado em: " + getDataHoraFormatada());
            writer.println("Arquivo salvo como: " + nomeArquivo);

            System.out.println("✅ Relatório de estoque baixo salvo em: " + nomeArquivo);

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório de estoque: " + e.getMessage());
        }
    }

    // Método para formatar data/hora para nome do arquivo
    private static String getDataHoraArquivo() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    // Método para formatar data/hora para exibição
    private static String getDataHoraFormatada() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss"));
    }
}