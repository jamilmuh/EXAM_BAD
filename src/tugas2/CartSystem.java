package tugas2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class CartSystem extends JFrame {
    private JComboBox<String> cbxJuice;
    private JLabel lblPrice;
    private JTextField tfdQuantity;
    private JButton btnAddCart;
    private JButton btnSubmit;
    private JButton btnClear;
    private JTable tblClear;
    private DefaultTableModel tmlListCart;

    public CartSystem() {
        super("Cart System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Panel for cart form
        JPanel cartFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Jus dropdown
        String[] jusOptions = {"Pilih Jus", "Jus Apel", "Jus Alpukat", "Jus Melon"};
        cbxJuice = new JComboBox<>(jusOptions);
        gbc.gridx = 0;
        gbc.gridy = 0;
        cartFormPanel.add(new JLabel("Pilih Jus:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        cartFormPanel.add(cbxJuice, gbc);

        // Harga label
        gbc.gridx = 0;
        gbc.gridy = 1;
        cartFormPanel.add(new JLabel("Harga:"), gbc);
        lblPrice = new JLabel("Rp 0");
        gbc.gridx = 1;
        gbc.gridy = 1;
        cartFormPanel.add(lblPrice, gbc);

        // Quantity input
        tfdQuantity = new JTextField(5);
        gbc.gridx = 0;
        gbc.gridy = 2;
        cartFormPanel.add(new JLabel("Kuantitas:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        cartFormPanel.add(tfdQuantity, gbc);

        // Add Cart button
        btnAddCart = new JButton("Masukkan keranjang");
        gbc.gridx = 1;
        gbc.gridy = 3;
        cartFormPanel.add(btnAddCart, gbc);

        // Table for cart
        tmlListCart = new DefaultTableModel(new String[]{"Nama Jus", "Kuantitas", "Harga", "Subtotal"}, 0);
        tblClear = new JTable(tmlListCart);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Submit button
        btnSubmit = new JButton("Selesai");
        buttonPanel.add(btnSubmit);

        // Clear button
        btnClear = new JButton("Hapus Data");
        buttonPanel.add(btnClear);

        // Main layout
        setLayout(new BorderLayout());
        add(cartFormPanel, BorderLayout.WEST);
        add(new JScrollPane(tblClear), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Jus dropdown action
        cbxJuice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateHargaLabel();
            }
        });

        // Add Cart button action
        btnAddCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        // Submit button action
        btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInvoiceDialog();
            }
        });

        // Clear button action
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearCart();
            }
        });

        setVisible(true);
    }

    private void addToCart() {
        String jus = (String) cbxJuice.getSelectedItem();
        String quantity = tfdQuantity.getText();

        if (jus == "Pilih Jus" && quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi form untuk melanjutkan!");
        } else if (jus == "Pilih Jus") {
            JOptionPane.showMessageDialog(this, "Pilih jus untuk melanjutkan!");
        } else if (quantity.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan kuantitas untuk melanjutkan!");
        } else if (quantity.matches("^[a-zA-Z]+$")) {
            JOptionPane.showMessageDialog(this, "Masukkan kuantitas dengan angka untuk melanjutkan!");
        } else {

            int quantityInt = Integer.parseInt(quantity);
            int price = getPrice(jus);
            int subtotal = price * quantityInt;

            Vector<String> row = new Vector<>();
            row.add(jus);
            row.add(String.valueOf(quantityInt));
            row.add(String.valueOf(price));
            row.add(String.valueOf(subtotal));
            tmlListCart.addRow(row);

            tfdQuantity.setText("");
        }
    }

    private void updateHargaLabel() {
        String jus = (String) cbxJuice.getSelectedItem();
        int price = getPrice(jus);
        lblPrice.setText("Rp " + price);
    }

    private int getPrice(String jus) {
        switch (jus) {
            case "Jus Apel":
                return 8000;
            case "Jus Alpukat":
                return 12000;
            case "Jus Melon":
                return 10000;
            default:
                return 0;
        }
    }

    private void showInvoiceDialog() {
        int total = 0;
        for (int row = 0; row < tmlListCart.getRowCount(); row++) {
            total += Integer.parseInt(tmlListCart.getValueAt(row, 3).toString());
        }

        JOptionPane.showMessageDialog(this, "Total: " + total);
    }

    private void clearCart() {
        tmlListCart.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CartSystem();
            }
        });
    }
}
