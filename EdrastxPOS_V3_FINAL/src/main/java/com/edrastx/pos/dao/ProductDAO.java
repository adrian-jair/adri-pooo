package com.edrastx.pos.dao;

import com.edrastx.pos.model.Product;
import com.edrastx.pos.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    @Override
    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("select id,code,name,price,stock from products order by name")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setCode(rs.getString("code"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Product findById(int id) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("select id,code,name,price,stock from products where id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setCode(rs.getString("code"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product findByCode(String code) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("select id,code,name,price,stock from products where code=?")) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setCode(rs.getString("code"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Product product) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("insert into products(code,name,price,stock) values(?,?,?,?)")) {
            ps.setString(1, product.getCode());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("update products set code=?,name=?,price=?,stock=? where id=?")) {
            ps.setString(1, product.getCode());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setInt(5, product.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("delete from products where id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStock(int productId, int quantity) {
        try (Connection cn = DBUtil.getConnection();
             PreparedStatement ps = cn.prepareStatement("update products set stock = stock - ? where id=?")) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
