package Vize;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Kayit extends JFrame {
    public Kayit() {
        super("Kullanıcı Kaydı");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        
         // İkon
        String iconPath = "src/image/icon.png";
        setIconImage(new ImageIcon(iconPath).getImage());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField tcField;
        addFormField(panel, "TC Kimlik No:", tcField = new JTextField(), gbc);
        tcField.setPreferredSize(new Dimension(200, 30));  
        tcField.addKeyListener(new KeyAdapter() {  // Sadece rakam girilmesi sağlanır
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                     (c == KeyEvent.VK_BACK_SPACE) ||
                     (c == KeyEvent.VK_DELETE))) {
                  e.consume();
                }
                if (tcField.getText().length() >= 11 ) // Maksimum 11 hane kontrolü
                    e.consume(); 
            }
        });

        JTextField emailField;
        addFormField(panel, "E-posta:", emailField = new JTextField(), gbc);
        emailField.setPreferredSize(new Dimension(200, 30)); 
        
        JPasswordField passwordField;
        addFormField(panel, "Şifre:", passwordField = new JPasswordField(), gbc);
        passwordField.setPreferredSize(new Dimension(200, 30)); 

        // Kaydet Butonu
        JButton saveButton = new JButton("Kaydet");
        saveButton.addActionListener(e -> {
            String tcKimlikNo = tcField.getText();
            String eposta = emailField.getText();
            String sifre = new String(passwordField.getPassword());

            // Burada veritabanına kayıt işlemleri yapılır
            try {
           
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vizedb", "root", "yed57*");

                // SQL sorgusu
                String sql = "INSERT INTO users (tcKimlikNo, eposta, sifre) VALUES (?, ?, ?)";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, tcKimlikNo);
                pstmt.setString(2, eposta);
                pstmt.setString(3, sifre);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Kayıt işlemi yapılmıştır");
                dispose();
                new Main();
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        gbc.gridx = 0;  // Butonu sola hizalar
        gbc.insets = new Insets(30, 20, 0, 0);
        gbc.gridwidth = 2;  // Butonun her iki sütunuda kaplamasını sağlar
        gbc.gridy++;
        
        panel.add(saveButton, gbc);

        add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addFormField(JPanel panel, String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }
}
