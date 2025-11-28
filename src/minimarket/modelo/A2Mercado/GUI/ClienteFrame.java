package minimarket.modelo.A2Mercado.GUI;

import minimarket.modelo.A2Mercado.Molde.Cliente;
import minimarket.modelo.A2Mercado.DataBase.ClienteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ClienteFrame extends JFrame {
    private ClienteDAO clienteDAO;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;

    public ClienteFrame() {
        clienteDAO = new ClienteDAO();
        initUI();
        carregarClientes();
    }

    private void initUI() {
        setTitle("Gerenciar Clientes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Tabela
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Email", "Telefone", "CPF"}, 0);
        tabelaClientes = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar Cliente");
        JButton btnEditar = new JButton("Editar");
        JButton btnVoltar = new JButton("Voltar ao Menu");

        btnAdicionar.addActionListener(e -> adicionarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnVoltar.addActionListener(this::actionPerformed);

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnVoltar);
        panel.add(panelBotoes, BorderLayout.SOUTH);

        add(panel);
    }

    private void carregarClientes() {
        modeloTabela.setRowCount(0);
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente c : clientes) {
            modeloTabela.addRow(new Object[]{c.getId(), c.getNome(), c.getEmail(), c.getTelefone(), c.getCpf()});
        }
    }

    private void adicionarCliente() {
        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtTelefone = new JTextField();
        JTextField txtCpf = new JTextField();

        Object[] fields = {
                "Nome:", txtNome,
                "Email:", txtEmail,
                "Telefone:", txtTelefone,
                "CPF:", txtCpf
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Adicionar Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Cliente cliente = new Cliente();
                cliente.setNome(txtNome.getText());
                cliente.setEmail(txtEmail.getText());
                cliente.setTelefone(txtTelefone.getText());
                cliente.setCpf(txtCpf.getText());

                if (clienteDAO.inserirCliente(cliente)) {
                    JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso!");
                    carregarClientes();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarCliente() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar!");
            return;
        }

        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        Cliente cliente = clienteDAO.buscarPorId(id);

        if (cliente != null) {
            JTextField txtNome = new JTextField(cliente.getNome());
            JTextField txtEmail = new JTextField(cliente.getEmail());
            JTextField txtTelefone = new JTextField(cliente.getTelefone());
            JTextField txtCpf = new JTextField(cliente.getCpf());

            Object[] fields = {
                    "Nome:", txtNome,
                    "Email:", txtEmail,
                    "Telefone:", txtTelefone,
                    "CPF:", txtCpf
            };

            int result = JOptionPane.showConfirmDialog(this, fields, "Editar Cliente", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    cliente.setNome(txtNome.getText());
                    cliente.setEmail(txtEmail.getText());
                    cliente.setTelefone(txtTelefone.getText());
                    cliente.setCpf(txtCpf.getText());

                    // Aqui você precisaria implementar o método atualizar no ClienteDAO
                    JOptionPane.showMessageDialog(this, "Funcionalidade de edição em desenvolvimento!");
                    carregarClientes();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void actionPerformed(ActionEvent e) {
        new MenuGerenteFrame().setVisible(true);
        this.dispose();
    }
}