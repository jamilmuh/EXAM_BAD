package tugas3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MutationSystem extends JFrame {
    private JComboBox<String> categoryComboBox;
    private JTextField jumlahField;
    private JTextField noteField;
    private JLabel lblSummary;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable mutasiTable;
    private DefaultTableModel tableModel;

    private DBConnection db;

    private Mutation m;

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public MutationSystem() {
        super("Mutasi System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Panel for form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Category dropdown
        String[] categoryOptions = {"Pilih Category", "Pengeluaran", "Pemasukkan"};
        categoryComboBox = new JComboBox<>(categoryOptions);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(categoryComboBox, gbc);

        // Jumlah input
        jumlahField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Jumlah:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(jumlahField, gbc);

        // Note input
        noteField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Note:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(noteField, gbc);

        // Add button
        addButton = new JButton("Add");
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(addButton, gbc);

        // Add button
        lblSummary = new JLabel("Add");
        gbc.gridx = 2;
        gbc.gridy = 3;
        formPanel.add(lblSummary, gbc);

        // Table for mutasi
        tableModel = new DefaultTableModel(new String[]{"ID", "Category", "Jumlah", "Note"}, 0);
        mutasiTable = new JTable(tableModel);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Update button
        updateButton = new JButton("Update");
        buttonPanel.add(updateButton);

        // Delete button
        deleteButton = new JButton("Delete");
        buttonPanel.add(deleteButton);

        // Main layout
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(mutasiTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add button action
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMutasi();
            }
        });

        // Update button action
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMutasi();
            }
        });

        // Delete button action
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMutasi();
            }
        });

        // Jus dropdown action
        categoryComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateInputJumlah();
            }
        });

        mutasiTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = mutasiTable.getSelectedRow();

            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String category = (String) tableModel.getValueAt(selectedRow, 1);
                int jumlah = (int) tableModel.getValueAt(selectedRow, 2);
                String note = (String) tableModel.getValueAt(selectedRow, 3);

                categoryComboBox.setSelectedItem(category);
                jumlahField.setText(Integer.toString(jumlah));
                noteField.setText(note);
            }
        });

        // Initialize database connection
        db = new DBConnection();
        m = new Mutation();
        rs = m.getAllMutation();

        // Load data into table
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("category");
                int jumlah = rs.getInt("amount");
                String note = rs.getString("note");
                tableModel.addRow(new Object[]{id, category, jumlah, note});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        lblSummary.setText("Rp " + getSumAmount());

        setVisible(true);
    }

    private int getSumAmount() {
        int sumAmount = 0;

        try {
            rs = m.getSumAmountMutation();

            while (rs.next()) {
                sumAmount = rs.getInt("sum_amount");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return sumAmount;
    }

    private void addMutasi() {
        String category = (String) categoryComboBox.getSelectedItem();
        String jumlah = jumlahField.getText();
        String note = noteField.getText();

        if (category == "Pilih Category" && jumlah.isEmpty() && note.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi form untuk melanjutkan!");
        } else if (category == "Pilih Category") {
            JOptionPane.showMessageDialog(this, "Pilih category untuk melanjutkan!");
        } else if (jumlah.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah untuk melanjutkan!");
        } else if (!jumlah.matches("^-?\\d+$")) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah dengan angka untuk melanjutkan!");
        } else if (note.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan note untuk melanjutkan!");
        } else {

            int jumlahInt = Integer.parseInt(jumlah);

            ps = m.insertMutation(category, jumlahInt, note);
            try {
                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Berhasil tambah data mutasi");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            lblSummary.setText("Rp " + getSumAmount());

            db.close();
            Object[] rowData = {tableModel.getRowCount() + 1, category, jumlah, note};
            tableModel.addRow(rowData);

            clearFields();
        }
    }

    private void updateMutasi() {
        int selectedRow = mutasiTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            String category = (String) categoryComboBox.getSelectedItem();
            String jumlah = jumlahField.getText();
            String note = noteField.getText();

            if (category == "Pilih Category" && jumlah.isEmpty() && note.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Isi form untuk melanjutkan!");
            } else if (category == "Pilih Category") {
                JOptionPane.showMessageDialog(this, "Pilih category untuk melanjutkan!");
            } else if (jumlah.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan jumlah untuk melanjutkan!");
            } else if (!jumlah.matches("^-?\\d+$")) {
                JOptionPane.showMessageDialog(this, "Masukkan jumlah dengan angka untuk melanjutkan!");
            } else if (note.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan note untuk melanjutkan!");
            } else {

                int jumlahInt = Integer.parseInt(jumlah);

                ps = m.updateMutation(id, category, jumlahInt, note);
                try {
                    int result = ps.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Berhasil update data mutasi");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                lblSummary.setText("Rp " + getSumAmount());

                tableModel.setValueAt(category, selectedRow, 1);
                tableModel.setValueAt(jumlah, selectedRow, 2);
                tableModel.setValueAt(note, selectedRow, 3);
            }
        }
    }

    private void deleteMutasi() {
        int selectedRow = mutasiTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            ps = m.deleteMutation(id);
            try {
                int result = ps.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Berhasil hapus data mutasi");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            lblSummary.setText("Rp " + getSumAmount());

            db.close();

            tableModel.removeRow(selectedRow);
            clearFields();

        }
    }

    private void updateInputJumlah() {
        String category = (String) categoryComboBox.getSelectedItem();
        String jumlah = (String) jumlahField.getText();

        if (!jumlah.isEmpty()) {
            getStateMutation(category);
        }
    }

    private void getStateMutation(String category) {
        if (category.equals("Pengeluaran")) {
            jumlahField.setText("-" + jumlahField.getText());
        } else {
            jumlahField.setText(jumlahField.getText().replace("-", ""));
        }
    }

    private void clearFields() {
        categoryComboBox.setSelectedIndex(0);
        jumlahField.setText("");
        noteField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MutationSystem();
            }
        });
    }
}
