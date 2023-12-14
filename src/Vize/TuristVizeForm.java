package Vize;

import Vize.TuristVizeForm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TuristVizeForm extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField tcKimlikNoField;
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
    
    public TuristVizeForm() {
        super("Vize Başvuru Formu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
 
        // Vize Başvuru Formu
        addFormField(panel, "TC Kimlik Numarası:", tcKimlikNoField = new JTextField(), gbc);
        tcKimlikNoField.setPreferredSize(new Dimension(200, 20));
        tcKimlikNoField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
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

        // Başvuru Butonu
        JButton basvuruButton = new JButton("Başvur");
        basvuruButton.addActionListener(e -> handleBasvuruButtonClick());
        gbc.gridx = 1;
        gbc.gridy++;
        panel.add(basvuruButton, gbc);

        add(panel);        
        setLocationRelativeTo(null); // Pencereyi ekranın ortasına yerleştirir
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
    
    private void handleBasvuruButtonClick() {
        // Başvurunun değerlendirilmesi (Örnek olarak rastgele bir sonuç oluşturulmuştur)
        Random rand = new Random();
        boolean basvuruSonucu = rand.nextBoolean();

        String mesaj;
        if (basvuruSonucu) {
            mesaj = "Başvurunuz değerlendirildi. Sonuç: Olumlu";
        } 
        else {
            mesaj = "Başvurunuz değerlendirildi. Sonuç: Olumsuz";
        }

        JOptionPane.showMessageDialog(this, mesaj);

        // Ana sayfaya geri dön
        new Main.AnaSayfa();
        this.dispose();
    }

}
    