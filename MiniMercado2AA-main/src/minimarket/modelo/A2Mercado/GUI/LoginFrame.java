package minimarket.modelo.A2Mercado.GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JComboBox<String> comboModo;

    public LoginFrame() {
        setTitle("Mini Mercado 2A - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("MINI MERCADO 2A", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitulo, gbc);

        // Modo
        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        panel.add(new JLabel("Modo:"), gbc);
        gbc.gridx = 1;
        comboModo = new JComboBox<>(new String[]{"Gerente", "Cliente"});
        panel.add(comboModo, gbc);

        // Usuário
        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        panel.add(txtUsuario, gbc);

        // Senha
        gbc.gridy = 3; gbc.gridx = 0;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtSenha = new JPasswordField(15);
        panel.add(txtSenha, gbc);

        // Botões
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel panelBotoes = new JPanel(new FlowLayout());
        JButton btnLogin = new JButton("Login");
        JButton btnSair = new JButton("Sair");

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fazerLogin();
            }
        });

        btnSair.addActionListener(e -> System.exit(0));

        panelBotoes.add(btnLogin);
        panelBotoes.add(btnSair);
        panel.add(panelBotoes, gbc);

        // Credenciais
        gbc.gridy = 5;
        JLabel lblCredenciais = new JLabel("Gerente: admin | Senha: 123 | Cliente: qualquer usuário", JLabel.CENTER);
        lblCredenciais.setForeground(Color.GRAY);
        panel.add(lblCredenciais, gbc);

        add(panel);
    }

    private void fazerLogin() {
        String modo = (String) comboModo.getSelectedItem();
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        if (modo.equals("Gerente")) {
            if (usuario.equals("admin") && senha.equals("123")) {
                new MenuGerenteFrame().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else { // Modo Cliente
            if (!usuario.trim().isEmpty()) {
                new MenuClienteFrame(usuario).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Digite um nome para continuar como cliente!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}