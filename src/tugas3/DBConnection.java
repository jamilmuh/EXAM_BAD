package tugas3;

import java.sql.*;

public class DBConnection {
    public Connection connection;
    String host;
    String user;
    String pass;

    public static Statement st;
    public static PreparedStatement ps;
    public static ResultSet rs;

    public DBConnection() {
        host = "jdbc:mysql://localhost/db_exam";
        user = "root";
        pass = "";
        try {
            if (cekDriver()) {
                connection = DriverManager.getConnection(host, user, pass);
                //System.out.println("Connection successfully to database established");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed " + e.getMessage());
        }
    }

    public final boolean cekDriver() {
        boolean isJDBC = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //System.out.println("Driver oke");
            isJDBC = true;
        } catch (ClassNotFoundException c) {
            System.out.println("Driver not found");
        }
        return isJDBC;
    }

    // method untuk menutup koneksi mysql
    public void close() {
        try {
            // tutup koneksi
            this.connection.close();
        } catch (SQLException ex) {
            System.out.println("Penutupan koneksi gagal");
        }
    }

    public ResultSet querySelectSQL(String str) {
        try {
            st = this.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(str);

            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement querySQL(String str) {
        try {
            ps = this.connection.prepareStatement(str);

            return ps;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}