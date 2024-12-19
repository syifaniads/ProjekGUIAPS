import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class AdminPage extends JFrame {
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private ArrayList<Produk> produkList;
    private JPanel productListPanel;

    public AdminPage(ArrayList<Produk> produkList) {
        this.produkList = produkList;
        setTitle("Admin Dashboard - Pabrikdompettas");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Main layout
        setLayout(new BorderLayout());

        // Create header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Create sidebar
        sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // Create main content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Initialize productListPanel
        productListPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productListPanel.setBackground(Color.WHITE);

        // Add content panels
        contentPanel.add(createKelolaProdukPanel(), "KELOLA_PRODUK");
        contentPanel.add(createVerifikasiPesananPanel(), "VERIFIKASI_PESANAN");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        JTextField searchField = new JTextField(30);
        searchPanel.add(searchField);

        // Admin profile
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        profilePanel.setBackground(Color.WHITE);
        JLabel adminLabel = new JLabel("Admin123");
        profilePanel.add(adminLabel);

        header.add(searchPanel, BorderLayout.CENTER);
        header.add(profilePanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton kelolaProdukBtn = createSidebarButton("Kelola Produk");
        JButton verifikasiPesananBtn = createSidebarButton("Verifikasi Pesanan");
        JButton viewKatalogBtn = createSidebarButton("View Katalog");
        JButton logoutBtn = createSidebarButton("Log out");

        kelolaProdukBtn.addActionListener(e -> cardLayout.show(contentPanel, "KELOLA_PRODUK"));
        verifikasiPesananBtn.addActionListener(e -> cardLayout.show(contentPanel, "VERIFIKASI_PESANAN"));
        logoutBtn.addActionListener(e -> handleLogout());

        viewKatalogBtn.addActionListener(e -> {
            // Buka kembali halaman utama katalog
            new EcommerceApp().setVisible(true);
            dispose(); // Tutup halaman admin
        });

        logoutBtn.addActionListener(e -> handleLogout());

        sidebar.add(kelolaProdukBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(verifikasiPesananBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(viewKatalogBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(new Color(240, 240, 240));
        return button;
    }

    private JPanel createKelolaProdukPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton tambahBtn = new JButton("Tambah Produk");
        JButton hapusBtn = new JButton("Hapus Produk");

        tambahBtn.addActionListener(e -> showTambahProdukDialog());
        hapusBtn.addActionListener(e -> showHapusProdukDialog());

        buttonPanel.add(tambahBtn);
        buttonPanel.add(hapusBtn);

        // Update product list
        updateProductList();

        // Add components to panel
        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void updateProductList() {
        productListPanel.removeAll();
        for (Produk produk : produkList) {
            productListPanel.add(createProductCard(produk));
        }
        productListPanel.revalidate();
        productListPanel.repaint();
    }

    private JPanel createProductCard(Produk produk) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(produk.getNama());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(produk.getDeskripsi());
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("Rp " + produk.getHarga());
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));

        return card;
    }

    private void showTambahProdukDialog() {
        JDialog dialog = new JDialog(this, "Tambah Produk", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField idField = new JTextField(20);
        JTextField namaField = new JTextField(20);
        JTextField deskripsiField = new JTextField(20);
        JTextField stokField = new JTextField(20);
        JTextField hargaField = new JTextField(20);
        JTextField kategoriField = new JTextField(20);

        addFormField(panel, "idProduk", idField, gbc, 0);
        addFormField(panel, "nama", namaField, gbc, 1);
        addFormField(panel, "deskripsi", deskripsiField, gbc, 2);
        addFormField(panel, "stok", stokField, gbc, 3);
        addFormField(panel, "harga", hargaField, gbc, 4);
        addFormField(panel, "kategori", kategoriField, gbc, 5);

        JButton uploadBtn = new JButton("Upload");
        uploadBtn.addActionListener(e -> {
            try {
                Produk newProduk = new Produk(
                        idField.getText(),
                        namaField.getText(),
                        deskripsiField.getText(),
                        Integer.parseInt(stokField.getText()),
                        Integer.parseInt(hargaField.getText()),
                        kategoriField.getText()
                );

                produkList.add(newProduk);
                updateProductList();
                showSuccessDialog("Produk berhasil ditambahkan!");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Stok dan harga harus berupa angka!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(uploadBtn, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showHapusProdukDialog() {
        JDialog dialog = new JDialog(this, "Hapus Produk", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField idField = new JTextField(20);
        JTextField namaField = new JTextField(20);
        JTextField deskripsiField = new JTextField(20);
        JTextField stokField = new JTextField(20);
        JTextField hargaField = new JTextField(20);
        JTextField kategoriField = new JTextField(20);

        addFormField(panel, "idProduk", idField, gbc, 0);
        addFormField(panel, "nama", namaField, gbc, 1);
        addFormField(panel, "deskripsi", deskripsiField, gbc, 2);
        addFormField(panel, "stok", stokField, gbc, 3);
        addFormField(panel, "harga", hargaField, gbc, 4);
        addFormField(panel, "kategori", kategoriField, gbc, 5);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> {
            String idToDelete = idField.getText();
            produkList.removeIf(p -> p.getId().equals(idToDelete));
            updateProductList();
            showSuccessDialog("Produk berhasil dihapus!");
            dialog.dispose();
        });

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(deleteBtn, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void showSuccessDialog(String message) {
        JDialog successDialog = new JDialog(this, "Success", true);
        successDialog.setSize(300, 150);
        successDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(messageLabel);

        successDialog.add(panel);
        successDialog.setVisible(true);

        Timer timer = new Timer(2000, e -> successDialog.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    private JPanel createVerifikasiPesananPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel infoLabel = new JLabel("Klik tombol di bawah untuk memverifikasi pesanan:");
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(infoLabel);

        panel.add(Box.createVerticalStrut(10));

        JButton verifikasiBtn = new JButton("Buka Verifikasi Pesanan");
        verifikasiBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        verifikasiBtn.setFont(new Font("Arial", Font.BOLD, 14));

        verifikasiBtn.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI( "https://docs.google.com/spreadsheets/d/1-y3IiTssOqopnWvTf0iP3cuc0OzXoxczfghMZWD33lY/edit?resourcekey=&gid=1864063375#gid=1864063375"
                ));
            } catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal membuka tautan. Pastikan sistem mendukung pembukaan tautan URL.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panel.add(verifikasiBtn);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void handleLogout() {
        dispose();
        new EcommerceApp().setVisible(true);
    }

    public static void openAdminPage(ArrayList<Produk> produkList) {
        SwingUtilities.invokeLater(() -> {
            AdminPage adminPage = new AdminPage(produkList);
            adminPage.setVisible(true);
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AdminPage(new ArrayList<>()).setVisible(true);
        });
    }

}