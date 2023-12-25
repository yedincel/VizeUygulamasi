package Vize;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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

    public BasvuruDurum(String tcKimlikNo) {
        super("Başvuru Durumu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);

        // İkon
        String iconPath = "src/image/icon.png";
        setIconImage(new ImageIcon(iconPath).getImage());

        // Fotoğraf
        String imagePath = "src/image/logo2.png";
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(800, 220, Image.SCALE_DEFAULT));
        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Geri dön butonu
        JButton geriButon = new JButton("Geri Dön");
        geriButon.setAlignmentX(Component.CENTER_ALIGNMENT);
        geriButon.addActionListener(e -> {
            new Main.AnaSayfa();
            setVisible(false);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(geriButon);
        buttonPanel.setBorder(new EmptyBorder(150, 0, 10, 0));
        panel.add(buttonPanel, BorderLayout.NORTH);

        veriGoruntule(tcKimlikNo);
    }

    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/vizedb";
        String user = "root";
        String password = "yed57*";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Veritabanı bağlantısı kurulurken hata oluştu: " + e.getMessage());
        }
        return conn;
    }

    private void veriGoruntule(String tcKimlikNo) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Veritabanı bağlantısı
                Connection conn = connect();

                String[] columnNames = {"TC Kimlik No", "Pasaport No", "Ad", "Soyad", "Doğum Tarihi", "Ülke", "İl", "Öğrenci Durumu", "Eğitim Durumu", "Çalışma Durumu", "Şirket Adı", "Pozisyon", "Maaş", "Onay Durumu"};

                DefaultTableModel model = new DefaultTableModel() {
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return String.class;
                    }
                };
                model.setColumnIdentifiers(columnNames);

                try {
                    // Admin giriş kontrolü
                    if ("admin".equals(tcKimlikNo)) {
                        // Admin ise tüm başvuruları gösterir
                        String sql = "SELECT * FROM vizedb.users_data";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        ResultSet rs = pstmt.executeQuery();

                        while (rs.next()) {
                            // Verileri ekler
                        	veriEkle(model, rs);
                        }
                    } else {
                        // Admin değilse sadece kendisine ait verileri göster
                        String sql = "SELECT * FROM vizedb.users_data WHERE tcKimlikNo = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, tcKimlikNo.trim());
                        ResultSet rs = pstmt.executeQuery();

                        while (rs.next()) {
                            // Verileri ekler
                        	veriEkle(model, rs);
                        }
                    }

                    table = new JTable(model) {
                        @Override
                        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                            Component comp = super.prepareRenderer(renderer, row, column);
                            String onayDurumu = (String) getValueAt(row, getColumnModel().getColumnIndex("Onay Durumu"));
                            if ("Değerlendiriliyor".equals(onayDurumu)) {
                                comp.setBackground(Color.YELLOW);
                            } else if ("Olumsuz".equals(onayDurumu)) {
                                comp.setBackground(Color.RED);
                            } else if ("Olumlu".equals(onayDurumu)) {
                                comp.setBackground(Color.GREEN);
                            } else {
                                comp.setBackground(getBackground());
                            }
                            return comp;
                        }
                    };

                    table.setRowHeight(40);
                    table.setFont(new Font("Serif", Font.BOLD, 12));
                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }

                    JScrollPane scrollPane = new JScrollPane(table);
                    add(scrollPane, BorderLayout.CENTER);

                    // Admin kontrolü
                    if ("admin".equals(tcKimlikNo)) {
                        addApprovalButton();
                    }

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Veri alma sırasında hata oluştu: " + e.getMessage());
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Veritabanı bağlantısı kapatılırken hata oluştu: " + e.getMessage());
                        }
                    }
                }
                return null;
            }

            private void veriEkle(DefaultTableModel model, ResultSet rs) throws SQLException {
                Object[] row = new Object[14];
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
                row[13] = rs.getString("onay");

                model.addRow(row);
            }
            // Onay durumu değiştirme işlemleri
            private void addApprovalButton() {
                JButton approvalButton = new JButton("Onay Değiştir");
                approvalButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {  
                            String tcKimlikNo = (String) table.getValueAt(selectedRow, table.getColumnModel().getColumnIndex("TC Kimlik No"));
                            String currentApprovalStatus = (String) table.getValueAt(selectedRow, table.getColumnModel().getColumnIndex("Onay Durumu"));
                            String newStatus = (String) JOptionPane.showInputDialog(  
                                    BasvuruDurum.this,
                                    "Onay Durumu Değiştir:",
                                    "Onay Durumu Değiştirme",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    new String[]{"Değerlendiriliyor","Olumlu", "Olumsuz"},
                                    currentApprovalStatus);

                            if (newStatus != null) {
                            	onayGuncelle(tcKimlikNo, newStatus);
                                veriGoruntule(tcKimlikNo);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Lütfen bir satır seçin");
                        }
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(approvalButton);
                buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
                add(buttonPanel, BorderLayout.SOUTH);
            }

            @Override
            protected void done() {
                setVisible(true);
            }
        };
        worker.execute();
    }

    private void onayGuncelle(String tcKimlikNo, String newStatus) {
        String sql = "UPDATE vizedb.users_data SET onay = ? WHERE tcKimlikNo = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, tcKimlikNo.trim());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Onay durumu güncellenirken hata oluştu: " + e.getMessage());
        }
    }
}