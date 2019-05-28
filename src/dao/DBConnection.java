package dao;


import com.mysql.jdbc.Connection;

import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnection {

    private Connection baza;
    private static DBConnection instance;

    private DBConnection() {

        try {

            Properties pro = new Properties();
            pro.load(new FileInputStream("D:\\Programowanie\\Java\\" +
                    "programy z IntelliJ I\\Programy Java 2\\Cwiczenie8.1\\" +
                    "src\\db.properties"));

            String user1 = pro.getProperty("user");
            String password1 = pro.getProperty("password");
            String url1 = pro.getProperty("dburl");

            baza = (Connection) DriverManager.getConnection(url1, user1, password1);
        } catch (SQLException e) {
            System.out.println("Jestem w DBConnection przy konstruktorze");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public ResultSet zapytanie(String z) {
        ResultSet resultset = null;
        try {
            Statement statement = baza.createStatement();
            resultset = statement.executeQuery(z);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultset;
    }

    public boolean modyfikacja(String m) {
        try {
            Statement statement = baza.createStatement();
            int liczba = statement.executeUpdate(m);
            if (liczba > 0) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void odłączenie(boolean b, ResultSet r1, ResultSet r2) {
        try {
            if (b == true) baza.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (r1 != null) r1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void zniszczenie(ResultSet r) {
        odłączenie(false, r, null);
    }

}

