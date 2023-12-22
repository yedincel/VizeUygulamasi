package Vize;

import Vize.BasvuruDurum;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasvuruDurum extends JFrame {
    private JTable table;

    public BasvuruDurum() {
        super("Başvuru Durumu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);

        // İkon
        String iconPath = "src/image/icon.png";
        setIconImage(new ImageIcon(iconPath).getImage());
        
        // Fotoğraf
        String imagePath = "src/image/logo.png";
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(500, 250, Image.SCALE_DEFAULT));
        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Geri Dön Butonu
        JButton geriButon = new JButton("Geri Dön");
        geriButon.setAlignmentX(Component.CENTER_ALIGNMENT); 
        geriButon.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new Main.AnaSayfa();
                setVisible(false);
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(geriButon);
        buttonPanel.setBorder(new EmptyBorder(150, 0, 10, 0));  // Buton ve tablo arasında boşluk bırak
        panel.add(buttonPanel, BorderLayout.NORTH);

        // Tabloyu burada ekleyin
        fetchAndDisplayData();
    }

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
    
    private void fetchAndDisplayData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
            	
                // Veritabanı bağlantısı
                Connection conn = connect();

                String[] columnNames = {"TC Kimlik No", "Pasaport No", "Ad", "Soyad", "Doğum Tarihi", "Ülke", "İl", "Öğrenci Durumu", "Eğitim Durumu", "Çalışma Durumu", "Şirket Adı", "Pozisyon", "Maaş"};

                // Verileri saklamak için bir DefaultTableModel
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(columnNames);

                try {
                    // Sorguyu hazırla
                    String sql = "SELECT * FROM users_data";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    
                    // Sonuçlar     
                    ResultSet rs = pstmt.executeQuery();
                    
                    while (rs.next()) {
                        Object[] row = new Object[13];
                        row[0] = rs.getString("tcKimlikNo");
                        row[1] = rs.getString("pasaportNo");
                        row[2] = rs.getString("ad");
                        row[3] = rs.getString("soyad");
                        row[4] = rs.getString("dogumTarihi");
                        row[5] = rs.getString("ulke");
                        row[6] = rs.getString("il");
                        row[7] = rs.getLong("ogrenciDurumu") == 1 ? "Evet" : "Hayır";
                        row[8] = rs.getString("egitimDurumu");
                        row[9] = rs.getLong("calismaDurumu") == 1 ? "Evet" : "Hayır";
                        row[10] = rs.getString("sirketAdi");
                        row[11] = rs.getString("pozisyon");
                        row[12] = rs.getString("maas");
                        
                        model.addRow(row);
                    }

                    // JTable
                    table = new JTable(model);
                    table.setRowHeight(40);
                    table.setFont(new Font("Serif", Font.BOLD, 14));
                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment( JLabel.CENTER );
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    JScrollPane scrollPane = new JScrollPane(table);
                    add(scrollPane, BorderLayout.CENTER);

                } 
                catch (SQLException e) {
                    // Hata
                    JOptionPane.showMessageDialog(null, "Veri alma sırasında hata oluştu: " + e.getMessage());
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            // Hata
                            JOptionPane.showMessageDialog(null, "Veritabanı bağlantısı kapatılırken hata oluştu: " + e.getMessage());
                        }
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                setVisible(true);
            }
        };
        worker.execute();
    }
}

