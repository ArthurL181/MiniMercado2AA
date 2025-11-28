package minimarket.modelo.A2Mercado.DataBase;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/minimercado";
    private static final String USER = "root";
    private static final String PASSWORD = "15122007";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void initDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            // Criar database se não existir
            stmt.execute("CREATE DATABASE IF NOT EXISTS minimercado");
            stmt.execute("USE minimercado");

            // Tabela produtos - SINTAXE MYSQL CORRIGIDA
            String sqlProdutos = "CREATE TABLE IF NOT EXISTS produtos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +  // MySQL usa INT AUTO_INCREMENT
                    "nome VARCHAR(100) NOT NULL, " +
                    "descricao TEXT, " +
                    "preco DECIMAL(10,2) NOT NULL, " +
                    "quantidade INT NOT NULL, " +
                    "data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sqlProdutos);

            // Tabela clientes - SINTAXE MYSQL CORRIGIDA
            String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100), " +
                    "telefone VARCHAR(20), " +
                    "cpf VARCHAR(14) UNIQUE)";
            stmt.execute(sqlClientes);

            // Tabela vendas - SINTAXE MYSQL CORRIGIDA
            String sqlVendas = "CREATE TABLE IF NOT EXISTS vendas (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "cliente_id INT, " +
                    "data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "total DECIMAL(10,2) NOT NULL, " +
                    "forma_pagamento VARCHAR(50), " +
                    "FOREIGN KEY (cliente_id) REFERENCES clientes(id))";
            stmt.execute(sqlVendas);

            // Tabela itens_venda - SINTAXE MYSQL CORRIGIDA
            String sqlItensVenda = "CREATE TABLE IF NOT EXISTS itens_venda (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "venda_id INT, " +
                    "produto_id INT, " +
                    "quantidade INT NOT NULL, " +
                    "preco_unitario DECIMAL(10,2) NOT NULL, " +
                    "FOREIGN KEY (venda_id) REFERENCES vendas(id), " +
                    "FOREIGN KEY (produto_id) REFERENCES produtos(id))";
            stmt.execute(sqlItensVenda);

            // Inserir produtos - SINTAXE MYSQL CORRIGIDA
            String insertProdutos = "INSERT IGNORE INTO produtos (nome, descricao, preco, quantidade) VALUES " +
                    "('Arroz', 'Arroz branco tipo 1', 25.90, 50), " +
                    "('Feijão', 'Feijão carioca', 8.50, 30), " +
                    "('Açúcar', 'Açúcar refinado', 4.75, 40), " +
                    "('Café', 'Café torrado e moído', 12.90, 25), " +
                    "('Óleo', 'Óleo de soja', 7.80, 35)";
            stmt.execute(insertProdutos);

            // Inserir clientes - SINTAXE MYSQL CORRIGIDA
            String insertClientes = "INSERT IGNORE INTO clientes (nome, email, telefone, cpf) VALUES " +
                    "('João Silva', 'joao@email.com', '(11) 9999-8888', '123.456.789-00'), " +
                    "('Maria Santos', 'maria@email.com', '(11) 7777-6666', '987.654.321-00')";
            stmt.execute(insertClientes);

            System.out.println("✅ Banco de dados MySQL inicializado com sucesso!");

        } catch (SQLException e) {
            System.err.println(" Erro ao criar banco MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}