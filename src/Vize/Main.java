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
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends JFrame {

    public Main() {
        super("Kullanıcı Girişi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        // İkon
        String iconPath = "src/image/icon.png";
        setIconImage(new ImageIcon(iconPath).getImage());

        // Fotoğraf
        String imagePath = "src/image/logo.png";
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(400, 190, Image.SCALE_DEFAULT));
        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField emailField;
        addFormField(panel, "E-posta:", emailField = new JTextField(), gbc);
        emailField.setPreferredSize(new Dimension(200, 30));  // Boyutu ayarla

        JPasswordField passwordField;
        addFormField(panel, "Şifre:", passwordField = new JPasswordField(), gbc);
        passwordField.setPreferredSize(new Dimension(200, 30)); 
        
        // Giriş Butonu
        JButton loginButton = new JButton("Giriş Yap");
        loginButton.addActionListener(e -> handleLoginButtonClick(emailField.getText(), new String(passwordField.getPassword())));
        gbc.gridy++;
        gbc.insets = new Insets(20, 120, 5, 5); 
        panel.add(loginButton, gbc);

              add(panel, BorderLayout.CENTER);
              setLocationRelativeTo(null);
              setVisible(true);
        
        // Kaydol Butonu
        JButton kayitButton = new JButton("Kaydol");
        kayitButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		new Kayit(); 
        		setVisible(false); 
        	}
        });

        gbc.insets = new Insets(20, -120, 5, 5);
        panel.add(kayitButton, gbc);
    }

    private void addFormField(JPanel panel, String label, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void handleLoginButtonClick(String email, String password) {
        // Veritabanı bağlantısı
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vizedb", "root", "yed57*")) {

            // SQL sorgusu 
            String sql = "SELECT * FROM users WHERE eposta = ? AND sifre = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            // Sonuçlar
            ResultSet rs = pstmt.executeQuery();

            // Sonuç kontrolü
            if (rs.next()) {
                new AnaSayfa();  
                this.dispose();  
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı kullanıcı adı veya şifre!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class AnaSayfa extends JFrame {
        public AnaSayfa() 
        {
            super("Ana Sayfa");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 400);
            
            // İkon
            String iconPath = "src/image/icon.png";
            setIconImage(new ImageIcon(iconPath).getImage());
            
            // Fotoğraf
            String imagePath = "src/image/logo.png";
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(400, 190, Image.SCALE_DEFAULT));
            JLabel imageLabel = new JLabel(imageIcon);
            add(imageLabel, BorderLayout.NORTH);
            
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(Box.createVerticalStrut(30));

            JButton basvuruButon = new JButton("Vize Başvurusu");
            basvuruButon.setAlignmentX(Component.CENTER_ALIGNMENT);  
            basvuruButon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new BasvuruForm(); 
                    setVisible(false); 
                }
            });
            
            JButton durumButon = new JButton("Başvu Durumu");
            durumButon.setAlignmentX(Component.CENTER_ALIGNMENT);
            durumButon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new BasvuruDurum(); 
                    setVisible(false); 
                }
            });
            
            JButton cikisButon = new JButton("Çıkış Yap");
            cikisButon.setAlignmentX(Component.CENTER_ALIGNMENT);
            cikisButon.addActionListener(new ActionListener()  {
            	@Override
            	public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            	}
            });
            		
            panel.add(basvuruButon);        
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Butonlar arasında boşluk oluşturur
            panel.add(durumButon); 
            panel.add(Box.createRigidArea(new Dimension(0, 10))); 
            panel.add(cikisButon); 
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
