package com.edrastx.pos.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtil {
    private static final String DB_FOLDER = "data";
    private static final String DB_FILE = "sysventas.db";
    private static final String URL = "jdbc:sqlite:" + DB_FOLDER + "/" + DB_FILE;

    public static void init() {
        try {
            Path folder = Paths.get(DB_FOLDER);
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            try (Connection cn = getConnection();
                 Statement st = cn.createStatement()) {
                st.executeUpdate("create table if not exists users(id integer primary key autoincrement,username text unique,password text,role text)");
                st.executeUpdate("create table if not exists products(id integer primary key autoincrement,code text unique,name text,price real,stock integer)");
                st.executeUpdate("create table if not exists clients(id integer primary key autoincrement,name text,document text,phone text)");
                st.executeUpdate("create table if not exists sales(id integer primary key autoincrement,date_time text,client_id integer,customer_name text,total real,user text)");
                st.executeUpdate("create table if not exists sale_items(id integer primary key autoincrement,sale_id integer,product_id integer,quantity integer,price real,subtotal real)");
                ensureDemoUsers(st);
                ensureDemoProducts(st);
                ensureCustomerNameColumn(cn, st);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ensureDemoUsers(Statement st) throws Exception {
        st.executeUpdate("insert or ignore into users(username,password,role) values('estrada','edrast','ADMIN')");
        st.executeUpdate("insert or ignore into users(username,password,role) values('ventas','venta12','VENDEDOR')");
    }

    private static void ensureDemoProducts(Statement st) throws Exception {
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('PAN01','Pan artesanal',1.50,50)");
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('CAF01','Café americano',4.00,40)");
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('CAF02','Cappuccino',5.50,40)");
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('EMP01','Empanada de pollo',3.00,30)");
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('EMP02','Empanada de queso',3.50,30)");
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('PST01','Pastel de chocolate',6.00,20)");
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('PST02','Cheesecake',7.00,20)");
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('TE01','Té caliente',3.50,30)");
        st.executeUpdate("insert or ignore into products(code,name,price,stock) values('JUG01','Jugo natural',5.00,25)");
    }

    private static void ensureCustomerNameColumn(Connection cn, Statement st) {
        try {
            ResultSet rs = st.executeQuery("pragma table_info(sales)");
            boolean found = false;
            while (rs.next()) {
                String name = rs.getString("name");
                if ("customer_name".equalsIgnoreCase(name)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                st.executeUpdate("alter table sales add column customer_name text");
            }
        } catch (Exception e) {
            // ignore
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL);
    }
}
