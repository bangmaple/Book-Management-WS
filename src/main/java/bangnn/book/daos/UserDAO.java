package bangnn.book.daos;

import bangnn.main.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static UserDAO INSTANCE = null;
    private Connection conn;
    private PreparedStatement prStm;
    private ResultSet rs;
    private UserDAO() {

    }

    public static UserDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDAO();
        }
        return INSTANCE;
    }

    private void closeConnection() throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (prStm != null) {
            prStm.close();
        }

        if (conn != null) {
            conn.close();
        }
    }

    public boolean isLoggedIn(String username) {
        try {
            conn = DBUtils.getBookWSConnection();
            if (conn != null) {
                String sql = "SELECT IsLoggedIn FROM Users WHERE Username = ?";
                prStm = conn.prepareStatement(sql);
                prStm.setString(1, username);
                rs = prStm.executeQuery();
                if (rs.next()) {
                    return rs.getBoolean("IsLoggedIn");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed at the isLoggedIn method of SQLException. Reason: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Failed at the isLoggedIn method of ClassNotFoundException. Reason: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed at the isLoggedIn method of Exception. Reason: " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {

            }
        }
        return false;
    }

    public boolean revokeUser(String username) {
        boolean check = false;
        try {
            conn = DBUtils.getBookWSConnection();
            if (conn != null) {
                String sql = "UPDATE Users SET IsLoggedIn = 0 WHERE Username = ?";
                prStm = conn.prepareStatement(sql);
                prStm.setString(1, username);
                check = prStm.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Failed at the revokeUser method of SQLException. Reason: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Failed at the revokeUser method of ClassNotFoundException. Reason: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed at the revokeUser method of Exception. Reason: " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {

            }
        }
        return check;
    }

    public void setLoggedIn(String username) {
        try {
            conn = DBUtils.getBookWSConnection();
            if (conn != null) {
                String sql = "UPDATE Users SET IsLoggedIn = 1 WHERE Username = ?";
                prStm = conn.prepareStatement(sql);
                prStm.setString(1, username);
                prStm.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Failed at the setLoggedIn method. Reason: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Failed at the setLoggedIn method. Reason: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed at the setLoggedIn method. Reason: " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {

            }
        }
    }

    public boolean checkLogin(String username, String password) {
        boolean check = false;
        try {
            conn = DBUtils.getBookWSConnection();
            if (conn != null) {
                String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
                prStm = conn.prepareStatement(sql);
                prStm.setString(1, username);
                prStm.setString(2, password);
                rs = prStm.executeQuery();
                check = rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Failed at the checkLogin method. Reason: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Failed at the checkLogin method. Reason: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed at the checkLogin method. Reason: " + e.getMessage());
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {

            }
        }
        return check;
    }

}
