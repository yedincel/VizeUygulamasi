package Vize;

import Vize.BasvuruForm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random; 
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class BasvuruForm extends JFrame {

    private JTextField tcKimlikNoField;
    private JTextField pasaportNoField; 
    private JTextField adField;
    private JTextField soyadField;
    private JTextField dogumTarihiField;
    private JTextField ulkeField;
    private JTextField ilField;   
    private JRadioButton ogrenciEvetRadioButton;
    private JRadioButton ogrenciHayirRadioButton;
    private JComboBox<String> egitimDurumuComboBox;   
    private JRadioButton calismaEvetRadioButton;
    private JRadioButton calismaHayirRadioButton;
    private JTextField sirketAdiField;
    private JTextField pozisyonField;
    private JTextField maasField;
    
    public BasvuruForm() {
        super("Vize Başvuru Formu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);

        // İkon
        String iconPath = "src/image/icon.png";
        setIconImage(new ImageIcon(iconPath).getImage());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // -- Vize Başvuru Formu --
        // TC Kimlik Numarası
        addFormField(panel, "TC Kimlik Numarası:", tcKimlikNoField = new JTextField(), gbc);
        tcKimlikNoField.setPreferredSize(new Dimension(200, 20));
        tcKimlikNoField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || tcKimlikNoField.getText().length() >= 11) { // TC Kimlik numarası 11 hane kontrolü
                    e.consume();
                }
            }
        });
        
        // Pasaport Numarası
        addFormField(panel, "Pasaport Numarası:", pasaportNoField = new JTextField(), gbc);
        pasaportNoField.setPreferredSize(new Dimension(200, 20));
        pasaportNoField.addKeyListener(new KeyAdapter() {
        	public void keyTyped(KeyEvent e) {
        	    if (pasaportNoField.getText().length() >= 9) { // Maksimum 9 haneli kontrolü
        	        e.consume();
        	    }
        	}
        });

        // Ad
        addFormField(panel, "Ad:", adField = new JTextField(), gbc);
        adField.setPreferredSize(new Dimension(200, 20));
        adField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c)) {
                    e.consume();  // Sayı girildiyse, bu karakteri sil
                }
            }
        });
        
        // Soyad
        addFormField(panel, "Soyad:", soyadField = new JTextField(), gbc);
        soyadField.setPreferredSize(new Dimension(200, 20));
        soyadField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c)) {
                    e.consume();  // Sayı girildiyse, bu karakteri sil
                }
            }
        });
    
        // Doğum Tarihi
        addFormField(panel, "Doğum Tarihi:", dogumTarihiField = new JTextField(), gbc);
        dogumTarihiField.setPreferredSize(new Dimension(200, 20));
        dogumTarihiField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '-') {
                    e.consume();  // 
                }
            }
        });
        
        // Ülke
        addFormField(panel, "Ülke:", ulkeField = new JTextField(), gbc);
        ulkeField.setPreferredSize(new Dimension(200, 20));
        ulkeField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c)) {
                    e.consume();
                }
            }
        });

        // Şehir
        addFormField(panel, "Şehir:", ilField = new JTextField(), gbc);
        ilField.setPreferredSize(new Dimension(200, 20));
        ilField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c)) {
                    e.consume();
                }
            }
        });

        // Eğitim Durumu Seçimi 1       
        JPanel ogrenciPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ogrenciPanel.add(ogrenciEvetRadioButton = new JRadioButton("Evet"));
        ogrenciPanel.add(ogrenciHayirRadioButton = new JRadioButton("Hayır"));
    
        ButtonGroup ogrenciGroup = new ButtonGroup();
        ogrenciGroup.add(ogrenciEvetRadioButton);
        ogrenciGroup.add(ogrenciHayirRadioButton);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Öğrenci misiniz?"), gbc);
        gbc.gridx = 1;
        panel.add(ogrenciPanel, gbc);
        
        // Eğitim Durumu Seçimi 2
        addFormField(panel, "Eğitim Durumu:", egitimDurumuComboBox = createEgitimDurumuComboBox(), gbc);
        egitimDurumuComboBox.setPreferredSize(new Dimension(200, 20));

        // Çalışma Durumu Bilgileri 1      
        JPanel calismaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        calismaPanel.add(calismaEvetRadioButton = new JRadioButton("Evet"));
        calismaPanel.add(calismaHayirRadioButton = new JRadioButton("Hayır"));
        ButtonGroup calismaGroup = new ButtonGroup();
        calismaGroup.add(calismaEvetRadioButton);
        calismaGroup.add(calismaHayirRadioButton);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Çalışıyor musunuz?"), gbc);
        gbc.gridx = 1;
        panel.add(calismaPanel, gbc);
   
        // Çalışma Durumu Bilgileri 2
        addFormField(panel, "Şirket Adı:", sirketAdiField = new JTextField(), gbc);
        sirketAdiField.setPreferredSize(new Dimension(200, 20));
        addFormField(panel, "Pozisyon:", pozisyonField = new JTextField(), gbc);
        pozisyonField.setPreferredSize(new Dimension(200, 20));
        
        // Maaş
        addFormField(panel, "Maaş:", maasField = new JTextField(), gbc);
        maasField.setPreferredSize(new Dimension(200, 20));
        maasField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();  
                }
            }
        });

        // Başvuru ve Geri butonu
        JButton basvuruButton = new JButton("Başvur");
        basvuruButton.addActionListener(e -> handleBasvuruButtonClick());

        JButton geriButon = new JButton("Geri Dön");
        geriButon.setAlignmentX(Component.CENTER_ALIGNMENT); 
        geriButon.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new Main.AnaSayfa();
                setVisible(false);
            }
        });

        // Butonları yan yana hizalar
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(geriButon);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Butonlar arasında boşluk
        buttonPanel.add(basvuruButton);

        gbc.gridx = 1;
        gbc.gridy++;
        panel.add(buttonPanel, gbc); 

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

    private JComboBox<String> createEgitimDurumuComboBox() {
        String[] egitimDurumuOptions = {"İlkokul", "Ortaokul", "Lise", "Ön Lisans", "Lisans", "Yüksek Lisans"};
        return new JComboBox<>(egitimDurumuOptions);
    } 	
    
    // Veritabanı bağlantısı 
    private Connection connect() {
      String url = "jdbc:mysql://localhost:3306/vizedb";
      String user = "root";
      String password = "yed57*";
      Connection conn = null;
      try {
        conn = DriverManager.getConnection(url, user, password);
      } catch (SQLException e) {
       
      }
      return conn;
    }
    
    // Verileri ekleyen metot
    public void insert(String tcKimlikNo, String pasaportNo, String ad, String soyad, String dogumTarihi, String ulke, String il, int ogrenciDurumu, String egitimDurumu, int calismaDurum, String sirketAdi, String pozisyon, String maas) {
		
        Connection conn = this.connect();

        try {
            String sql = "INSERT INTO users_data(tcKimlikNo, pasaportNo, ad, soyad, dogumTarihi, ulke, il, ogrenciDurumu, egitimDurumu, calismaDurumu, sirketAdi, pozisyon, maas) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tcKimlikNo);
            pstmt.setString(2, pasaportNo);
            pstmt.setString(3, ad);
            pstmt.setString(4, soyad);
            pstmt.setString(5, dogumTarihi);
            pstmt.setString(6, ulke);
            pstmt.setString(7, il);
            pstmt.setLong(8, ogrenciDurumu);
            pstmt.setString(9, egitimDurumu);
            pstmt.setLong(10, calismaDurum);
            pstmt.setString(11, sirketAdi);
            pstmt.setString(12, pozisyon);
            pstmt.setString(13, maas);

            // Sorguyu çalıştır
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 1) {
                System.out.println("Veri başarıyla eklendi.");
            } else {
                System.out.println("Veri eklenemedi.");
            }

        } catch (SQLException e) {
            System.out.println("Veri ekleme sırasında hata oluştu: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Veritabanı bağlantısı kapatılırken hata oluştu: " + e.getMessage());
                }
            }
        }
    }
    
    private void handleBasvuruButtonClick() {
    
        String tcKimlikNo = tcKimlikNoField.getText();
        String pasaportNo = pasaportNoField.getText();
        String ad = adField.getText();
        String soyad = soyadField.getText();
        String dogumTarihi = dogumTarihiField.getText();
        String ulke = ulkeField.getText();
        String il = ilField.getText();
        
    	String ogrenciDurumuText = ogrenciEvetRadioButton.isSelected() ? "Evet" : "Hayır";
    	int ogrenciDurumu = ogrenciDurumuText.equals("Evet") ? 1 : 0;
    	
    	String egitimDurumu = (String) egitimDurumuComboBox.getSelectedItem();
    	
    	String calismaDurumText = calismaEvetRadioButton.isSelected() ? "Evet" : "Hayır";
    	int calismaDurum = calismaDurumText.equals("Evet") ? 1 : 0;
    	
        String sirketAdi = sirketAdiField.getText();
        String pozisyon = pozisyonField.getText();
        String maas = maasField.getText();
    
	    // Veriler insert edilir
        insert(tcKimlikNo, pasaportNo, ad, soyad, dogumTarihi, ulke, il, ogrenciDurumu, egitimDurumu, calismaDurum, sirketAdi, pozisyon, maas);

        String mesaj = "Başvurunuz değerlendirmeye alınmıştır, en geç 3 iş günü içerisinde sonucunuzu sistemden öğrenebilirsiniz.";
        JOptionPane.showMessageDialog(this, mesaj);
        new Main.AnaSayfa();
        this.dispose();
    }
}
