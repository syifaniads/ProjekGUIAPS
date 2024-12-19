import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


class Produk {
    private String idProduk;
    private String nama;
    private String deskripsi;
    private int stok;
    private int harga;
    private String kategori;

    public Produk(String idProduk, String nama, String deskripsi, int stok, int harga, String kategori) {
        this.idProduk = idProduk;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.stok = stok;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getHarga() {
        return harga;
    }
    public String getId() {
        return idProduk;
    }
    public int getStok() {
        return stok;
    }
}

public class EcommerceApp extends JFrame {
    private JPanel headerPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField emailField;
    private JTextField passwordField;
    private JTextArea outputArea;
    private ArrayList<Produk> produkList;


    private JPanel createOrderFormPanel() {
        JPanel orderPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

//        JTextField productName = new JTextField(20);
//        JTextField businessField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField postcodeField = new JTextField(20);
        JTextField contactField = new JTextField(20);
//        JTextField phoneField = new JTextField(20);
        JTextField emailField = new JTextField(20);

  
        addFormRow(orderPanel, "Nama Pelanggan:", addressField, gbc, 2);
        addFormRow(orderPanel, "Alamat:", postcodeField, gbc, 3);
        addFormRow(orderPanel, "Nomor Telepon:", contactField, gbc, 4);
//        addFormRow(orderPanel, "Contact Phone:", phoneField, gbc, 5);
        addFormRow(orderPanel, "Email:", emailField, gbc, 6);

//        JButton submitButton = new JButton("Buy now");
//        submitButton.addActionListener(e -> showSuccessDialog());

//        gbc.gridy = 7;
//        gbc.gridwidth = 2;
//        orderPanel.add(submitButton, gbc);

        // Google Form Link Button
    JButton formLinkButton = new JButton("Fill out Google Form");
    formLinkButton.addActionListener(e -> openGoogleForm());
    
    gbc.gridy = 8;
    gbc.gridwidth = 2;
    orderPanel.add(formLinkButton, gbc);


        return orderPanel;
    }
    
    private void openGoogleForm() {
        try {
            Desktop.getDesktop().browse(new URI("https://docs.google.com/forms/d/e/1FAIpQLSdPmQPlfZssCajw8GTvn5UDnT8ROHf-IwrDNi4eqY3AXOtlKw/viewform?usp=sharing"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    private void addFormRow(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void showSuccessDialog() {
        JDialog successDialog = new JDialog(this, "Success", true);
        successDialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel successLabel = new JLabel("Payment Successful!");
        JLabel receiptLabel = new JLabel("Receipt sent to your email address");
        JButton backButton = new JButton("Back to browsing");

        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        receiptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(successLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(receiptLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(backButton);
        contentPanel.add(Box.createVerticalGlue());

        successDialog.add(contentPanel);

        backButton.addActionListener(e -> {
            successDialog.dispose();
            cardLayout.show(mainPanel, "CATALOG");
        });

        successDialog.setSize(400, 200);
        successDialog.setLocationRelativeTo(this);
        successDialog.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        outputArea = new JTextArea(5, 20);
        outputArea.setEditable(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("idAdmin:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        loginPanel.add(new JScrollPane(outputArea), gbc);

        loginButton.addActionListener(e -> {
            String idAdmin = emailField.getText();
            String password = new String(((JPasswordField) passwordField).getPassword());
            if (idAdmin.equals("admin123") && password.equals("password123")) {
                outputArea.setText("Login successful");

                // Buka halaman AdminPage
                AdminPage.openAdminPage(produkList);

                // Tutup halaman EcommerceApp
                this.dispose();
            } else {
                outputArea.setText("Invalid Username or Password");
            }
        });
        return loginPanel;
    }
    public EcommerceApp() {
        setTitle("Pabrikdompettas");  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);  

        produkList = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            produkList.add(new Produk(
                    "P00" + i,
                    "Tas Kulit " + i,
                    "Deskripsi singkat produk.",
                    10,
                    105000 + (i * 1000),
                    "Tas"
            ));
        }

        initializeComponents();
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }
    private void initializeComponents() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logoLabel = new JLabel("Pabrikdompettas");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        navPanel.setBackground(new Color(240, 240, 240));

        JButton katalogBtn = createNavButton("KATALOG");
        JButton pesananBtn = createNavButton("PESANAN");
        JButton adminBtn = createNavButton("ADMIN");

        navPanel.add(katalogBtn);
        navPanel.add(pesananBtn);
        navPanel.add(adminBtn);
        headerPanel.add(navPanel, BorderLayout.EAST);
        // Main panel setup
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createCatalogPanel(), "CATALOG");
        mainPanel.add(createOrderFormPanel(), "ORDER_FORM");
        mainPanel.add(createLoginPanel(), "LOGIN");

        // Action listeners
        katalogBtn.addActionListener(e -> cardLayout.show(mainPanel, "CATALOG"));
        pesananBtn.addActionListener(e -> cardLayout.show(mainPanel, "ORDER_FORM"));
        adminBtn.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        cardLayout.show(mainPanel, "CATALOG");
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 240));
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }
    private JPanel createCatalogPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.WHITE);

        JPanel gridPanel = new JPanel(new GridLayout(2, 5, 15, 15));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (Produk produk : produkList) {
            gridPanel.add(createProductCard(produk));
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        outerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBackground(Color.WHITE);
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel boldTitle = new JLabel("Pabrikdompettas", JLabel.CENTER);
        boldTitle.setFont(new Font("Arial", Font.BOLD, 60));

        JLabel subtitle = new JLabel("E-commerce no.1 Indonesia produksi Dompet dan Tas Lokal", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));

        descriptionPanel.add(boldTitle, BorderLayout.NORTH);
        descriptionPanel.add(subtitle, BorderLayout.CENTER);

        outerPanel.add(descriptionPanel, BorderLayout.NORTH);

        return outerPanel;
    }

    private JPanel createProductCard(Produk produk) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);
        
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(200, 200)); 
        imagePanel.setBackground(Color.LIGHT_GRAY); 
        
        ImageIcon imageIcon = new ImageIcon("2.jpg"); 
        JLabel imageLabel = new JLabel(imageIcon);
        
        Image scaledImage = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage)); 
        
        imagePanel.add(imageLabel);
        card.add(imagePanel);
        
        // Product name
        JLabel nameLabel = new JLabel(produk.getNama());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);
        
        // Product description
        JLabel descriptionLabel = new JLabel("<html>" + produk.getDeskripsi() + "</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(descriptionLabel);
        
        // Product price
        JLabel priceLabel = new JLabel("Rp " + produk.getHarga());
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        priceLabel.setForeground(new Color(0, 153, 0)); 
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(priceLabel);
        
        // Product button 
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Detail for: " + produk.getNama() + "\n" +
                "Deskripsi: " + produk.getDeskripsi() + "\n" +
                "Harga: Rp " + produk.getHarga() + "\n" +
                "Stok: " + produk.getStok() + " unit"
            );
        });
        card.add(viewButton, BorderLayout.SOUTH);

        return card;
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new EcommerceApp().setVisible(true);
   });
    }
}