package tugas3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static tugas3.DBConnection.*;

public class Mutation extends DBConnection {
    // atribut mutation

    public int id;
    public String category;
    public int amount;
    public String note;

    public static String queryAllSQL = "SELECT * FROM mutation";

    private static String querySumAmountSQL = "SELECT SUM(amount) AS sum_amount FROM mutation";

    private static String queryInsertSQL = "INSERT INTO mutation (category, amount, note) VALUES (?, ?, ?)";

    public static String queryUpdateSQL = "UPDATE mutation SET category = ?, amount = ?, note = ? WHERE id = ?";

    public static String queryDeleteSQL = "DELETE FROM mutation WHERE id = ?";

    public static PreparedStatement ps;
    public static ResultSet rs;

    // constructor
    public Mutation(String category, int amount, String note) {
        this.category = category;
        this.amount = amount;
        this.note = note;
    }

    public Mutation() {

    }

    public ResultSet getAllMutation() {
        rs = querySelectSQL(queryAllSQL);
        return rs;
    }

    public ResultSet getSumAmountMutation() {
        rs = this.querySelectSQL(querySumAmountSQL);
        return rs;
    }

    public PreparedStatement insertMutation(String category, int jumlah, String note) {

        try {
            ps = this.querySQL(queryInsertSQL);

            ps.setString(1, category);
            ps.setInt(2, jumlah);
            ps.setString(3, note);

            return ps;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement updateMutation(int id, String category, int jumlah, String note) {

        try {
            ps = this.querySQL(queryUpdateSQL);

            ps.setString(1, category);
            ps.setInt(2, jumlah);
            ps.setString(3, note);
            ps.setInt(4, id);

            return ps;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement deleteMutation(int id) {
        try {
            ps = this.querySQL(queryDeleteSQL);

            ps.setInt(1, id);

            return ps;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
