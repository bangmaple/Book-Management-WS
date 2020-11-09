/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bangnn.book.daos;

import bangnn.book.models.Book;
import bangnn.main.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author bangmaple
 */
public class BookDAO {

    private static BookDAO INSTANCE;

    private BookDAO() {
    }

    public static BookDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookDAO();
        }
        return INSTANCE;
    }
    private Connection conn;
    private PreparedStatement prStm;
    private ResultSet rs;

    private void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (prStm != null) {
                prStm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertToDB(Book b) {
        boolean check = false;
        if (retrieveBookByISBN(b.getIsbn()) != null) {
            return check;
        }
        try {
            String sql = "INSERT INTO Book VALUES (?,?,?,?,?)";
            conn = DBUtils.getBookWSConnection();
            prStm = conn.prepareStatement(sql);
            prStm.setString(1, b.getIsbn());
            prStm.setString(2, b.getTitle());
            prStm.setString(3, b.getAuthor());
            prStm.setInt(4, b.getEdition());
            prStm.setInt(5, b.getPublishYear());
            check = prStm.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return check;
    }

    public boolean updateBookToDB(Book b) {
        boolean check = false;
        try {
            String sql = "UPDATE Book SET Title = ?, Author = ?, Edition = ?, PublishYear = ? WHERE Isbn = ?";
            conn = DBUtils.getBookWSConnection();
            prStm = conn.prepareStatement(sql);
            prStm.setString(1, b.getTitle());
            prStm.setString(2, b.getAuthor());
            prStm.setInt(3, b.getEdition());
            prStm.setInt(4, b.getPublishYear());
            prStm.setString(5, b.getIsbn());
            check = prStm.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return check;
    }

    public boolean removeBookFromDBByISBN(String isbn) {
        boolean check = false;
        try {
            String sql = "DELETE FROM Book WHERE Isbn = ?";
            conn = DBUtils.getBookWSConnection();
            prStm = conn.prepareStatement(sql);
            prStm.setString(1, isbn);
            check = prStm.executeUpdate() > 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return check;
    }

    public Book retrieveBookByISBN(String isbn) {
        Book b = null;
        try {
            conn = DBUtils.getBookWSConnection();
            String sql = "SELECT * FROM Book WHERE ISBN = ?";
            prStm = conn.prepareStatement(sql);
            prStm.setString(1, isbn);
            rs = prStm.executeQuery();
            if (rs.next()) {
                b = new Book(isbn, rs.getString("Title"),
                        rs.getString("Author"), rs.getInt("Edition"),
                        rs.getInt("PublishYear"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return b;
    }

    public List<Book> retrieveAllBooks() {
        List<Book> list = null;
        try {
            String sql = "SELECT * FROM Book";
            conn = DBUtils.getBookWSConnection();
            prStm = conn.prepareStatement(sql);
            rs = prStm.executeQuery();
            list = new LinkedList<>();
            while (rs.next()) {
                list.add(new Book(rs.getString("Isbn"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getInt("Edition"),
                        rs.getInt("PublishYear")));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return list;
    }

    public List<Book> retrieveBooksByLikeName(String search) {
        List<Book> list = null;
        try {
            String sql = "SELECT * FROM Book WHERE Title LIKE ?";
            conn = DBUtils.getBookWSConnection();
            prStm = conn.prepareStatement(sql);
            prStm.setString(1, "%" + search + "%");
            rs = prStm.executeQuery();
            list = new LinkedList<>();
            while (rs.next()) {
                list.add(new Book(rs.getString("Isbn"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getInt("Edition"),
                        rs.getInt("PublishYear")));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return list;
    }
}
