package Vize;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
// Veri tabanı bağlantıları için gerekli olan kütüphaneler
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends JFrame {

	public Main() {
        super("Kullanıcı Girişi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField emailField;
        addFormField(panel, "E-posta:", emailField = new JTextField(), gbc);
        emailField.setPreferredSize(new Dimension(200, 20));  // Boyutu ayarla

        JPasswordField passwordField;
        addFormField(panel, "Şifre:", passwordField = new JPasswordField(), gbc);
        passwordField.setPreferredSize(new Dimension(200, 20)); 
        
        // Giriş Butonu
        JButton loginButton = new JButton("Giriş Yap");
        loginButton.addActionListener(e -> handleLoginButtonClick(emailField.getText(), new String(passwordField.getPassword())));
        gbc.gridx = 1;
        gbc.gridy++;
        panel.add(loginButton, gbc);

        add(panel);
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

    private void handleLoginButtonClick(String email, String password) {
        // Veritabanı bağlantısı oluştur
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vizedb", "root", "yed57*")) {

            // SQL sorgusu oluştur
            String sql = "SELECT * FROM login WHERE eposta = ? AND sifre = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            // Sorguyu çalıştır ve sonuçları al
            ResultSet rs = pstmt.executeQuery();

            // Sonuçları kontrol et
            if (rs.next()) {
                new AnaSayfa();  // AnaSayfa'yi aç
                this.dispose();  // Kullanıcı girişi penceresini kapat
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı kullanıcı adı veya şifre!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static class AnaSayfa extends JFrame {
        public AnaSayfa() {
            super("Ana Sayfa");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 200);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(Box.createVerticalStrut(30));

            JButton basvuruButon = new JButton("Vize Başvurusu");
            basvuruButon.setAlignmentX(Component.CENTER_ALIGNMENT);
            basvuruButon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new TuristVizeForm(); // Başvuru formunu aç
                    setVisible(false); // AnaSayfa penceresini gizle
                }
            });
            panel.add(basvuruButon); 
            panel.add(Box.createVerticalStrut(30)); 
            add(panel);  
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}