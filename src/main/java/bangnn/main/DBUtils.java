/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bangnn.main;

import java.sql.*;

/**
 * @author bangmaple
 */
public class DBUtils {

    private static Connection conn;
    private static PreparedStatement prStm;
    private static Statement stm;
    private static ResultSet rs;

    public static void main(String[] args) throws Exception {
        conn = getMasterConnection();
        createBookDBWithInseredTableData();
    }

    public static void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (prStm != null) {
                prStm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getMasterConnection() {

        conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=master;username=sa;password=Nhatrang1");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection getBookWSConnection() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SWT;username=sa;password=Nhatrang1");
        return conn;
    }

    private static boolean createBookDB() throws SQLException {
        String sqlCreateDB = "CREATE DATABASE SWT";
        stm = conn.createStatement();
        stm.executeUpdate(sqlCreateDB);
        String sqlUseDB = "USE SWT";
        stm = conn.createStatement();
        return stm.executeUpdate(sqlUseDB) >= 0;
    }

    private static boolean createBookTbl() throws SQLException {
        String sqlCreateTable = "CREATE TABLE Book ("
                + "Isbn char(13) primary key,"
                + "Title nvarchar(50) not null,"
                + "Author nvarchar(50) not null,"
                + "Edition int,"
                + "PublishYear int"
                + ");";
        stm = conn.createStatement();
        return stm.executeUpdate(sqlCreateTable) > 0;
    }

    private static boolean insertBookDataIntoTable() throws SQLException {
        boolean check;

        String sqlInsertData1 = "insert into Book values ('2518407786529', N'The Alchemist (Nhà giả kim)', N'Paulo Coelho', 1, 2013);";
        stm = conn.createStatement();
        check = stm.executeUpdate(sqlInsertData1) >= 0;

        stm = conn.createStatement();
        String sqlInsertData2 = "insert into Book values ('6911225907262', N'Tuổi Trẻ Đáng Giá Bao Nhiêu', N'Rosie Nguyễn', 2, 2018);";
        check = stm.executeUpdate(sqlInsertData2) >= 0;

        stm = conn.createStatement();
        String sqlInsertData3 = "insert into Book values ('2425402340697', N'Đời Ngắn Đừng Ngủ Dài', N'Robin Sharma', 2, 2014);";
        check = stm.executeUpdate(sqlInsertData3) >= 0;
        return check;
    }

    public static void createBookDBWithInseredTableData() throws SQLException, ClassNotFoundException {
        try {
            createBookDB();
            System.out.println("Create database.");
            createBookTbl();
            System.out.println("Created table.");
            insertBookDataIntoTable();
            System.out.println("Inserted data to table.");
        } finally {
            closeConnection();
        }
    }

}
